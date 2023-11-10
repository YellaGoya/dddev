package com.d103.dddev.api.request.service;

import com.d103.dddev.api.request.collection.Comment;
import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.request.repository.dto.requestDto.*;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.InvalidAttributeValueException;
import java.util.List;

public interface RequestService {
    Request insertRequest(int groundId, RequestInsertOneDto request1InsertDto, UserDetails userDetails) throws Exception;
    List<Request> insertRequestsWithTitles(int groundId, RequestInsertManyDto requestInsertManyDto, UserDetails userDetails);
    Request getRequest(int groundId, String requestId) throws Exception;
    List<RequestResponseDto> getStep1Requests(int groundId);
    List<Request> getStep2Requests(int groundId);
    Request updateRequest(int groundId, String requestId, RequestUpdateDto requestUpdateDto, UserDetails userDetails) throws Exception;
    void sendRequest(int groundId, String requestId, RequestUpdateDto requestUpdateDto, UserDetails userDetails) throws Exception;
    Comment createComment(int groundId, String requestId, String comment, UserDetails userDetails) throws Exception;
    Request moveRequest(int groundId, String requestId, RequestMoveDto requestMoveDto) throws Exception;
    void deleteRequest(int groundId, String requestId) throws Exception;

    Request titleRequest(int groundId, String requestId, RequestTitleDto requestTitleDto, UserDetails userDetails) throws Exception;
}
