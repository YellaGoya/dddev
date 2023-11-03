package com.d103.dddev.api.request.controller;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.request.collection.Request1;
import com.d103.dddev.api.request.collection.Request2;
import com.d103.dddev.api.request.repository.dto.Request1InsertDto;
import com.d103.dddev.api.request.repository.dto.Request2InsertDto;
import com.d103.dddev.api.request.service.RequestServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ground/{groundId}/request")
@RequiredArgsConstructor
@Api(tags = {"요청 문서 API"})
public class RequestController {
    private final RequestServiceImpl requestService;

    @PostMapping("/step1")
    @ApiOperation(value="step 1 요청 문서 생성")
    public ResponseEntity<?> insertRequest1(@PathVariable("groundId") int groundId,
                                            @ApiParam(value="title 없으면 제목없음으로 생성") @RequestBody Request1InsertDto request1InsertDto){
        ResponseVO<Request1> responseVo;

        try{
            Request1 request = requestService.insertRequest1(groundId, request1InsertDto);
            responseVo = ResponseVO.<Request1>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서가 생성되었습니다.")
                    .data(request)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<Request1>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/step2")
    @ApiOperation(value="step 2 요청 문서 생성")
    public ResponseEntity<?> insertRequest1(@PathVariable("groundId") int groundId,
                                            @ApiParam(value="title 없으면 제목없음으로 생성") @RequestBody Request2InsertDto request2InsertDto){
        ResponseVO<Request2> responseVo;

        try{
            Request2 request = requestService.insertRequest2(groundId, request2InsertDto);
            responseVo = ResponseVO.<Request2>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서가 생성되었습니다.")
                    .data(request)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<Request2>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
