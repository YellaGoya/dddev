package com.d103.dddev.api.request.service;

import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralResponseDto;
import com.d103.dddev.api.request.collection.Comment;
import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.request.repository.RequestRepository;
import com.d103.dddev.api.request.repository.dto.requestDto.*;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestResponseDto;
import com.d103.dddev.api.user.repository.UserRepository;
import com.d103.dddev.api.user.repository.dto.UserDto;
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
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService{

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public Request insertRequest(int groundId, RequestInsertOneDto requestInsertOneDto, UserDetails userDetails) throws InvalidAttributeValueException{
        int step = requestInsertOneDto.getStep(); // 문서의 step
        Request insertRequest = new Request(); // DB에 저장될 문서
        Request parent; // 저장될 문서의 부모

        if(!stepIsRange(step)) throw new InvalidAttributeValueException("잘못된 step입니다.");
        insertRequest.setGroundId(groundId);
        insertRequest.setStep(step);
        if(requestInsertOneDto.getTitle() == null)
            insertRequest.setTitle("");
        else{
            insertRequest.setTitle(requestInsertOneDto.getTitle());
        }
        insertRequest.setAuthor(userDetails.getUsername());
        insertRequest.setModifier(userDetails.getUsername());

        // step1의 문서는 부모가 필요가 없다.
        if(step == 1){
            // 저장할 문서 생성
            try{
                requestRepository.save(insertRequest);
            }catch(Exception e){
                throw new TransactionException("문서 저장에 실패했습니다.");
            }
        }
        // step1이 아닌 문서들
        else{
            // 미분류 문서를 부모로 설정
            if(requestInsertOneDto.getParentId() == null){
                Request unclassified = requestRepository.findByGroundIdAndUnclassified(groundId, true).orElseThrow(()-> new NoSuchElementException("미분류 문서를 찾을 수 없습니다."));
                insertRequest.setParentId(unclassified.getId());
            }
            else{
                insertRequest.setParentId(requestInsertOneDto.getParentId());
            }
            try{
                requestRepository.save(insertRequest);
            }catch(Exception e){
                throw new TransactionException("문서 저장에 실패했습니다.");
            }

            parent = requestRepository.findById(requestInsertOneDto.getParentId()).orElseThrow(()->new NoSuchElementException("부모 문서를 찾을 수 없습니다."));
            List<Request> children = parent.getChildren();
            if(children == null){
                children = new ArrayList<>();
            }
            children.add(insertRequest);
            parent.setChildren(children);
            // 부모 문서의 자식을 업데이트한다.
            try{
                requestRepository.save(parent);
            }catch(Exception e){
                throw new TransactionException("부모 문서를 저장하는데 실패했습니다.");
            }
        }

        return insertRequest;
    }

    @Override
    public List<Request> insertRequestsWithTitles(int groundId, RequestInsertManyDto requestInsertManyDto, UserDetails userDetails) {
        List<Request> list = new ArrayList<>();
        for(String title : requestInsertManyDto.getTitles()){
            Request insertRequest = Request.builder()
                    .groundId(groundId)
                    .step(1)
                    .title(title)
                    .author(userDetails.getUsername())
                    .modifier(userDetails.getUsername())
                    .build();
            list.add(insertRequest);
        }
        try{
            requestRepository.saveAll(list);
        }catch(Exception e){
            throw new TransactionException("문서 저장에 실패했습니다.");
        }
        return list;
    }

    @Override
    public Request getRequest(int groundId, String requestId) {
        return requestRepository.findById(requestId).orElseThrow(()-> new NoSuchElementException("해당 문서가 존재하지 않습니다."));
    }

    @Override
    public List<RequestResponseDto> getStep1Requests(int groundId) {
        List<Request> requestList = requestRepository.findByGroundIdAndStep(groundId, 1).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
//      requestList.sort((r1, r2) -> Boolean.compare(r2.isUnclassified(), r1.isUnclassified()));
        List<RequestResponseDto> requestResponseDtoList = new ArrayList<>();
        for (Request request : requestList) {
            RequestResponseDto requestResponseDto = convertToDto(request);
            requestResponseDtoList.add(requestResponseDto);
        }
        return requestResponseDtoList;
    }

    @Override
    public List<Request> getStep2Requests(int groundId) {
        return requestRepository.findByGroundIdAndStep(groundId,2).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
    }

    @Override
    public Request updateRequest(int groundId, String requestId, RequestUpdateDto requestUpdateDto, UserDetails userDetails) throws Exception{
        int sendUserId = requestUpdateDto.getSendUserId();
        int receiveUserId = requestUpdateDto.getReceiveUserId();
        UserDto sendUser = userRepository.findById(sendUserId).orElseThrow(()->new NoSuchElementException("유저를 불러오는데 실패했습니다."));
        UserDto receiveUser = userRepository.findById(receiveUserId).orElseThrow(()->new NoSuchElementException("유저를 불러오는데 실패했습니다."));
        if(sendUser == null){
            throw new InvalidAttributeValueException("잘못된 보내는 유저 아이디입니다.");
        }
        if(receiveUser == null){
            throw new InvalidAttributeValueException("잘못된 받는 유저 아이디입니다.");
        }
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()->new NoSuchElementException("해당 문서를 불러오는데 실패했습니다."));
        // 이미 보낸 요청이라면 수정할 수 없다.
        if(loadRequest.getStatus() == 1){
            throw new InvalidAttributeValueException("수정할 수 없습니다.");
        }
        int step = loadRequest.getStep();
        loadRequest.setTitle(requestUpdateDto.getTitle());
        loadRequest.setContent(requestUpdateDto.getContent());
        loadRequest.setUpdatedAt(LocalDateTime.now());
        loadRequest.setSendUser(sendUser);
        loadRequest.setReceiveUser(receiveUser);
        loadRequest.setModifier(userDetails.getUsername());
        try{
            requestRepository.save(loadRequest);
        }catch(Exception e){
            throw new TransactionException("문서 업데이트를 실패했습니다.");
        }
        // step1 문서가 아니라면 부모를 찾아서 업데이트해줘야한다.
        if(step != 1){
            String parentId = loadRequest.getParentId();
            Request parent = requestRepository.findById(parentId).orElseThrow(()->new NoSuchElementException("부모 문서를 불러오는데 실패했습니다."));
            List<Request> children = parent.getChildren();
            ListIterator<Request> iterator = children.listIterator();
            while (iterator.hasNext()) {
                Request child = iterator.next();
                if (child.getId().equals(loadRequest.getId())) {
                    iterator.set(loadRequest);
                }
            }
            parent.setChildren(children);
            try{
                requestRepository.save(parent);
            }catch(Exception e){
                throw new TransactionException("부모 문서를 저장하는데 실패했습니다.");
            }
        }

        return loadRequest;
    }

    @Override
    public Request moveRequest(int groundId, String requestId, RequestMoveDto requestMoveDto) throws InvalidAttributeValueException {
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()->new NoSuchElementException("잘못된 문서 아이디입니다."));
        if(loadRequest.getStep() == 1){
            throw new InvalidAttributeValueException("움직일 수 없는 문서입니다.");
        }
        String originParentId = loadRequest.getParentId();
        Request originParent = requestRepository.findById(originParentId).orElseThrow(()->new NoSuchElementException("잘못된 부모 아이디입니다."));
        String newParentId = requestMoveDto.getParentId();
        Request newParent = requestRepository.findById(newParentId).orElseThrow(()->new NoSuchElementException("잘못된 부모 아이디입니다."));

        // 원래 부모문서에서 자기 지우기
        List<Request> originChildren = originParent.getChildren();
        originChildren.removeIf(child -> (child.getId().equals(loadRequest.getId())));
        originParent.setChildren(originChildren);

        // 새로운 부모문서에 자기 추가하기
        List<Request> newChildren = newParent.getChildren();
        newChildren.add(loadRequest);
        newParent.setChildren(newChildren);

        // 자기 부모아이디 수정하기
        loadRequest.setParentId(newParentId);

        try{
            requestRepository.save(originParent);
            requestRepository.save(newParent);
            requestRepository.save(loadRequest);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }
        return loadRequest;
    }

    @Override
    public void sendRequest(int groundId, String requestId, RequestUpdateDto requestUpdateDto, UserDetails userDetails) throws Exception{
        try{
            Request request = updateRequest(groundId, requestId, requestUpdateDto, userDetails);
            if(!userDetails.getUsername().equals(request.getAuthor()))
                throw new InvalidAttributeValueException("작성자와 보내는 사람이 다릅니다.");
            request.setStatus(1);
            try{
                requestRepository.save(request);
            }catch(Exception e){
                throw new TransactionException("문서를 저장하는데 실패했스빈다.");
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Comment createComment(int groundId, String requestId, String comment, UserDetails userDetails) {
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()-> new NoSuchElementException("요청 문서를 찾을 수 없습니다."));
        List<Comment> comments = loadRequest.getComments();
        if(comments == null){
            comments = new ArrayList<>();
        }
        Comment saveComment = new Comment();
        saveComment.setAuthor(userDetails.getUsername());
        saveComment.setComment(comment);
        comments.add(saveComment);
        loadRequest.setComments(comments);

        try{
            requestRepository.save(loadRequest);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }
        return saveComment;
    }

    @Override
    public void deleteRequest(int groundId, String requestId) throws InvalidAttributeValueException{
        Request unclassifiedRequest = requestRepository.findByGroundIdAndUnclassified(groundId, true).orElseThrow(()->new NoSuchElementException("미분류 문서를 찾을 수 없습니다."));
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()->new TransactionException("해당 문서를 가지고 오는데 실패했습니다."));
        if(unclassifiedRequest.getId().equals(loadRequest.getId())) throw new InvalidAttributeValueException("미분류 문서를 삭제할 수 없습니다.");
        int step = loadRequest.getStep();
        // step1인 문서가 삭제되었을 때
        if(step == 1){
            List<Request> children = requestRepository.findByParentId(requestId).orElseThrow(()->new TransactionException("자식문서들을 들고 오는데 실패했습니다."));
            ListIterator<Request> iterator = children.listIterator();
            while(iterator.hasNext()){
                Request child = iterator.next();
                child.setParentId(unclassifiedRequest.getId());
            }
            try{
                requestRepository.saveAll(children);
            }catch(Exception e){
                throw new TransactionException("문서들을 저장하는데 실패했습니다.");
            }
        }
        else{
            // 부모를 업데이트한다.
            String parentId = loadRequest.getParentId();
            Request parent = requestRepository.findById(loadRequest.getParentId()).orElseThrow(()-> new TransactionException("문서를 불러오는데 실패했습니다."));
            List<Request> children = parent.getChildren();
            children.removeIf((child) -> (child.getId().equals(requestId)));
            parent.setChildren(children);
            try{
                requestRepository.save(parent);
            }catch(Exception e){
                throw new TransactionException("부모를 저장하는데 실패했습니다.");
            }
        }
        // 문서 삭제
        try{
            requestRepository.deleteById(requestId);
        }catch(Exception e){
            throw new TransactionException("문서를 삭제하는데 실패했습니다.");
        }
    }
    public boolean stepIsRange(int step){
        return step>=1 && step<=2;
    }
    public RequestResponseDto convertToDto(Request request){
        RequestResponseDto requestResponseDto = new RequestResponseDto();
        requestResponseDto.setId(request.getId());
        requestResponseDto.setStep(request.getStep());
        if(request.getTitle() == null){
            requestResponseDto.setTitle("");
        }
        else{
            requestResponseDto.setTitle(request.getTitle());
        }
        List<RequestResponseDto> children = new ArrayList<>();
        if(request.getChildren() != null){
            for(Request child : request.getChildren()){
                children.add(convertToDto(child));
            }
        }
        requestResponseDto.setChildren(children);
        return requestResponseDto;
    }
}
