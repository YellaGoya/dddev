package com.d103.dddev.api.general.service;

import com.d103.dddev.api.general.collection.General1;
import com.d103.dddev.api.general.collection.General2;
import com.d103.dddev.api.general.repository.General1Repository;
import com.d103.dddev.api.general.repository.General2Repository;
import com.d103.dddev.api.general.repository.dto.General1InsertDto;
import com.d103.dddev.api.general.repository.dto.General2InsertDto;
import com.d103.dddev.api.general.repository.dto.General2MoveDto;
import com.d103.dddev.api.general.repository.dto.GeneralUpdateDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.TransactionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class GeneralServiceImpl implements GeneralService{

    private final General1Repository general1Repository;
    private final General2Repository general2Repository;

    @Override
    public General1 insertGeneral1(int groundId) {
        General1 insertGeneral;

        insertGeneral = General1.builder()
                .groundId(groundId)
                .build();

        try{
            general1Repository.save(insertGeneral);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }

        return insertGeneral;
    }

    @Override
    public General2 insertGeneral2(int groundId, String parentId) {
        General1 parent = general1Repository.findById(parentId).orElseThrow(()->new TransactionException("부모 문서를 불러오는데 실패했습니다."));
        General2 insertGeneral;

        insertGeneral = General2.builder()
                .parentId(parent.getId())
                .groundId(groundId)
                .build();

        try{
            general2Repository.save(insertGeneral);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }

        List<General2> childrenList = parent.getChildren();
        if(childrenList == null){
            childrenList = new ArrayList<>();
        }
        childrenList.add(insertGeneral);
        parent.setChildren(childrenList);
        try{
            general1Repository.save(parent);
        }catch(Exception e){
            throw new TransactionException("부모 문서를 저장에 실패했습니다.");
        }

        return insertGeneral;
    }

    @Override
    public General1 insertGeneral1WithTitle(int groundId, General1InsertDto general1InsertDto) {
        General1 insertGeneral;

        insertGeneral = General1.builder()
                .title(general1InsertDto.getTitle())
                .groundId(groundId)
                .build();

        try{
            general1Repository.save(insertGeneral);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }

        return insertGeneral;
    }

    @Override
    public General2 insertGeneral2WithTitle(int groundId, General2InsertDto general2InsertDto) {
        General1 parent = general1Repository.findById(general2InsertDto.getParentId()).orElseThrow(()->new TransactionException("부모 문서를 불러오는데 실패했습니다."));
        General2 insertGeneral;

        insertGeneral = General2.builder()
                .title(general2InsertDto.getTitle())
                .parentId(parent.getId())
                .groundId(groundId)
                .build();

        try{
            general2Repository.save(insertGeneral);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }

        List<General2> childrenList = parent.getChildren();
        if(childrenList == null){
            childrenList = new ArrayList<>();
        }
        childrenList.add(insertGeneral);
        parent.setChildren(childrenList);
        try{
            general1Repository.save(parent);
        }catch(Exception e){
            throw new TransactionException("부모 문서를 저장에 실패했습니다.");
        }

        return insertGeneral;
    }

    @Override
    public List<General1> insertGeneral1WithTitles(int groundId, String[] titles) {
        List<General1> list = new ArrayList<>();
        for(String title : titles){
            General1 insertGeneral = General1.builder()
                        .title(title)
                        .groundId(groundId)
                        .build();
            list.add(insertGeneral);
        }
        try{
            general1Repository.saveAll(list);
        }catch(Exception e){
            throw new TransactionException("문서 저장에 실패했습니다.");
        }
        return list;
    }

    @Override
    public General1 getGeneral1(int groundId, String generalId){
        return general1Repository.findById(generalId).orElseThrow(()->new NoSuchElementException("잘못된 문서 아이디입니다."));
    }

    @Override
    public General2 getGeneral2(int groundId, String generalId){
        return general2Repository.findById(generalId).orElseThrow(()->new NoSuchElementException("잘못된 문서 아이디입니다."));
    }

    @Override
    public General1 updateGeneral1(int groundId, GeneralUpdateDto generalUpdateDto) {
        General1 loadGeneral = general1Repository.findById(generalUpdateDto.getId()).orElseThrow(()->new NoSuchElementException("잘못된 문서 아이디입니다."));
        loadGeneral.setTitle(generalUpdateDto.getTitle());
        loadGeneral.setContent(generalUpdateDto.getTitle());
        try{
            general1Repository.save(loadGeneral);
        }catch(Exception e){
            throw new TransactionException("문서 업데이트를 실패했습니다.");
        }
        return loadGeneral;
    }

    @Override
    public General2 updateGeneral2(int groundId, GeneralUpdateDto generalUpdateDto) {
        General2 loadGeneral = general2Repository.findById(generalUpdateDto.getId()).orElseThrow(()->new NoSuchElementException("잘못된 문서 아이디입니다."));
        loadGeneral.setTitle(generalUpdateDto.getTitle());
        loadGeneral.setContent(generalUpdateDto.getTitle());
        try{
            general2Repository.save(loadGeneral);
        }catch(Exception e){
            throw new TransactionException("문서 업데이트를 실패했습니다.");
        }
        return loadGeneral;
    }

    @Override
    public General2 moveGeneral2(int groundId, General2MoveDto general2MoveDto) {
        General2 loadGeneral = general2Repository.findById(general2MoveDto.getId()).orElseThrow(()->new NoSuchElementException("잘못된 문서 아이디입니다."));
        String originParentId = loadGeneral.getParentId();
        General1 originParent = general1Repository.findById(originParentId).orElseThrow(()->new NoSuchElementException("잘못된 부모 아이디입니다."));
        String newParentId = general2MoveDto.getParentId();
        General1 newParent = general1Repository.findById(newParentId).orElseThrow(()->new NoSuchElementException("잘못된 부모 아이디입니다."));

        // 원래 부모문서에서 자기 지우기
        List<General2> originChildren = originParent.getChildren();
        originChildren.removeIf(child -> (child.getId().equals(loadGeneral.getId())));
        originParent.setChildren(originChildren);

        // 새로운 부모문서에 자기 추가하기
        List<General2> newChildren = newParent.getChildren();
        newChildren.add(loadGeneral);
        newParent.setChildren(newChildren);

        // 자기 부모아이디 수정하기
        loadGeneral.setParentId(newParentId);

        try{
            general1Repository.save(originParent);
            general1Repository.save(newParent);
            general2Repository.save(loadGeneral);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }
        return loadGeneral;
    }

    @Override
    public void deleteGeneral1(int groundId, String generalId) {
        // 자식들의 부모를 삭제한다.
        List<General2> general2List = general2Repository.findByParentId(generalId).orElseThrow(()-> new NoSuchElementException("잘못된 문서아이디입니다."));
        for(General2 general2 : general2List){
            general2.setParentId(null);
        }

        try{
            // 부모를 삭제한다.
            general1Repository.deleteById(generalId);
            general2Repository.saveAll(general2List);
        }catch(Exception e){
            throw new TransactionException("문서 삭제, 저장에 실패했습니다.");
        }
    }

    @Override
    public void deleteGeneral2(int groundId, String generalId) {
        General2 loadGeneral = general2Repository.findById(generalId).orElseThrow(()->new NoSuchElementException("잘못된 문서 아이디입니다."));
        String parentId = loadGeneral.getParentId();
        // parent 불러와서 children 삭제하기
        General1 parent = general1Repository.findById(parentId).orElseThrow(()->new NoSuchElementException("잘못된 부모 아이디입니다."));
        List<General2> children = parent.getChildren();
        children.removeIf((child) -> (child.getParentId().equals(parentId)));
        parent.setChildren(children);
        try{
            general1Repository.save(parent);
            general2Repository.delete(loadGeneral);
        }catch(Exception e){
            throw new TransactionException("문서 삭제, 저장에 실패했습니다.");
        }
    }

}
