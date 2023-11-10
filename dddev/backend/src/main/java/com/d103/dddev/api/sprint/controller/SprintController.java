package com.d103.dddev.api.sprint.controller;

import com.d103.dddev.api.common.ResponseDto;
import com.d103.dddev.api.sprint.controller.error.ErrorResponse;
import com.d103.dddev.api.sprint.repository.dto.SprintUpdateDto;
import com.d103.dddev.api.sprint.repository.entity.SprintEntity;
import com.d103.dddev.api.sprint.service.SprintService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ground/{groundId}/sprint")
@Api(tags = {"스프린트 API"})
public class SprintController {
    private final SprintService sprintService;

    @PostMapping
    @ApiOperation(value = "스프린트 생성", notes = "스프린트 생성하는 API", response = ResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 4000, message = "메시지", response = ErrorResponse.class, examples = @Example(value = {
                    @ExampleProperty(mediaType = "application/json", value = "{\"status\":\"200\", \"message\":\"나와라\"}")
            }))
    })
    public ResponseEntity<?> createSprint(@PathVariable("groundId") int groundId,
                                          @RequestHeader String Authorization){
        ResponseDto<SprintEntity> responseDto;
        try{
            SprintEntity returnSprint = sprintService.createSprint(groundId);
            responseDto = ResponseDto.<SprintEntity>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트가 생성되었습니다.")
                    .data(returnSprint)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        catch(Exception e){
            responseDto = ResponseDto.<SprintEntity>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // groundId로 거기에 속하는 sprint들 다 들고오기
    @GetMapping
    @ApiOperation(value = "그라운드 소속 모든 sprint 불러오기")
    public ResponseEntity<?> loadSprintList(@PathVariable("groundId") int groundId,
                                            @RequestHeader String Authorization){
        ResponseDto<List> responseDto;

        try{
            List<SprintEntity> sprintList = sprintService.loadSprintList(groundId);
            responseDto = ResponseDto.<List>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 불러왔습니다.")
                    .data(sprintList)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        catch(Exception e){
            responseDto = ResponseDto.<List>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{sprintId}")
    @ApiOperation(value = "스프린트 삭제하기")
    public ResponseEntity<?> deleteSprint(@PathVariable("sprintId") int sprintId,
                                          @RequestHeader String Authorization){
        ResponseDto<Object> responseDto;

        try{
            sprintService.deleteSprint(sprintId);
            responseDto = ResponseDto.builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        catch(Exception e){
            responseDto = ResponseDto.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{sprintId}")
    @ApiOperation(value = "스프린트 수정하기")
    public ResponseEntity<?> updateSprint(@PathVariable("sprintId") int sprintId, @RequestBody SprintUpdateDto sprintUpdateDto,
                                          @RequestHeader String Authorization){
        ResponseDto<SprintEntity> responseDto;

        try{
            SprintEntity returnSprint = sprintService.updateSprint(sprintId, sprintUpdateDto);
            responseDto = ResponseDto.<SprintEntity>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 업데이트했습니다.")
                    .data(returnSprint)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<SprintEntity>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 스프린트 시작버튼을 눌렀을 때
    @PutMapping("/{sprintId}/start")
    @ApiOperation(value = "스프린트 시작하기")
    public ResponseEntity<?> startSprint(@PathVariable("groundId") int groundId, @PathVariable("sprintId") int sprintId,
                                         @RequestHeader String Authorization){
        ResponseDto<Object> responseDto;

        try{
            sprintService.startSprint(sprintId);
            responseDto = ResponseDto.builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 시작했습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        catch(Exception e){
            responseDto = ResponseDto.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 스프린트 완료버튼을 눌렀을 때
    @PutMapping("/{sprintId}/complete")
    @ApiOperation(value = "스프린트 완료하기")
    public ResponseEntity<?> completeSprint(@PathVariable("sprintId") int sprintId,
                                            @RequestHeader String Authorization){
        ResponseDto<Object> responseDto;

        try{
            sprintService.completeSprint(sprintId);
            responseDto = ResponseDto.builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 완료헀습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        catch(Exception e){
            responseDto = ResponseDto.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
