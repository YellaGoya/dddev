package com.d103.dddev.api.general.service;

import com.d103.dddev.api.file.service.DocumentServiceImpl;
import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.repository.GeneralRepository;
import com.d103.dddev.api.general.repository.dto.requestDto.*;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralStepResponseDto;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralTreeResponseDto;
import com.d103.dddev.common.exception.document.DocumentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.TransactionException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InvalidAttributeValueException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Service
@RequiredArgsConstructor
@Transactional
public class GeneralServiceImpl implements GeneralService{

    private final GeneralRepository generalRepository;
    private final DocumentServiceImpl documentService;

    @Override
    public General insertGeneral(int groundId, GeneralInsertOneDto generalInsertOneDto, UserDetails userDetails) throws Exception{
        General insertGeneral = new General(); // DB에 저장될 문서
        General parent; // 저장될 문서의 부모


        insertGeneral.setGroundId(groundId);
        if(generalInsertOneDto.getTitle() == null)
            insertGeneral.setTitle("");
        else{
            insertGeneral.setTitle(generalInsertOneDto.getTitle());
        }
        insertGeneral.setAuthor(userDetails.getUsername());
        insertGeneral.setModifier(userDetails.getUsername());

        if(generalInsertOneDto.getParentId() == null){
            insertGeneral.setStep(1);
            try{
                generalRepository.save(insertGeneral);
            }catch(Exception e){
                throw new TransactionException("문서 저장에 실패했습니다.");
            }
        }
        else{
            insertGeneral.setStep(2);
            insertGeneral.setParentId(generalInsertOneDto.getParentId());

            try{
                generalRepository.save(insertGeneral);
            }catch(Exception e){
                throw new TransactionException("문서 저장에 실패했습니다.");
            }

            parent = generalRepository.findById(generalInsertOneDto.getParentId()).orElseThrow(()->new DocumentNotFoundException("부모 문서를 찾을 수 없습니다."));
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
    public List<General> insertGeneralsWithTitles(int groundId, GeneralInsertManyDto generalInsertManyDto, UserDetails userDetails) {
        List<General> list = new ArrayList<>();
        for(String title : generalInsertManyDto.getTitles()){
            General insertGeneral = General.builder()
                        .groundId(groundId)
                        .step(1)
                        .title(title)
                        .author(userDetails.getUsername())
                        .modifier(userDetails.getUsername())
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
    public General getGeneral(int groundId, String generalId) throws Exception{
        return generalRepository.findById(generalId).orElseThrow(()-> new DocumentNotFoundException("해당 문서가 존재하지 않습니다."));
    }

    /**
     * 문서 구조를 트리로 보내주는 함수
     * @param groundId
     * @return
     */
    @Override
    public List<GeneralTreeResponseDto> getTreeGenerals(int groundId){
        List<General> generalList = generalRepository.findByGroundIdAndStep(groundId, 1).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
        List<GeneralTreeResponseDto> generalResponseDtoList = new ArrayList<>();
        for (General general : generalList) {
            GeneralTreeResponseDto generalResponseDto = convertToGeneralTreeResponseDto(general);
            generalResponseDtoList.add(generalResponseDto);
        }
        return generalResponseDtoList;
    }

    @Override
    public List<GeneralStepResponseDto> getStep1Generals(int groundId){
        List<General> generalList = generalRepository.findByGroundIdAndStep(groundId, 1).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
        List<GeneralStepResponseDto> generalResponseDtoList = new ArrayList<>();
        for (General general : generalList) {
            GeneralStepResponseDto generalResponseDto = convertToGeneralStepResponseDto(general);
            generalResponseDtoList.add(generalResponseDto);
        }
        return generalResponseDtoList;
    }

    @Override
    public List<General> getStep2Generals(int groundId){
        return generalRepository.findByGroundIdAndStep(groundId, 2).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
    }

    @Override
    public General updateGeneral(int groundId, String generalId, GeneralUpdateDto generalUpdateDto, UserDetails userDetails) throws Exception{
        General loadGeneral = generalRepository.findById(generalId).orElseThrow(()->new DocumentNotFoundException("해당 문서를 불러오는데 실패했습니다."));
        int step = loadGeneral.getStep();
        String title = generalUpdateDto.getTitle();
        String content = generalUpdateDto.getContent();


        if(title == null && content == null) return loadGeneral;
        if(title != null) loadGeneral.setTitle(title);
        if(content != null) loadGeneral.setContent(content);
        loadGeneral.setUpdatedAt(LocalDateTime.now());
        loadGeneral.setModifier(userDetails.getUsername());
        try{
            generalRepository.save(loadGeneral);
        }catch(Exception e){
            throw new TransactionException("문서 업데이트를 실패했습니다.");
        }
        // step1 문서가 아니라면 부모를 찾아서 업데이트해줘야한다.
        if(step != 1){
            String parentId = loadGeneral.getParentId();
            General parent = generalRepository.findById(parentId).orElseThrow(()->new DocumentNotFoundException("부모 문서를 불러오는데 실패했습니다."));
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
    public General moveGeneral(int groundId, String generalId, GeneralMoveDto GeneralMoveDto) throws Exception{
        General loadGeneral = generalRepository.findById(generalId).orElseThrow(()->new DocumentNotFoundException("잘못된 문서 아이디입니다."));
        if(loadGeneral.getStep() == 1){
            throw new InvalidAttributeValueException("움직일 수 없는 문서입니다.");
        }
        String originParentId = loadGeneral.getParentId();
        General originParent = generalRepository.findById(originParentId).orElseThrow(()->new DocumentNotFoundException("잘못된 부모 아이디입니다."));
        String newParentId = GeneralMoveDto.getParentId();
        General newParent = generalRepository.findById(newParentId).orElseThrow(()->new DocumentNotFoundException("잘못된 부모 아이디입니다."));

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
    public void deleteGeneral(int groundId, String generalId) throws Exception{
        General unclassifiedGeneral = generalRepository.findByGroundIdAndUnclassified(groundId, true).orElseThrow(()->new DocumentNotFoundException("미분류 문서를 찾을 수 없습니다."));
        General loadGeneral = generalRepository.findById(generalId).orElseThrow(()->new DocumentNotFoundException("해당 문서를 가지고 오는데 실패했습니다."));
        if(unclassifiedGeneral.getId().equals(loadGeneral.getId())) throw new InvalidAttributeValueException("미분류 문서를 삭제할 수 없습니다.");
        int step = loadGeneral.getStep();
        // step1인 문서가 삭제되었을 때
        if(step == 1){
            // 자식문서들의 parent를 미분류 문서로 바꾼다.
            List<General> children = generalRepository.findByParentId(generalId).orElseThrow(()->new TransactionException("자식문서들을 들고 오는데 실패했습니다."));
            ListIterator<General> iterator = children.listIterator();
            while(iterator.hasNext()){
                General child = iterator.next();
                child.setParentId(unclassifiedGeneral.getId());
            }
            // 미분류 문서에 자식들을 넣는다.
            unclassifiedGeneral.setChildren(children);
            try{
                generalRepository.saveAll(children);
                generalRepository.save(unclassifiedGeneral);
            }catch(Exception e){
                throw new TransactionException("문서들을 저장하는데 실패했습니다.");
            }
        }
        else{
            // 부모문서에서 자식문서를 제거한다.
            String parentId = loadGeneral.getParentId();
            General parent = generalRepository.findById(parentId).orElseThrow(()-> new DocumentNotFoundException("부모 문서를 찾을 수 없습니다."));
            List<General> children = parent.getChildren();
            children.removeIf((child) -> (child.getId().equals(generalId)));
            parent.setChildren(children);
            try{
                generalRepository.save(parent);
            }catch(Exception e) {
                throw new TransactionException("부모를 저장하는데 실패했습니다.");
            }
        }
        // 문서 삭제
        try{
            generalRepository.deleteById(generalId);
        }catch(Exception e){
            throw new TransactionException("문서를 삭제하는데 실패했습니다.");
        }

        documentService.deleteFile(generalId);
    }

    @Override
    public General titleGeneral(int groundId, String generalId, GeneralTitleDto generalTitleDto, UserDetails userDetails) throws Exception{
        if(generalTitleDto.getTitle() == null) throw new InvalidAttributeValueException("제목이 없습니다.");
        General loadGeneral = generalRepository.findById(generalId).orElseThrow(()->new DocumentNotFoundException("해당 문서를 불러오는데 실패했습니다."));
        int step = loadGeneral.getStep();
        loadGeneral.setTitle(generalTitleDto.getTitle());
        try{
            generalRepository.save(loadGeneral);
        }catch(Exception e){
            throw new TransactionException("문서 업데이트를 실패했습니다.");
        }
        // step1 문서가 아니라면 부모를 찾아서 업데이트해줘야한다.
        if(step != 1){
            String parentId = loadGeneral.getParentId();
            General parent = generalRepository.findById(parentId).orElseThrow(()->new DocumentNotFoundException("부모 문서를 불러오는데 실패했습니다."));
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

    public boolean stepIsRange(int step){
        return step>=1 && step<=2;
    }
    public GeneralTreeResponseDto convertToGeneralTreeResponseDto(General general) {
        GeneralTreeResponseDto generalResponseDto = new GeneralTreeResponseDto();
        generalResponseDto.setId(general.getId());
        generalResponseDto.setStep(general.getStep());
        if(general.getTitle() == null){
            generalResponseDto.setTitle("");
        }
        else{
            generalResponseDto.setTitle(general.getTitle());
        }

        List<GeneralTreeResponseDto> children = new ArrayList<>();
        if (general.getChildren() != null) {
            for (General child : general.getChildren()) {
                children.add(convertToGeneralTreeResponseDto(child));
            }
        }
        generalResponseDto.setChildren(children);

        return generalResponseDto;
    }

    public GeneralStepResponseDto convertToGeneralStepResponseDto(General general) {
        GeneralStepResponseDto generalStepResponseDto = new GeneralStepResponseDto();
        generalStepResponseDto.setId(general.getId());

        if(general.getTitle() == null){
            generalStepResponseDto.setTitle("");
        }
        else{
            generalStepResponseDto.setTitle(general.getTitle());
        }

        return generalStepResponseDto;
    }


}
