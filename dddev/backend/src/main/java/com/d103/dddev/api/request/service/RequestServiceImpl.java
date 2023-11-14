package com.d103.dddev.api.request.service;

import com.d103.dddev.api.file.service.DocumentServiceImpl;
import com.d103.dddev.api.request.collection.Comment;
import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.request.repository.CommentRepository;
import com.d103.dddev.api.request.repository.RequestRepository;
import com.d103.dddev.api.request.repository.dto.requestDto.*;
import com.d103.dddev.api.request.repository.dto.responseDto.*;
import com.d103.dddev.api.user.repository.UserRepository;
import com.d103.dddev.api.user.repository.dto.UserDto;
import com.d103.dddev.api.user.repository.entity.User;
import com.d103.dddev.common.exception.document.DocumentNotFoundException;
import com.d103.dddev.common.exception.user.UserNotFoundException;
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
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final DocumentServiceImpl documentService;

    @Override
    public RequestResponseDto insertRequest(int groundId, RequestInsertOneDto requestInsertOneDto, UserDto userDto) throws Exception{
        Request insertRequest = new Request(); // DB에 저장될 문서
        Request parent; // 저장될 문서의 부모

        insertRequest.setGroundId(groundId);
        if(requestInsertOneDto.getTitle() == null)
            insertRequest.setTitle("");
        else{
            insertRequest.setTitle(requestInsertOneDto.getTitle());
        }
        insertRequest.setAuthor(userDto);
        insertRequest.setModifier(userDto);

        if(requestInsertOneDto.getParentId() == null){
            insertRequest.setStep(1);
            try{
                requestRepository.save(insertRequest);
            }catch(Exception e){
                throw new TransactionException("문서 저장에 실패했습니다.");
            }
        }
        else{
            insertRequest.setStep(2);
            insertRequest.setParentId(requestInsertOneDto.getParentId());

            parent = requestRepository.findById(requestInsertOneDto.getParentId()).orElseThrow(()->new DocumentNotFoundException("부모 문서를 찾을 수 없습니다."));
            // 부모의 템플릿 값이 true라면 content를 복사한다.
            if(parent.isTemplate()){
                insertRequest.setContent(parent.getContent());
            }

            try{
                requestRepository.save(insertRequest);
            }catch(Exception e){
                throw new TransactionException("문서 저장에 실패했습니다.");
            }

            // 부모의 자식에 insertRequest를 넣는다.
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

        return convertToRequestResponseDto(insertRequest);
    }

    @Override
    public List<RequestResponseDto> insertRequestsWithTitles(int groundId, RequestInsertManyDto requestInsertManyDto, UserDto userDto) {
        List<Request> list = new ArrayList<>();
        List<RequestResponseDto> requestResponseDtoList = new ArrayList<>();
        for(String title : requestInsertManyDto.getTitles()){
            Request insertRequest = Request.builder()
                    .groundId(groundId)
                    .step(1)
                    .title(title)
                    .author(userDto)
                    .modifier(userDto)
                    .build();
            list.add(insertRequest);
        }
        try{
            requestRepository.saveAll(list);
        }catch(Exception e){
            throw new TransactionException("문서 저장에 실패했습니다.");
        }
        for(Request request : list){
            RequestResponseDto requestResponseDto = convertToRequestResponseDto(request);
            requestResponseDtoList.add(requestResponseDto);
        }
        return requestResponseDtoList;
    }

    @Override
    public RequestResponseDto getRequest(int groundId, String requestId) throws Exception{
        return convertToRequestResponseDto(requestRepository.findById(requestId).orElseThrow(()-> new DocumentNotFoundException("해당 문서가 존재하지 않습니다.")));
    }
    @Override
    public List<RequestTreeResponseDto> getTreeRequests(int groundId) {
        List<Request> requestList = requestRepository.findByGroundIdAndStep(groundId, 1).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
//      requestList.sort((r1, r2) -> Boolean.compare(r2.isUnclassified(), r1.isUnclassified()));
        List<RequestTreeResponseDto> requestResponseDtoList = new ArrayList<>();
        for (Request request : requestList) {
            RequestTreeResponseDto requestResponseDto = convertToRequestTreeResponseDto(request);
            requestResponseDtoList.add(requestResponseDto);
        }
        return requestResponseDtoList;
    }

    @Override
    public List<RequestTitleResponseDto> getStep1Requests(int groundId) {
        List<Request> requestList = requestRepository.findByGroundIdAndStep(groundId, 1).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
        List<RequestTitleResponseDto> requestResponseDtoList = new ArrayList<>();
        for (Request request : requestList) {
            RequestTitleResponseDto requestResponseDto = convertToRequestStepResponseDto(request);
            requestResponseDtoList.add(requestResponseDto);
        }
        return requestResponseDtoList;
    }

    @Override
    public List<RequestResponseDto> getStep2Requests(int groundId) {
        List<Request> requestList = requestRepository.findByGroundIdAndStep(groundId,2).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
        List<RequestResponseDto> requestResponseDtoList = new ArrayList<>();
        for(Request request : requestList){
            RequestResponseDto requestResponseDto = convertToRequestResponseDto(request);
            requestResponseDtoList.add(requestResponseDto);
        }
        return requestResponseDtoList;
    }

    @Override
    public List<RequestResponseDto> getStep2TodoRequests(int groundId) throws Exception {
        List<Request> requestList = requestRepository.findByGroundIdAndStepAndStatus(groundId, 2, 0).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
        List<RequestResponseDto> requestResponseDtoList = new ArrayList<>();
        for(Request request : requestList){
            RequestResponseDto requestResponseDto = convertToRequestResponseDto(request);
            requestResponseDtoList.add(requestResponseDto);
        }
        return requestResponseDtoList;
    }

    @Override
    public List<RequestResponseDto> getStep2ProceedRequests(int groundId) throws Exception {
        List<Request> requestList = requestRepository.findByGroundIdAndStepAndStatus(groundId, 2, 1).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
        List<RequestResponseDto> requestResponseDtoList = new ArrayList<>();
        for(Request request : requestList){
            RequestResponseDto requestResponseDto = convertToRequestResponseDto(request);
            requestResponseDtoList.add(requestResponseDto);
        }
        return requestResponseDtoList;
    }

    @Override
    public List<RequestResponseDto> getStep2DoneRequests(int groundId) throws Exception {
        List<Request> requestList = requestRepository.findByGroundIdAndStepAndStatus(groundId, 2, 2).orElseThrow(()->new TransactionException("문서들을 불러오는데 실패했습니다."));
        List<RequestResponseDto> requestResponseDtoList = new ArrayList<>();
        for(Request request : requestList){
            RequestResponseDto requestResponseDto = convertToRequestResponseDto(request);
            requestResponseDtoList.add(requestResponseDto);
        }
        return requestResponseDtoList;
    }

    @Override
    public RequestResponseDto updateRequest(int groundId, String requestId, RequestUpdateDto requestUpdateDto, UserDto userDto) throws Exception{
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()->new DocumentNotFoundException("해당 문서를 불러오는데 실패했습니다."));
        // 이미 진행중인 요청이거나 완료된 요청이라면 수정할 수 없다.
        if(loadRequest.getStatus() == 1 || loadRequest.getStatus() == 2){
            throw new InvalidAttributeValueException("수정할 수 없습니다.");
        }
        String title = requestUpdateDto.getTitle();
        String content = requestUpdateDto.getContent();
        if(title==null && content==null) return convertToRequestResponseDto(loadRequest);
        if(title != null){
            loadRequest.setTitle(title);
        }
        if(content != null){
            loadRequest.setContent(content);
        }

        int step = loadRequest.getStep();

        loadRequest.setUpdatedAt(LocalDateTime.now());
        loadRequest.setModifier(userDto);

        try{
            requestRepository.save(loadRequest);
        }catch(Exception e){
            throw new TransactionException("문서 업데이트를 실패했습니다.");
        }
        // step1 문서가 아니라면 부모를 찾아서 업데이트해줘야한다.
        if(step != 1){
            String parentId = loadRequest.getParentId();
            Request parent = requestRepository.findById(parentId).orElseThrow(()->new DocumentNotFoundException("부모 문서를 불러오는데 실패했습니다."));
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

        return convertToRequestResponseDto(loadRequest);
    }

    @Override
    public RequestResponseDto moveRequest(int groundId, String requestId, RequestMoveDto requestMoveDto) throws Exception {
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()->new DocumentNotFoundException("잘못된 문서 아이디입니다."));
        if(loadRequest.getStep() == 1){
            throw new InvalidAttributeValueException("움직일 수 없는 문서입니다.");
        }
        String originParentId = loadRequest.getParentId();
        Request originParent = requestRepository.findById(originParentId).orElseThrow(()->new DocumentNotFoundException("잘못된 부모 아이디입니다."));
        String newParentId = requestMoveDto.getParentId();
        Request newParent = requestRepository.findById(newParentId).orElseThrow(()->new DocumentNotFoundException("잘못된 부모 아이디입니다."));

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
        return convertToRequestResponseDto(loadRequest);
    }

    @Override
    public void changeStatus(int groundId, String requestId, RequestStatusDto requestStatusDto) throws Exception{
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()->new DocumentNotFoundException("잘못된 문서 아이디입니다."));
        loadRequest.setStatus(requestStatusDto.getStatus());

        try{
            requestRepository.save(loadRequest);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했스빈다.");
        }
    }

    @Override
    public void changeSender(int groundId, String requestId, RequestSenderDto requestSenderDto) throws Exception{
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()->new DocumentNotFoundException("잘못된 문서 아이디입니다."));
        User sendUser = userRepository.findByGithubId(requestSenderDto.getSendUserId()).orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));
        loadRequest.setSendUser(sendUser.convertToDto());

        try{
            requestRepository.save(loadRequest);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했스빈다.");
        }
    }

    @Override
    public void changeReceiver(int groundId, String requestId, RequestReceiverDto requestReceiverDto) throws Exception{
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()->new DocumentNotFoundException("잘못된 문서 아이디입니다."));
        User receiveUser = userRepository.findByGithubId(requestReceiverDto.getReceiveUserId()).orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));
        loadRequest.setReceiveUser(receiveUser.convertToDto());

        try{
            requestRepository.save(loadRequest);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했스빈다.");
        }
    }

    @Override
    public CommentResponseDto createComment(int groundId, String requestId, RequestCommentDto comment, UserDto user) throws Exception{
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()-> new DocumentNotFoundException("요청 문서를 찾을 수 없습니다."));

        Comment saveComment = new Comment();
        saveComment.setAuthor(user);
        saveComment.setComment(comment.getComment());
        saveComment.setCreatedTime(LocalDateTime.now());
        saveComment.setRequestId(loadRequest.getId());

        try{
            commentRepository.save(saveComment);
        }catch(Exception e){
            throw new TransactionException("댓글을 저장하는데 실패했습니다.");
        }

        List<String> commentIdList = loadRequest.getCommentIdList();
        if(commentIdList == null){
            commentIdList = new ArrayList<>();
        }
        commentIdList.add(saveComment.getCommentId());
        loadRequest.setCommentIdList(commentIdList);

        try{
            requestRepository.save(loadRequest);
        }catch(Exception e){
            throw new TransactionException("문서를 저장하는데 실패했습니다.");
        }
        return convertToCommentResponseDto(saveComment);
    }

    @Override
    public void deleteRequest(int groundId, String requestId) throws Exception{
        Request unclassifiedRequest = requestRepository.findByGroundIdAndUnclassified(groundId, true).orElseThrow(()->new DocumentNotFoundException("미분류 문서를 찾을 수 없습니다."));
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()->new TransactionException("해당 문서를 가지고 오는데 실패했습니다."));
        if(unclassifiedRequest.getId().equals(loadRequest.getId())) throw new InvalidAttributeValueException("미분류 문서를 삭제할 수 없습니다.");
        int step = loadRequest.getStep();
        // step1인 문서가 삭제되었을 때
        if(step == 1){
            // 자식문서들의 parent를 미분류 문서로 바꾼다.
            List<Request> children = requestRepository.findByParentId(requestId).orElseThrow(()->new TransactionException("자식문서들을 들고 오는데 실패했습니다."));
            ListIterator<Request> iterator = children.listIterator();
            while(iterator.hasNext()){
                Request child = iterator.next();
                child.setParentId(unclassifiedRequest.getId());
            }
            // 미분류 문서에 자식들을 넣는다.
            unclassifiedRequest.setChildren(children);
            try{
                requestRepository.saveAll(children);
                requestRepository.save(unclassifiedRequest);
            }catch(Exception e){
                throw new TransactionException("문서들을 저장하는데 실패했습니다.");
            }
        }
        else{
            // 부모문서에서 자식문서를 제거한다.
            String parentId = loadRequest.getParentId();
            Request parent = requestRepository.findById(parentId).orElseThrow(()-> new DocumentNotFoundException("부모 문서를 찾을 수 없습니다."));
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

        documentService.deleteFile(requestId);

    }

    @Override
    public void deleteAllRequestWhenGroundDelete(int groundId) throws Exception {
        try{
            requestRepository.deleteByGroundId(groundId);
        }catch(Exception e){
            throw new TransactionException("요청 문서들을 삭제하는데 실패했습니다.");
        }
    }

    @Override
    public RequestResponseDto changeTemplate(int groundId, String requestId) throws Exception {
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException("요청 문서를 찾을 수 없습니다."));
        // isTemplate 값을 true면은 false로 false였다면 true로 바꾼다.
        if(loadRequest.isTemplate()){
            loadRequest.setTemplate(false);
        }
        else{
            loadRequest.setTemplate(true);
        }
        try{
            requestRepository.save(loadRequest);
        }catch(Exception e){
            throw new TransactionException("요청 문서 저장에 실패했습니다.");
        }
        return convertToRequestResponseDto(loadRequest);
    }

    @Override
    public RequestResponseDto titleRequest(int groundId, String requestId, RequestTitleDto requestTitleDto, UserDto userDto) throws Exception{
        if(requestTitleDto.getTitle() == null) throw new InvalidAttributeValueException("제목이 없습니다.");
        Request loadRequest = requestRepository.findById(requestId).orElseThrow(()->new DocumentNotFoundException("해당 문서를 불러오는데 실패했습니다."));
        // 이미 진행중인 요청이거나 완료된 요청이라면 수정할 수 없다.
        if(loadRequest.getStatus() == 1 || loadRequest.getStatus() == 2){
            throw new InvalidAttributeValueException("수정할 수 없습니다.");
        }
        int step = loadRequest.getStep();
        loadRequest.setTitle(requestTitleDto.getTitle());
        try{
            requestRepository.save(loadRequest);
        }catch(Exception e){
            throw new TransactionException("문서 업데이트를 실패했습니다.");
        }
        // step1 문서가 아니라면 부모를 찾아서 업데이트해줘야한다.
        if(step != 1){
            String parentId = loadRequest.getParentId();
            Request parent = requestRepository.findById(parentId).orElseThrow(()->new DocumentNotFoundException("부모 문서를 불러오는데 실패했습니다."));
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

        return convertToRequestResponseDto(loadRequest);
    }

    public boolean stepIsRange(int step){
        return step>=1 && step<=2;
    }
    public RequestTreeResponseDto convertToRequestTreeResponseDto(Request request){
        RequestTreeResponseDto requestResponseDto = new RequestTreeResponseDto();
        requestResponseDto.setId(request.getId());
        requestResponseDto.setStep(request.getStep());
        if(request.getTitle() == null){
            requestResponseDto.setTitle("");
        }
        else{
            requestResponseDto.setTitle(request.getTitle());
        }
        List<RequestTreeResponseDto> children = new ArrayList<>();
        if(request.getChildren() != null){
            for(Request child : request.getChildren()){
                children.add(convertToRequestTreeResponseDto(child));
            }
        }
        requestResponseDto.setChildren(children);
        return requestResponseDto;
    }

    public RequestTitleResponseDto convertToRequestStepResponseDto(Request request) {
        RequestTitleResponseDto requestStepResponseDto = new RequestTitleResponseDto();
        requestStepResponseDto.setId(request.getId());

        if(request.getTitle() == null){
            requestStepResponseDto.setTitle("");
        }
        else{
            requestStepResponseDto.setTitle(request.getTitle());
        }

        return requestStepResponseDto;
    }

    public CommentResponseDto convertToCommentResponseDto(Comment comment){
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getCommentId());
        commentResponseDto.setComment(comment.getComment());
        commentResponseDto.setAuthor(comment.getAuthor().getNickname());
        if(comment.getAuthor().getProfileDto() != null){
            commentResponseDto.setFileName(comment.getAuthor().getProfileDto().getFileName());
        }
        commentResponseDto.setCreatedTime(comment.getCreatedTime());
        return commentResponseDto;
    }

    public RequestResponseDto convertToRequestResponseDto(Request request){
        RequestResponseDto requestResponseDto = new RequestResponseDto();
        requestResponseDto.setId(request.getId());
        requestResponseDto.setGroundId(request.getGroundId());
        requestResponseDto.setParentId(request.getParentId());
        requestResponseDto.setChildren(request.getChildren());
        requestResponseDto.setTitle(request.getTitle());
        requestResponseDto.setContent(request.getContent());
        requestResponseDto.setStep(request.getStep());
        requestResponseDto.setCreatedAt(request.getCreatedAt());
        requestResponseDto.setUpdatedAt(request.getUpdatedAt());
        requestResponseDto.setSendUser(convertToRequestUserResponseDto(request.getSendUser()));
        requestResponseDto.setReceiveUser(convertToRequestUserResponseDto(request.getReceiveUser()));
        requestResponseDto.setAuthor(convertToRequestUserResponseDto(request.getAuthor()));
        requestResponseDto.setModifier(convertToRequestUserResponseDto(request.getModifier()));
        requestResponseDto.setStatus(request.getStatus());
        requestResponseDto.setUnclassified(request.isUnclassified());
        requestResponseDto.setTemplate(request.isTemplate());
        List<Comment> commentList = commentRepository.findByRequestId(request.getId()).orElseThrow(() -> new TransactionException("댓글을 불러오는데 실패했습니다."));
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            CommentResponseDto commentResponseDto = convertToCommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }
        requestResponseDto.setComments(commentResponseDtoList);
        return requestResponseDto;
    }

    public RequestUserResponseDto convertToRequestUserResponseDto(UserDto userDto){
        if(userDto == null) return null;
        RequestUserResponseDto requestUserResponseDto = new RequestUserResponseDto();
        requestUserResponseDto.setId(userDto.getId());
        requestUserResponseDto.setGithubId(userDto.getGithubId());
        requestUserResponseDto.setNickname(userDto.getNickname());
        requestUserResponseDto.setEmail(userDto.getEmail());
        requestUserResponseDto.setStatusMsg(userDto.getStatusMsg());
        if(userDto.getProfileDto() != null){
            requestUserResponseDto.setFileName(userDto.getProfileDto().getFileName());
        }
        return requestUserResponseDto;
    }

}
