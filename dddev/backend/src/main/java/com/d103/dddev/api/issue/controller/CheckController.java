package com.d103.dddev.api.issue.controller;

import com.d103.dddev.api.issue.model.dto.CheckDto;
import com.d103.dddev.api.issue.model.dto.TargetDto;
import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.model.message.Response;
import com.d103.dddev.api.issue.service.CheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ground/{groundId}/check")
@Api(tags = "체크 포인트 문서 API")
@AllArgsConstructor
@Slf4j
public class CheckController {

    private final CheckService checkService;

    @ApiOperation(value="체크포인트 문서 생성", notes = "체크포인트 문서 생성 API", response = CheckDto.Create.Response.class)
    @PostMapping("/create")
    public ResponseEntity createCheck(@PathVariable String groundId, @RequestBody @ApiParam(value = "체크포인트 생성 요청") CheckDto.Create.Request request) {
        try{
            log.info("체크포인트 문서 생성");
            CheckDto.Create.Response response = checkService.createCheck(groundId, request);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(), HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("체크포인트 문서 목록 조회")
    @GetMapping("{targetId}/list")
    public ResponseEntity checkList(@PathVariable String groundId, @PathVariable String targetId){
        try{
            log.info("체크포인트 문서 목록 조회");
            CheckDto.List.Response response = checkService.checkList(groundId, targetId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("체크포인트 문서 상세 조회")
    @GetMapping("/{checkId}")
    public ResponseEntity checkDetail(@PathVariable String groundId, @PathVariable String checkId){
        try{
            log.info("체크포인트 문서 상세 조회");
            CheckDto.Detail.Response response = checkService.checkDetail(groundId, checkId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }

    @ApiOperation("체크포인트 문서 삭제")
    @DeleteMapping("/{checkId}")
    public ResponseEntity checkDelete(@PathVariable String groundId, @PathVariable String checkId){
        try{
            log.info("체크포인트 문서 삭제");
            CheckDto.Delete.Response response = checkService.checkDelete(groundId, checkId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("체크포인트 문서 수정")
    @PutMapping("/{checkId}")
    public ResponseEntity checkUpdate(@PathVariable String groundId, @PathVariable String checkId, @RequestBody CheckDto.Update.Request request){
        try{
            log.info("체크포인트 문서 수정");
            CheckDto.Update.Response response = checkService.checkUpdate(request, checkId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("목표 문서와 연결")
    @PutMapping("/connect")
    public ResponseEntity connectTarget(@PathVariable String groundId, @RequestBody CheckDto.Connect.Request request){
        try{
            log.info("목표 문서와 연결");
            CheckDto.Connect.Response response = checkService.connectTarget(request);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
