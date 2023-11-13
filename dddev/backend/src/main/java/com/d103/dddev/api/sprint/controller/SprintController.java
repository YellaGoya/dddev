package com.d103.dddev.api.sprint.controller;


import com.d103.dddev.api.common.ResponseDto;
import com.d103.dddev.api.sprint.repository.dto.requestDto.SprintUpdateDto;
import com.d103.dddev.api.sprint.repository.dto.responseDto.SprintResponseDto;
import com.d103.dddev.api.sprint.repository.entity.SprintEntity;
import com.d103.dddev.api.sprint.service.SprintService;
import com.d103.dddev.common.exception.sprint.SprintNotFoundException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ground/{groundId}/sprint")
@Api(tags = {"스프린트 API"})
public class SprintController {
    private final SprintService sprintService;

    @PostMapping
    @ApiOperation(value = "스프린트 생성", notes = "스프린트가 생성이 된다.\n" + "스프린트 생성할 때 현재 만드는 날짜로 시작 날짜와 끝나는 날짜가 결정이 되며 월요일~금요일에 생성을 하면 그 주 시작일은 월요일, 끝나는 일은 금요일이된다.\n"+
    "주말에 생성을 하면 시작일은 다음주 월요일, 끝나는 일은 다음주 금요일이 된다.\n" +
    "생성은 여러개 할 수 있다. 하지만 시작을 여러개 할 수는 없다.\n" +
            "기본 이름은 [그라운드 이름 + 그 주차]로 기본적으로 생성된다.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "스프린트 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<SprintResponseDto>> createSprint(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                       @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<SprintResponseDto> responseDto;
        try{
            SprintResponseDto returnSprint = sprintService.createSprint(groundId);
            responseDto = ResponseDto.<SprintResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트가 생성되었습니다.")
                    .data(returnSprint)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<SprintResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // groundId로 거기에 속하는 sprint들 다 들고오기
    @GetMapping
    @ApiOperation(value = "그라운드 소속 모든 sprint 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "스프린트 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<SprintResponseDto>>> loadSprintList(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                          @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<SprintResponseDto>> responseDto;

        try{
            List<SprintResponseDto> sprintList = sprintService.loadSprintList(groundId);
            responseDto = ResponseDto.<List<SprintResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트 목록을 불러왔습니다.")
                    .data(sprintList)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<SprintResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/recent")
    @ApiOperation(value = "가장 최근 생성한 sprint 들고오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "스프린트 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<SprintResponseDto>>> loadRecentSprint(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                                 @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<SprintResponseDto>> responseDto;

        try{
            List<SprintResponseDto> sprintList = sprintService.loadSprintList(groundId);
            responseDto = ResponseDto.<List<SprintResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 불러왔습니다.")
                    .data(sprintList)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<SprintResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{sprintId}")
    @ApiOperation(value = "스프린트 삭제하기", notes = "스프린트 삭제하면 연결되어 있는 이슈들 다시 스프린트 소속 제외")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "스프린트 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<Void>> deleteSprint(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                          @ApiParam(value = "스프린트 아이디")@PathVariable("sprintId") int sprintId,
                                                          @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<Void> responseDto;

        try{
            sprintService.deleteSprint(sprintId);
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{sprintId}")
    @ApiOperation(value = "스프린트 수정하기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "스프린트 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<SprintResponseDto>> updateSprint(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                  @ApiParam(value = "스프린트 아이디")@PathVariable("sprintId") int sprintId,
                                                                  @ApiParam(value = "goal(필수x)은 스프린트의 목표 지라에 보면은 스프린트 제목밑에 자그마한 글씨로 목표를 쓸 수 있다. 그 내용이다.\n" +
                                                                          "name(필수 x)은 스프린트의 이름")@RequestBody SprintUpdateDto sprintUpdateDto,
                                                                  @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<SprintResponseDto> responseDto;

        try{
            SprintResponseDto returnSprint = sprintService.updateSprint(sprintId, sprintUpdateDto);
            responseDto = ResponseDto.<SprintResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 업데이트했습니다.")
                    .data(returnSprint)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(SprintNotFoundException e){
            responseDto = ResponseDto.<SprintResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<SprintResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<SprintResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 스프린트 시작버튼을 눌렀을 때
    @PutMapping("/{sprintId}/start")
    @ApiOperation(value = "스프린트 시작하기", notes = "status 0(기본 상태) -> 1(진행 중)로 바꿈\n" +
            "스프린트 시작은 주말에 하지는 못한다. 무조건 평일에 할 수 있다.\n" +
            "또한 이미 진행 중인 스프린트가 있다면 시작할 수 없다.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "이미 진행중인 스프린트있음 에러"),
            @ApiResponse(code = 403, message = "스프린트를 주말에 시작 에러"),
            @ApiResponse(code = 404, message = "스프린트 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<Void>> startSprint(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                         @ApiParam(value = "스프린트 아이디")@PathVariable("sprintId") int sprintId,
                                                         @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<Void> responseDto;

        try{
            // 주말에 시작할 수 없음
            int day = LocalDate.now().getDayOfWeek().getValue();
            if(day >= 6 && day <= 7) throw new UnsupportedOperationException("주말에 시작할 수 없습니다.");

            sprintService.startSprint(groundId, sprintId);
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 시작했습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(SprintNotFoundException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(IllegalAccessException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }catch(UnsupportedOperationException e){
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

    // 스프린트 완료버튼을 눌렀을 때
    @PutMapping("/{sprintId}/complete")
    @ApiOperation(value = "스프린트 완료하기", notes = "status 1(진행중) -> 2(완료)로 바꿈\n" +
            "스프린트를 완료하면은 완료에 있던 이슈들은 완료처리\n " +
            "해야할 일, 진행중에 있던 이슈들은 다시 백로그 상태로 바뀐다.\n" +
            "토요일, 일요일에 완료된 이슈들은 금요일에 완료된걸로 한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "스프린트 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<Void>> completeSprint(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                            @ApiParam(value = "스프린트 아이디") @PathVariable("sprintId") int sprintId,
                                                            @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<Void> responseDto;

        try{
            sprintService.completeSprint(sprintId);
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 완료헀습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(SprintNotFoundException e){
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
}
