package com.d103.dddev.api.request.controller;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.request.collection.Comment;
import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.request.repository.dto.requestDto.*;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestResponseDto;
import com.d103.dddev.api.request.service.RequestServiceImpl;
import com.d103.dddev.api.user.repository.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/ground/{groundId}/request")
@RequiredArgsConstructor
@Api(tags = {"요청 문서 API"})
public class RequestController {
    private final RequestServiceImpl requestService;

    @PostMapping("/create")
    @ApiOperation(value="요청 문서 생성")
    public ResponseEntity<?> insertRequest(@PathVariable("groundId") int groundId,
                                           @ApiParam(value = "step -> required\n"+
                                                   "step값이 1일때는 parentId 필요없음\n" +
                                                   "title -> not required")@RequestBody RequestInsertOneDto requestInsertOneDto,
                                           @RequestHeader String Authorization,
                                           @AuthenticationPrincipal UserDetails userDetails){
        ResponseVO<Request> responseVo;

        try{
            Request request = requestService.insertRequest(groundId, requestInsertOneDto, userDetails);
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
                                                      @RequestBody RequestInsertManyDto requestInsertManyDto,
                                                      @RequestHeader String Authorization,
                                                      @AuthenticationPrincipal UserDetails userDetails){
        ResponseVO<List<Request>> responseVo;

        try{
            List<Request> requestList = requestService.insertRequestsWithTitles(groundId, requestInsertManyDto, userDetails);
            responseVo = ResponseVO.<List<Request>>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서들이 생성되었습니다.")
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

    @GetMapping("/{requestId}")
    @ApiOperation(value="요청 문서 상세 조회")
    public ResponseEntity<?> getRequest(@PathVariable("groundId") int groundId, @PathVariable("requestId") String RequestId,
                                        @RequestHeader String Authorization){
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
    public ResponseEntity<?> getStep1Requests(@PathVariable("groundId") int groundId,
    @RequestHeader String Authorization){
        ResponseVO<List<RequestResponseDto>> responseVo;

        try{
            List<RequestResponseDto> requests = requestService.getStep1Requests(groundId);
            responseVo = ResponseVO.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step1 문서들을 불러왔습니다.")
                    .data(requests)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/step2")
    @ApiOperation(value="step2 문서들 불러오기")
    public ResponseEntity<?> getStep2Requests(@PathVariable("groundId") int groundId,
                                              @RequestHeader String Authorization){
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

    @PutMapping("/{requestId}")
    @ApiOperation(value="요청 문서 수정")
    public ResponseEntity<?> updateRequest(@PathVariable("groundId") int groundId,
                                           @PathVariable("requestId") String requestId,
                                           @RequestBody RequestUpdateDto requestUpdateDto,
                                           @RequestHeader String Authorization,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        ResponseVO<Request> responseVo;

        try{
            Request Request = requestService.updateRequest(groundId, requestId, requestUpdateDto, userDetails);
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
    @PutMapping("/send/{requestId}")
    @ApiOperation(value="요청 문서 요청보내기")
    public ResponseEntity<?> sendRequest(@PathVariable("groundId") int groundId,
                                         @PathVariable("requestId") String requestId,
                                         @RequestBody RequestUpdateDto requestUpdateDto,
                                         @RequestHeader String Authorization,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        ResponseVO<Request> responseVo;

        try{
            requestService.sendRequest(groundId, requestId, requestUpdateDto, userDetails);
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청을 보냈습니다.")
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
    @PutMapping("/{requestId}/createComment")
    @ApiOperation(value="요청 문서 댓글달기")
    public ResponseEntity<?> createComment(@PathVariable("groundId") int groundId,
                                         @PathVariable("requestId") String requestId,
                                         @RequestBody String comment,
                                         @RequestHeader String Authorization,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        ResponseVO<Comment> responseVo;

        try{
            Comment saveComment = requestService.createComment(groundId, requestId, comment, userDetails);
            responseVo = ResponseVO.<Comment>builder()
                    .code(HttpStatus.OK.value())
                    .message("댓글을 달았습니다.")
                    .data(saveComment)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<Comment>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/move/{requestId}")
    @ApiOperation(value="요청 문서 위치이동")
    public ResponseEntity<?> moveRequest(@PathVariable("groundId") int groundId,
                                         @PathVariable("requestId") String requestId,
                                         @ApiParam(value= "parentId -> 목적지 부모의 아이디") @RequestBody RequestMoveDto requestMoveDto,
                                         @RequestHeader String Authorization) {
        ResponseVO<Request> responseVo;

        try{
            Request Request = requestService.moveRequest(groundId, requestId, requestMoveDto);
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

    @DeleteMapping("/{requestId}")
    @ApiOperation(value="요청 문서 삭제")
    public ResponseEntity<?> deleteRequest(@PathVariable("groundId") int groundId, @PathVariable("requestId") String requestId,@RequestHeader String Authorization){

        ResponseVO<Request> responseVo;

        try{
            requestService.deleteRequest(groundId, requestId);
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

    @PutMapping("/{requestId}/title")
    @ApiOperation(value="요청 문서 제목 수정")
    public ResponseEntity<?> updateRequest(@PathVariable("groundId") int groundId,
                                           @PathVariable("requestId") String requestId,
                                           @RequestBody RequestTitleDto requestTitleDto,
                                           @RequestHeader String Authorization,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        ResponseVO<Request> responseVo;

        try{
            Request Request = requestService.titleRequest(groundId, requestId, requestTitleDto, userDetails);
            responseVo = ResponseVO.<Request>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서의 제목을 수정했습니다.")
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
}
