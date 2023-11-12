package com.d103.dddev.api.issue.controller;

import com.d103.dddev.api.issue.model.dto.CheckDto;
import com.d103.dddev.api.issue.model.dto.IssueDto;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<CheckDto.Create.Response> createCheck(@PathVariable Integer groundId,
                                                                @RequestBody @ApiParam(value = "체크포인트 생성 요청") CheckDto.Create.Request request,
                                                                @AuthenticationPrincipal UserDetails userDetails,
                                                                @RequestHeader String Authorization) {
        try{
            log.info("체크포인트 문서 생성");
            CheckDto.Create.Response response = checkService.createCheck(groundId, request, userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(), HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("체크포인트 문서 목록 조회")
    @GetMapping("{targetId}/list")
    public ResponseEntity<CheckDto.List.Response> checkList(@PathVariable Integer groundId,
                                    @PathVariable String targetId,
                                    @RequestHeader String Authorization){
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
    public ResponseEntity<CheckDto.Detail.Response> checkDetail(@PathVariable Integer groundId,
                                      @PathVariable String checkId,
                                      @RequestHeader String Authorization){
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
    public ResponseEntity<CheckDto.Delete.Response> checkDelete(@PathVariable Integer groundId,
                                      @PathVariable String checkId,
                                      @RequestHeader String Authorization){
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

    @ApiOperation(value = "체크포인트 문서 수정", notes="체크 포인트 문서 수정 => 들어오는 값 그대로 저장")
    @PutMapping("/{checkId}")
    public ResponseEntity<CheckDto.Update.Response> checkUpdate(@PathVariable Integer groundId,
                                      @PathVariable String checkId,
                                      @RequestBody CheckDto.Update.Request request,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestHeader String Authorization){
        try{
            log.info("체크포인트 문서 수정");
            CheckDto.Update.Response response = checkService.checkUpdate(request, checkId, userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("목표 문서와 연결")
    @PutMapping("{checkId}/connect")
    public ResponseEntity<CheckDto.Connect.Response> connectTarget(@PathVariable Integer groundId,
                                       @PathVariable String checkId,
                                       @RequestBody CheckDto.Connect.Request request,
                                        @AuthenticationPrincipal UserDetails userDetails,
                                        @RequestHeader String Authorization){
        try{
            log.info("목표 문서와 연결");
            CheckDto.Connect.Response response = checkService.connectTarget(request, userDetails, checkId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @ApiOperation(value = "체크포인트 문서 제목 변경",notes = "체크 포인트 문서 제목 변경 => 들어오는 값 그대로 저장")
    @PutMapping("/{checkId}/title")
    public ResponseEntity<CheckDto.Title.Response> targetTitle(@PathVariable Integer groundId,
                                                                @PathVariable String checkId,
                                                                @RequestBody CheckDto.Title.Request request,
                                                                @AuthenticationPrincipal UserDetails userDetails,
                                                                @RequestHeader String Authorization){
        try{
            log.info("이슈 문서 제목 변경");
            CheckDto.Title.Response response = checkService.checkTitle(request, checkId,userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("체크포인트 문서 전체 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<CheckDto.List.Response> totalCheckList(@PathVariable Integer groundId,
                                                                 @RequestHeader String Authorization){
        try{
            log.info("체크 포인트 문서 전체 목록 조회");
            CheckDto.List.Response response = checkService.checkTotalList(groundId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation(value = "목표 문서 템플릿 여부 변경", notes = "template 속성 여부 변경")
    @PutMapping("/{checkId}/template")
    public ResponseEntity<CheckDto.Template.Response> isTemplate(@PathVariable Integer groundId,
                                                               @PathVariable String checkId,
                                                               @RequestHeader String Authorization){
        try{
            log.info("이슈 문서 제목 변경");
            CheckDto.Template.Response response = checkService.isTemplate(checkId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }


}
