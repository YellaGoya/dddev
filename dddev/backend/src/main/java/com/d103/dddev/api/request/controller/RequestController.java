package com.d103.dddev.api.request.controller;

import com.d103.dddev.api.common.ResponseDto;
import com.d103.dddev.api.request.repository.dto.requestDto.*;
import com.d103.dddev.api.request.repository.dto.responseDto.CommentResponseDto;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestResponseDto;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestTitleResponseDto;
import com.d103.dddev.api.request.repository.dto.responseDto.RequestTreeResponseDto;
import com.d103.dddev.api.request.service.RequestServiceImpl;
import com.d103.dddev.api.user.repository.dto.UserDto;
import com.d103.dddev.api.user.repository.entity.User;
import com.d103.dddev.common.exception.document.DocumentNotFoundException;
import com.d103.dddev.common.exception.user.UserNotFoundException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.management.InvalidAttributeValueException;
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
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<RequestResponseDto>> insertRequest(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                              @ApiParam(value = "step1문서 생성할 때 parentId 필요없음\n" +
                                                                        "미분류로 생성할 때 parentId 미분류 문서 id\n" +
                                                                        "title -> not required 없으면 빈 문자열 \"\"로 생성")@RequestBody RequestInsertOneDto requestInsertOneDto,
                                                              @ApiParam(value = "인증 정보")@RequestHeader String Authorization,
                                                              HttpServletRequest request){
        ResponseDto<RequestResponseDto> responseDto;
        ModelAndView modelAndView = (ModelAndView) request.getAttribute("modelAndView");
        User user = (User) modelAndView.getModel().get("user");
        UserDto userDto = user.convertToDto();

        try{
            RequestResponseDto requestResponseDto = requestService.insertRequest(groundId, requestInsertOneDto, userDto);
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서가 생성되었습니다.")
                    .data(requestResponseDto)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/titles")
    @ApiOperation(value="제목들로 step1 요청 문서들 생성")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<RequestResponseDto>>> insertRequestsWithTitles(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                                          @RequestBody RequestInsertManyDto requestInsertManyDto,
                                                                                          @ApiParam(value = "인증 정보")@RequestHeader String Authorization,
                                                                                          HttpServletRequest request){
        ResponseDto<List<RequestResponseDto>> responseDto;
        ModelAndView modelAndView = (ModelAndView) request.getAttribute("modelAndView");
        User user = (User) modelAndView.getModel().get("user");
        UserDto userDto = user.convertToDto();

        try{
            List<RequestResponseDto> requestList = requestService.insertRequestsWithTitles(groundId, requestInsertManyDto, userDto);
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서들이 생성되었습니다.")
                    .data(requestList)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{requestId}")
    @ApiOperation(value="요청 문서 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<RequestResponseDto>> getRequest(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                      @ApiParam(value = "문서 아이디")@PathVariable("requestId") String requestId,
                                                                      @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<RequestResponseDto> responseDto;

        try{
            RequestResponseDto request = requestService.getRequest(groundId, requestId);
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서를 불러왔습니다.")
                    .data(request)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total")
    @ApiOperation(value="문서트리 구조로 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<RequestTreeResponseDto>>> getTreeRequests(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                                     @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<RequestTreeResponseDto>> responseDto;

        try{
            List<RequestTreeResponseDto> requests = requestService.getTreeRequests(groundId);
            responseDto = ResponseDto.<List<RequestTreeResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서 트리를 불러왔습니다.")
                    .data(requests)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<RequestTreeResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/list")
    @ApiOperation(value="step1 문서들 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<RequestTitleResponseDto>>> getStep1Requests(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                                       @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<RequestTitleResponseDto>> responseDto;

        try{
            List<RequestTitleResponseDto> requests = requestService.getStep1Requests(groundId);
            responseDto = ResponseDto.<List<RequestTitleResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서 step1들을 불러왔습니다.")
                    .data(requests)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<RequestTitleResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/step2")
    @ApiOperation(value="step2 문서들 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<RequestResponseDto>>> getStep2Requests(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                                  @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<RequestResponseDto>> responseDto;

        try{
            List<RequestResponseDto> requests = requestService.getStep2Requests(groundId);
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step2 문서들을 불러왔습니다.")
                    .data(requests)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todo")
    @ApiOperation(value="step2 문서들 중에 해야할 일 문서 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<RequestResponseDto>>> getStep2TodoRequests(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                           @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<RequestResponseDto>> responseDto;

        try{
            List<RequestResponseDto> requests = requestService.getStep2TodoRequests(groundId);
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("해야할 일 문서들을 불러왔습니다.")
                    .data(requests)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/proceed")
    @ApiOperation(value="step2 문서들 중에 진행 중 문서 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<RequestResponseDto>>> getStep2ProceedRequests(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                              @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<RequestResponseDto>> responseDto;

        try{
            List<RequestResponseDto> requests = requestService.getStep2ProceedRequests(groundId);
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("진행 중 문서들을 불러왔습니다.")
                    .data(requests)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/done")
    @ApiOperation(value="step2 문서들 중에 완료된 문서 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<RequestResponseDto>>> getStep2DoneRequests(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                           @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<RequestResponseDto>> responseDto;

        try{
            List<RequestResponseDto> requests = requestService.getStep2DoneRequests(groundId);
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("완료 문서들을 불러왔습니다.")
                    .data(requests)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{requestId}")
    @ApiOperation(value="요청 문서 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<RequestResponseDto>> updateRequest(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                              @ApiParam(value = "문서아이디") @PathVariable("requestId") String requestId,
                                                              @ApiParam(value = "title(필수 x), content(필수 x)")@RequestBody RequestUpdateDto requestUpdateDto,
                                                              @ApiParam(value = "인증 정보")@RequestHeader String Authorization,
                                                              HttpServletRequest request) {
        ResponseDto<RequestResponseDto> responseDto;
        ModelAndView modelAndView = (ModelAndView) request.getAttribute("modelAndView");
        User user = (User) modelAndView.getModel().get("user");
        UserDto userDto = user.convertToDto();

        try{
            RequestResponseDto requestResponseDto = requestService.updateRequest(groundId, requestId, requestUpdateDto, userDto);
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 수정했습니다.")
                    .data(requestResponseDto)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{requestId}/status")
    @ApiOperation(value="요청 문서 진행 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<Void>> changeStatus(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                          @ApiParam(value = "문서아이디")@PathVariable("requestId") String requestId,
                                                          @ApiParam(value = "status -> 0: 해야할 일\n"+
                                                          "status -> 1: 진행 중\n"+
                                                          "status -> 2: 완료")@RequestBody RequestStatusDto requestStatusDto,
                                                          @ApiParam(value = "인증 정보")@RequestHeader String Authorization) {
        ResponseDto<Void> responseDto;

        try{
            requestService.changeStatus(groundId, requestId, requestStatusDto);
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("상태를 변경했습니다")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{requestId}/sender")
    @ApiOperation(value="요청 문서 보내는 사람 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "유저를 찾을 수 없음"),
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<Void>> changeSender(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                          @ApiParam(value = "문서 아이디")@PathVariable("requestId") String requestId,
                                                          @ApiParam(value = "보내는 사람의 githubId")@RequestBody RequestSenderDto requestSenderDto,
                                                          @ApiParam(value = "인증 정보")@RequestHeader String Authorization) {
        ResponseDto<Void> responseDto;

        try{
            requestService.changeSender(groundId, requestId, requestSenderDto);
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("보내는 사람을 변경했습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(UserNotFoundException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{requestId}/receiver")
    @ApiOperation(value="요청 문서 받는 사람 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "유저를 찾을 수 없음"),
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<Void>> changeReceiver(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                            @ApiParam(value = "문서 아이디")@PathVariable("requestId") String requestId,
                                                            @ApiParam(value = "받는 사람의 githubId")@RequestBody RequestReceiverDto requestReceiverDto,
                                                            @ApiParam(value = "인증 정보")@RequestHeader String Authorization) {
        ResponseDto<Void> responseDto;

        try{
            requestService.changeReceiver(groundId, requestId, requestReceiverDto);
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("받는 사람을 변경했습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(UserNotFoundException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{requestId}/comment")
    @ApiOperation(value="요청 문서 댓글달기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<CommentResponseDto>> createComment(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                         @ApiParam(value = "문서아이디")@PathVariable("requestId") String requestId,
                                                                         @ApiParam(value = "요청 문서에 달리는 댓글")@RequestBody RequestCommentDto comment,
                                                                         @ApiParam(value = "인증 정보")@RequestHeader String Authorization,
                                                                         HttpServletRequest request) {
        ResponseDto<CommentResponseDto> responseDto;
        ModelAndView modelAndView = (ModelAndView) request.getAttribute("modelAndView");
        User user = (User) modelAndView.getModel().get("user");
        UserDto userDto = user.convertToDto();

        try{
            CommentResponseDto saveComment = requestService.createComment(groundId, requestId, comment, userDto);
            responseDto = ResponseDto.<CommentResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("댓글을 생성했습니다.")
                    .data(saveComment)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<CommentResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<CommentResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<CommentResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{requestId}/connect")
    @ApiOperation(value="요청 문서 카테고리 이동")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<RequestResponseDto>> moveRequest(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                            @ApiParam(value = "문서아이디")@PathVariable("requestId") String requestId,
                                                            @ApiParam(value= "parentId -> 목적지 부모의 아이디") @RequestBody RequestMoveDto requestMoveDto,
                                                            @ApiParam(value = "인증 정보")@RequestHeader String Authorization) {
        ResponseDto<RequestResponseDto> responseDto;

        try{
            RequestResponseDto requestResponseDto = requestService.moveRequest(groundId, requestId, requestMoveDto);
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 이동했습니다.")
                    .data(requestResponseDto)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{requestId}")
    @ApiOperation(value="요청 문서 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<Void>> deleteRequest(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                           @ApiParam(value = "문서아이디")@PathVariable("requestId") String requestId,
                                                           @ApiParam(value = "인증 정보")@RequestHeader String Authorization){

        ResponseDto<Void> responseDto;

        try{
            requestService.deleteRequest(groundId, requestId);
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{requestId}/title")
    @ApiOperation(value="요청 문서 제목 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<RequestResponseDto>> updateRequest(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                              @ApiParam(value = "문서아이디")@PathVariable("requestId") String requestId,
                                                              @ApiParam(value = "변경하고 싶은 제목\n" +
                                                                      "title(필수) 없으면 422에러")@RequestBody RequestTitleDto requestTitleDto,
                                                              @ApiParam(value = "인증 정보")@RequestHeader String Authorization,
                                                              HttpServletRequest request) {
        ResponseDto<RequestResponseDto> responseDto;
        ModelAndView modelAndView = (ModelAndView) request.getAttribute("modelAndView");
        User user = (User) modelAndView.getModel().get("user");
        UserDto userDto = user.convertToDto();

        try{
            RequestResponseDto requestResponseDto = requestService.titleRequest(groundId, requestId, requestTitleDto, userDto);
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서의 제목을 수정했습니다.")
                    .data(requestResponseDto)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{requestId}/template")
    @ApiOperation(value="요청 문서 템플릿 여부 변경", notes = "template 속성 여부 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<RequestResponseDto>> changeTemplate(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                              @ApiParam(value = "문서 아이디")@PathVariable("requestId") String requestId,
                                                              @ApiParam(value = "인증 정보")@RequestHeader String Authorization) {
        ResponseDto<RequestResponseDto> responseDto;

        try{
            RequestResponseDto requestResponseDto = requestService.changeTemplate(groundId, requestId);
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("요청 문서 템플릿 여부 수정 완료")
                    .data(requestResponseDto)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<RequestResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
