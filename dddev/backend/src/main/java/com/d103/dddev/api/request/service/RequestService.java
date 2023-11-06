package com.d103.dddev.api.request.service;

import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.request.repository.dto.requestDto.*;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.InvalidAttributeValueException;
import java.util.List;

public interface RequestService {
    Request insertRequest(int groundId, RequestInsertOneDto request1InsertDto, UserDetails userDetails) throws InvalidAttributeValueException;
    List<Request> insertRequestsWithTitles(int groundId, RequestInsertManyDto requestInsertManyDto);
    Request getRequest(int groundId, String requestId);
    List<RequestResponseDto> getStep1Requests(int groundId);
    List<Request> getStep2Requests(int groundId);
    Request updateRequest(int groundId, RequestUpdateDto requestUpdateDto, UserDetails userDetails) throws Exception;

    Request moveRequest(int groundId, RequestMoveDto requestMoveDto) throws InvalidAttributeValueException;
    void deleteRequest(int groundId, RequestDeleteDto requestDeleteDto);

}
