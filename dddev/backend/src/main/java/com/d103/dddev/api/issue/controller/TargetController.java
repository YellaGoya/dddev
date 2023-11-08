package com.d103.dddev.api.issue.controller;

import com.d103.dddev.api.issue.model.dto.TargetDto;
import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.model.message.Response;
import com.d103.dddev.api.issue.service.TargetService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ground/{groundId}/target")
@Api(tags = "목표 문서 API")
@AllArgsConstructor
@Slf4j
public class TargetController {

    private final TargetService targetService;

    @ApiOperation(value="목표 문서 생성", notes="목표 문서 생성 API")
    @PostMapping("/create")
    public ResponseEntity<TargetDto.Create.Response> createTarget(@PathVariable Integer groundId,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       @RequestHeader String Authorization) {
        try{
            TargetDto.Create.Response response = targetService.createTarget(groundId, userDetails);
            log.debug("목표 문서 생성 : {}", response);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("목표 문서 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<TargetDto.List.Response> targetList(@PathVariable Integer groundId,
                                     @RequestHeader String Authorization){
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
    public ResponseEntity<TargetDto.Detail.Response> targetDetail(@PathVariable Integer groundId,
                                       @PathVariable String targetId,
                                       @RequestHeader String Authorization){
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
    public ResponseEntity<TargetDto.Delete.Response> targetDelete(@PathVariable Integer groundId,
                                       @PathVariable String targetId,
                                       @RequestHeader String Authorization){
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
    public ResponseEntity<TargetDto.Update.Response> targetUpdate(@PathVariable Integer groundId,
                                       @PathVariable String targetId,
                                       @RequestBody TargetDto.Update.Request request,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       @RequestHeader String Authorization){
        try{
            log.info("목표 문서 수정");
            TargetDto.Update.Response response = targetService.targetUpdate(request, targetId, userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("문서 트리 조회")
    @GetMapping("/total")
    public ResponseEntity<TargetDto.Tree.Response> total(@PathVariable Integer groundId,
                                @RequestHeader String Authorization){
        try{
            log.info("문서 트리 조회");
            TargetDto.Tree.Response response = targetService.Tree(groundId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
