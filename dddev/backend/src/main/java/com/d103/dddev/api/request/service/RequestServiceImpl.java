package com.d103.dddev.api.request.service;

import com.d103.dddev.api.general.collection.General1;
import com.d103.dddev.api.general.collection.General2;
import com.d103.dddev.api.general.repository.dto.General2InsertDto;
import com.d103.dddev.api.request.collection.Request1;
import com.d103.dddev.api.request.collection.Request2;
import com.d103.dddev.api.request.repository.Request1Repository;
import com.d103.dddev.api.request.repository.Request2Repository;
import com.d103.dddev.api.request.repository.dto.Request1InsertDto;
import com.d103.dddev.api.request.repository.dto.Request2InsertDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.hibernate.TransactionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{

    private final Request1Repository request1Repository;
    private final Request2Repository request2Repository;

    @Override
    public Request1 insertRequest1(int groundId, Request1InsertDto request1InsertDto) {
        Request1 insertRequest;
        // 제목없이 값이 들어왔을 경우
        if(request1InsertDto.getTitle() == null){
            insertRequest = Request1.builder()
                    .title("제목 없음")
                    .groundId(groundId)
                    .build();
        }
        else{
            insertRequest = Request1.builder()
                    .title(request1InsertDto.getTitle())
                    .groundId(groundId)
                    .build();
        }
        try{
            request1Repository.save(insertRequest);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }

        return insertRequest;
    }

    @Override
    public Request2 insertRequest2(int groundId, Request2InsertDto request2InsertDto) {
        String parentId = request2InsertDto.getParentId();
        Request1 parent = request1Repository.findById(parentId).orElseThrow(()->new TransactionException("부모 문서를 불러오는데 실패했습니다."));
        Request2 insertRequest;
        // 제목없이 값이 들어왔을 경우
        if(request2InsertDto.getTitle() == null){
            insertRequest = Request2.builder()
                    .title("제목 없음")
                    .parentId(parentId)
                    .groundId(groundId)
                    .build();
        }
        else{
            insertRequest = Request2.builder()
                    .title(request2InsertDto.getTitle())
                    .parentId(parent.getId())
                    .groundId(groundId)
                    .build();
        }
        try{
            request2Repository.save(insertRequest);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }

        List<Request2> childrenList = parent.getChildren();
        if(childrenList == null){
            childrenList = new ArrayList<>();
        }
        childrenList.add(insertRequest);
        parent.setChildren(childrenList);
        try{
            request1Repository.save(parent);
        }catch(Exception e){
            throw new TransactionException("부모 문서를 저장에 실패했습니다.");
        }

        return insertRequest;
    }
}
