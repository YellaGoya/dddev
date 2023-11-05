package com.d103.dddev.api.request.controller;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.request.repository.dto.*;
import com.d103.dddev.api.request.service.RequestServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ground/{groundId}/request")
@RequiredArgsConstructor
@Api(tags = {"요청 문서 API"})
public class RequestController {
    private final RequestServiceImpl requestService;

    @PostMapping
    @ApiOperation(value="요청 문서 생성")
    public ResponseEntity<?> insertRequest(@PathVariable("groundId") int groundId,
                                           @ApiParam(value = "step -> required\n"+
                                                   "step값이 1일때는 parentId 필요없음\n" +
                                                   "title -> not required")@RequestBody RequestInsertOneDto requestInsertOneDto){
        ResponseVO<Request> responseVo;

        try{
            Request request = requestService.insertRequest(groundId, requestInsertOneDto);
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서가 생성되었습니다.")
                    .data(request)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/titles")
    @ApiOperation(value="제목들로 step1 요청 문서들 생성")
    public ResponseEntity<?> insertRequestsWithTitles(@PathVariable("groundId") int groundId,
                                                      @RequestBody RequestInsertManyDto requestInsertManyDto){
        ResponseVO<List<Request>> responseVo;

        try{
            List<Request> requestList = requestService.insertRequestsWithTitles(groundId, requestInsertManyDto);
            responseVo = ResponseVO.<List<Request>>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서들이 생성되었습니다.")
                    .data(requestList)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<List<Request>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{reqeustId}")
    @ApiOperation(value="문서 아이디로 요청 문서 가져오기")
    public ResponseEntity<?> getRequest(@PathVariable("groundId") int groundId, @PathVariable("RequestId") String RequestId){
        ResponseVO<Request> responseVo;

        try{
            Request Request = requestService.getRequest(groundId, RequestId);
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서를 불러왔습니다.")
                    .data(Request)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/step1")
    @ApiOperation(value="step1 문서들 불러오기")
    public ResponseEntity<?> getStep1Requests(@PathVariable("groundId") int groundId){
        ResponseVO<List<Request>> responseVo;

        try{
            List<Request> Requests = requestService.getStep1Requests(groundId);
            responseVo = ResponseVO.<List<Request>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step1 문서들을 불러왔습니다.")
                    .data(Requests)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<List<Request>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/step2")
    @ApiOperation(value="step2 문서들 불러오기")
    public ResponseEntity<?> getStep2Requests(@PathVariable("groundId") int groundId){
        ResponseVO<List<Request>> responseVo;

        try{
            List<Request> Requests = requestService.getStep2Requests(groundId);
            responseVo = ResponseVO.<List<Request>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step2 문서들을 불러왔습니다.")
                    .data(Requests)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<List<Request>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    @ApiOperation(value="문서 수정하기")
    public ResponseEntity<?> updateRequest(@PathVariable("groundId") int groundId, @RequestBody RequestUpdateDto requestUpdateDto) {
        ResponseVO<Request> responseVo;

        try{
            Request Request = requestService.updateRequest(groundId, requestUpdateDto);
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 수정했습니다.")
                    .data(Request)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/move")
    @ApiOperation(value="요청 문서 위치이동하기")
    public ResponseEntity<?> moveRequest(@PathVariable("groundId") int groundId,
                                         @ApiParam(value="id -> 옮기려는 문서의 아이디\n" +
                                                 "parentId -> 목적지 부모의 아이디") @RequestBody RequestMoveDto requestMoveDto) {
        ResponseVO<Request> responseVo;

        try{
            Request Request = requestService.moveRequest(groundId, requestMoveDto);
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 이동했습니다.")
                    .data(Request)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    @ApiOperation(value="요청 문서 삭제하기")
    public ResponseEntity<?> deleteRequest(@PathVariable("groundId") int groundId, @RequestBody RequestDeleteDto RequestDeleteDto){

        ResponseVO<Request> responseVo;

        try{
            requestService.deleteRequest(groundId, RequestDeleteDto);
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
