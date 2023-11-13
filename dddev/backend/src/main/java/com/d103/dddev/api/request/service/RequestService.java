package com.d103.dddev.api.request.service;

import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.request.repository.dto.requestDto.*;
import com.d103.dddev.api.request.repository.dto.responseDto.CommentResponseDto;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestResponseDto;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestTitleResponseDto;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestTreeResponseDto;
import com.d103.dddev.api.user.repository.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface RequestService {
    RequestResponseDto insertRequest(int groundId, RequestInsertOneDto request1InsertDto, UserDto userDto) throws Exception;
    List<RequestResponseDto> insertRequestsWithTitles(int groundId, RequestInsertManyDto requestInsertManyDto, UserDto userDto);
    RequestResponseDto getRequest(int groundId, String requestId) throws Exception;
    List<RequestTreeResponseDto> getTreeRequests(int groundId);
    List<RequestTitleResponseDto> getStep1Requests(int groundId);
    List<Request> getStep2Requests(int groundId);
    List<Request> getStep2TodoRequests(int groundId) throws Exception;
    List<Request> getStep2ProceedRequests(int groundId) throws Exception;
    List<Request> getStep2DoneRequests(int groundId) throws Exception;
    RequestResponseDto updateRequest(int groundId, String requestId, RequestUpdateDto requestUpdateDto, UserDto userDto) throws Exception;
    void changeStatus(int groundId, String requestId, RequestStatusDto requestStatusDto) throws Exception;
    void changeSender(int groundId, String requestId, RequestSenderDto requestSenderDto) throws Exception;
    void changeReceiver(int groundId, String requestId, RequestReceiverDto requestReceiverDto) throws Exception;
    CommentResponseDto createComment(int groundId, String requestId, RequestCommentDto comment, UserDto user) throws Exception;
    Request moveRequest(int groundId, String requestId, RequestMoveDto requestMoveDto) throws Exception;
    void deleteRequest(int groundId, String requestId) throws Exception;
    Request changeTemplate(int groundId, String requestId) throws Exception;
    RequestResponseDto titleRequest(int groundId, String requestId, RequestTitleDto requestTitleDto, UserDto userDto) throws Exception;
}
