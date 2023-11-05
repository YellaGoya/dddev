package com.d103.dddev.api.request.service;

import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.request.repository.dto.*;

import javax.management.InvalidAttributeValueException;
import java.util.List;

public interface RequestService {
    Request insertRequest(int groundId, RequestInsertOneDto request1InsertDto) throws InvalidAttributeValueException;
    List<Request> insertRequestsWithTitles(int groundId, RequestInsertManyDto requestInsertManyDto);
    Request getRequest(int groundId, String requestId);
    List<Request> getStep1Requests(int groundId);
    List<Request> getStep2Requests(int groundId);
    Request updateRequest(int groundId, RequestUpdateDto requestUpdateDto) throws Exception;

    Request moveRequest(int groundId, RequestMoveDto requestMoveDto) throws InvalidAttributeValueException;
    void deleteRequest(int groundId, RequestDeleteDto requestDeleteDto);

}
