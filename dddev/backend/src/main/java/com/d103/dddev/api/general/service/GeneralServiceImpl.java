package com.d103.dddev.api.general.service;

import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.repository.GeneralRepository;
import com.d103.dddev.api.general.repository.dto.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.TransactionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InvalidAttributeValueException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class GeneralServiceImpl implements GeneralService{

    private final GeneralRepository generalRepository;

    @Override
    public General insertGeneral(int groundId, GeneralInsertOneDto generalInsertOneDto) throws InvalidAttributeValueException{
        int step = generalInsertOneDto.getStep(); // 문서의 step
        General insertGeneral; // DB에 저장될 문서
        General parent; // 저장될 문서의 부모

        if(!stepIsRange(step)) throw new InvalidAttributeValueException("잘못된 step입니다.");
        // step1의 문서는 부모가 필요가 없다.
        if(step == 1){
            // 저장할 문서 생성
            insertGeneral = General.builder()
                    .groundId(groundId)
                    .step(step)
                    .title(generalInsertOneDto.getTitle())
                    .build();
            try{
                generalRepository.save(insertGeneral);
            }catch(Exception e){
                throw new TransactionException("문서 저장에 실패했습니다.");
            }
        }
        // step1이 아닌 문서들
        else{
//            // 미분류 문서를 만들것인가?
//            if(generalInsertOneDto.getParentId() == null){
//
//            }

            // 저장할 문서 생성
            insertGeneral = General.builder()
                    .groundId(groundId)
                    .step(step)
                    .title(generalInsertOneDto.getTitle())
                    .parentId(generalInsertOneDto.getParentId())
                    .build();
            try{
                generalRepository.save(insertGeneral);
            }catch(Exception e){
                throw new TransactionException("문서 저장에 실패했습니다.");
            }

            parent = generalRepository.findById(generalInsertOneDto.getParentId()).orElseThrow(()->new NoSuchElementException("부모 문서를 찾을 수 없습니다."));
            List<General> children = parent.getChildren();
            if(children == null){
                children = new ArrayList<>();
            }
            children.add(insertGeneral);
            parent.setChildren(children);
            // 부모 문서의 자식을 업데이트한다.
            try{
                generalRepository.save(parent);
            }catch(Exception e){
                throw new TransactionException("부모 문서를 저장하는데 실패했습니다.");
            }
        }

        return insertGeneral;
    }

    @Override
    public List<General> insertGeneralsWithTitles(int groundId, GeneralInsertManyDto generalInsertManyDto) {
        List<General> list = new ArrayList<>();
        for(String title : generalInsertManyDto.getTitles()){
            General insertGeneral = General.builder()
                        .groundId(groundId)
                        .step(1)
                        .title(title)
                        .build();
            list.add(insertGeneral);
        }
        try{
            generalRepository.saveAll(list);
        }catch(Exception e){
            throw new TransactionException("문서 저장에 실패했습니다.");
        }
        return list;
    }

    @Override
    public General getGeneral(int groundId, String generalId) {
        return generalRepository.findById(generalId).orElseThrow(()-> new TransactionException("문서를 불러오는데 실패했습니다."));
    }

    @Override
    public List<General> getStep1Generals(int groundId){
        return generalRepository.findByGroundIdAndStep(groundId, 1).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
    }

    @Override
    public List<General> getStep2Generals(int groundId){
        return generalRepository.findByGroundIdAndStep(groundId, 2).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
    }

    @Override
    public General updateGeneral(int groundId, GeneralUpdateDto generalUpdateDto) {
        General loadGeneral = generalRepository.findById(generalUpdateDto.getId()).orElseThrow(()->new TransactionException("문서를 불러오는데 실패했습니다."));
        int step = loadGeneral.getStep();
        loadGeneral.setTitle(generalUpdateDto.getTitle());
        loadGeneral.setContent(generalUpdateDto.getContent());
        loadGeneral.setUpdatedAt(LocalDateTime.now());
        try{
            generalRepository.save(loadGeneral);
        }catch(Exception e){
            throw new TransactionException("문서 업데이트를 실패했습니다.");
        }
        // step1 문서가 아니라면 부모를 찾아서 업데이트해줘야한다.
        if(step != 1){
            String parentId = loadGeneral.getParentId();
            General parent = generalRepository.findById(parentId).orElseThrow(()->new TransactionException("부모 문서를 불러오는데 실패했습니다."));
            List<General> children = parent.getChildren();
            ListIterator<General> iterator = children.listIterator();
            while (iterator.hasNext()) {
                General child = iterator.next();
                if (child.getId().equals(loadGeneral.getId())) {
                    iterator.set(loadGeneral);
                }
            }
            parent.setChildren(children);
            try{
                generalRepository.save(parent);
            }catch(Exception e){
                throw new TransactionException("부모 문서를 저장하는데 실패했습니다.");
            }
        }

        return loadGeneral;
    }

    @Override
    public General moveGeneral(int groundId, GeneralMoveDto GeneralMoveDto) throws InvalidAttributeValueException{
        General loadGeneral = generalRepository.findById(GeneralMoveDto.getId()).orElseThrow(()->new NoSuchElementException("잘못된 문서 아이디입니다."));
        if(loadGeneral.getStep() == 1){
            throw new InvalidAttributeValueException("움직일 수 없는 문서입니다.");
        }
        String originParentId = loadGeneral.getParentId();
        General originParent = generalRepository.findById(originParentId).orElseThrow(()->new NoSuchElementException("잘못된 부모 아이디입니다."));
        String newParentId = GeneralMoveDto.getParentId();
        General newParent = generalRepository.findById(newParentId).orElseThrow(()->new NoSuchElementException("잘못된 부모 아이디입니다."));

        // 원래 부모문서에서 자기 지우기
        List<General> originChildren = originParent.getChildren();
        originChildren.removeIf(child -> (child.getId().equals(loadGeneral.getId())));
        originParent.setChildren(originChildren);

        // 새로운 부모문서에 자기 추가하기
        List<General> newChildren = newParent.getChildren();
        newChildren.add(loadGeneral);
        newParent.setChildren(newChildren);

        // 자기 부모아이디 수정하기
        loadGeneral.setParentId(newParentId);

        try{
            generalRepository.save(originParent);
            generalRepository.save(newParent);
            generalRepository.save(loadGeneral);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }
        return loadGeneral;
    }


    @Override
    public void deleteGeneral(int groundId, GeneralDeleteDto generalDeleteDto) {
        String generalId = generalDeleteDto.getId();
        General loadGeneral = generalRepository.findById(generalId).orElseThrow(()->new TransactionException("문서를 불러오는데 실패했습니다."));
        int step = loadGeneral.getStep();
        // step1인 문서가 삭제되었을 때
        if(step == 1){
            List<General> children = generalRepository.findByParentId(generalId).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
            ListIterator<General> iterator = children.listIterator();
            while(iterator.hasNext()){
                General child = iterator.next();
                child.setParentId(null);
            }
            try{
                generalRepository.saveAll(children);
            }catch(Exception e){
                throw new TransactionException("문서들을 저장하는데 실패했습니다.");
            }
        }
        else{
            // 부모를 업데이트한다.
            String parentId = loadGeneral.getParentId();
            General parent = generalRepository.findById(parentId).orElseThrow(()-> new TransactionException("문서를 불러오는데 실패했습니다."));
            List<General> children = parent.getChildren();
            children.removeIf((child) -> (child.getId().equals(generalId)));
            parent.setChildren(children);
            try{
                generalRepository.save(parent);
            }catch(Exception e){
                throw new TransactionException("부모를 저장하는데 실패했습니다.");
            }
        }
        // 문서 삭제
        try{
            generalRepository.deleteById(generalId);
        }catch(Exception e){
            throw new TransactionException("문서를 삭제하는데 실패했습니다.");
        }
    }

    public boolean stepIsRange(int step){
        return step>=1 && step<=2;
    }

}
