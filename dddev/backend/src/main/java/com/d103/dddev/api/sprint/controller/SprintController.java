package com.d103.dddev.api.sprint.controller;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.sprint.repository.entity.SprintEntity;
import com.d103.dddev.api.sprint.service.SprintService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "스프린트 생성", notes = "스프린트 생성하는 API")
    public ResponseEntity<?> createSprint(@PathVariable("groundId") int groundId){
        ResponseVO<SprintEntity> responseVo;

        try{
            SprintEntity returnSprint = sprintService.createSprint(groundId);
            responseVo = ResponseVO.<SprintEntity>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트가 생성되었습니다.")
                    .data(returnSprint)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }
        catch(Exception e){
            responseVo = ResponseVO.<SprintEntity>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // groundId로 거기에 속하는 sprint들 다 들고오기
    @GetMapping
    @ApiOperation(value = "그라운드 소속 모든 sprint 불러오기")
    public ResponseEntity<?> loadSprintList(@PathVariable("groundId") int groundId){
        ResponseVO<List> responseVo;

        try{
            List<SprintEntity> sprintList = sprintService.loadSprintList(groundId);
            responseVo = ResponseVO.<List>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 불러왔습니다.")
                    .data(sprintList)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }
        catch(Exception e){
            responseVo = ResponseVO.<List>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{sprintId}")
    @ApiOperation(value = "스프린트 삭제하기")
    public ResponseEntity<?> deleteSprint(@PathVariable("sprintId") int sprintId){
        ResponseVO<Object> responseVo;

        try{
            sprintService.deleteSprint(sprintId);
            responseVo = ResponseVO.builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }
        catch(Exception e){
            responseVo = ResponseVO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{sprintId}")
    @ApiOperation(value = "스프린트 수정하기")
    public ResponseEntity<?> updateSprint(@PathVariable("sprintId") int sprintId, @RequestBody SprintEntity sprint){
        ResponseVO<SprintEntity> responseVo;

        try{
            SprintEntity returnSprint = sprintService.updateSprint(sprintId, sprint);
            responseVo = ResponseVO.<SprintEntity>builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 업데이트했습니다.")
                    .data(returnSprint)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<SprintEntity>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 스프린트 시작버튼을 눌렀을 때
    @PutMapping("/{sprintId}/start")
    @ApiOperation(value = "스프린트 시작하기")
    public ResponseEntity<?> startSprint(@PathVariable("sprintId") int sprintId){
        ResponseVO<Object> responseVo;

        try{
            sprintService.startSprint(sprintId);
            responseVo = ResponseVO.builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 시작했습니다.")
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }
        catch(Exception e){
            responseVo = ResponseVO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 스프린트 완료버튼을 눌렀을 때
    @PutMapping("/{sprintId}/complete")
    @ApiOperation(value = "스프린트 완료하기")
    public ResponseEntity<?> completeSprint(@PathVariable("sprintId") int sprintId){
        ResponseVO<Object> responseVo;

        try{
            sprintService.deleteSprint(sprintId);
            responseVo = ResponseVO.builder()
                    .code(HttpStatus.OK.value())
                    .message("스프린트를 완료헀습니다.")
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }
        catch(Exception e){
            responseVo = ResponseVO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
