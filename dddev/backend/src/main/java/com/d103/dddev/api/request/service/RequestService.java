package com.d103.dddev.api.request.service;

import com.d103.dddev.api.request.collection.Request1;
import com.d103.dddev.api.request.collection.Request2;
import com.d103.dddev.api.request.repository.dto.Request1InsertDto;
import com.d103.dddev.api.request.repository.dto.Request2InsertDto;

public interface RequestService {
    Request1 insertRequest1(int groundId, Request1InsertDto request1InsertDto);
    Request2 insertRequest2(int groundId, Request2InsertDto request2InsertDto);
}
