package com.d103.dddev.api.issue.controller;

import com.d103.dddev.api.issue.model.dto.TargetDto;
import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.model.message.Response;
import com.d103.dddev.api.issue.service.TargetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ground/{groundId}/target")
@Api(tags = "목표 문서 API")
@AllArgsConstructor
@Slf4j
public class TargetController {

    private final TargetService targetService;

    @ApiOperation("목표 문서 생성")
    @PostMapping("/create")
    public ResponseEntity createTarget(@PathVariable String groundId) {
        try{
            log.info("목표 문서 생성");
            TargetDto.Create.Response response = targetService.createTarget(groundId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("목표 문서 목록 조회")
    @GetMapping("/list")
    public ResponseEntity targetList(@PathVariable String groundId){
        try{
            log.info("목표 문서 목록 조회");
            TargetDto.List.Response response = targetService.targetList(groundId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("목표 문서 상세 조회")
    @GetMapping("/{targetId}")
    public ResponseEntity targetDetail(@PathVariable String groundId, @PathVariable String targetId){
        try{
            log.info("목표 문서 상세 조회");
            TargetDto.Detail.Response response = targetService.targetDetail(groundId, targetId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }

    @ApiOperation("목표 삭제")
    @DeleteMapping("/{targetId}")
    public ResponseEntity targetDelete(@PathVariable String groundId, @PathVariable String targetId){
        try{
            log.info("목표 문서 삭제");
            TargetDto.Delete.Response response = targetService.targetDelete(groundId, targetId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("목표 수정")
    @PutMapping("/{targetId}")
    public ResponseEntity targetUpdate(@PathVariable String groundId, @PathVariable String targetId, @RequestBody TargetDto.Update.Request request){
        try{
            log.info("목표 문서 수정");
            TargetDto.Update.Response response = targetService.targetUpdate(request, targetId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
