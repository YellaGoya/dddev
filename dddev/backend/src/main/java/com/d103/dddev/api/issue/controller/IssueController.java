package com.d103.dddev.api.issue.controller;

import com.d103.dddev.api.issue.model.dto.CheckDto;
import com.d103.dddev.api.issue.model.dto.IssueDto;
import com.d103.dddev.api.issue.model.dto.TargetDto;
import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.model.message.Response;
import com.d103.dddev.api.issue.service.IssueService;
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
@RequestMapping("/ground/{groundId}/issue")
@AllArgsConstructor
@Api(tags = "이슈 문서 API")
@Slf4j
public class IssueController {
    private final IssueService issueService;
    @ApiOperation(value="이슈 문서 생성", notes = "이슈 문서 생성 API", response = IssueDto.Create.Response.class)
    @PostMapping("/create")
    public ResponseEntity createIssue(@PathVariable String groundId, @RequestBody @ApiParam(value = "이슈 생성 요청") IssueDto.Create.Request request) {
        try{
            log.info("이슈 문서 생성");
            IssueDto.Create.Response response = issueService.issueCreate(groundId, request);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(), HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("이슈 문서 목록 조회")
    @GetMapping("{checkId}/list")
    public ResponseEntity issueList(@PathVariable String groundId, @PathVariable String checkId){
        try{
            log.info("이슈 문서 목록 조회");
            IssueDto.List.Response response = issueService.issueList(groundId, checkId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("이슈 문서 상세 조회")
    @GetMapping("/{issueId}")
    public ResponseEntity issueDetail(@PathVariable String groundId, @PathVariable String issueId){
        try{
            log.info("이슈 문서 상세 조회");
            IssueDto.Detail.Response response = issueService.issueDetail(groundId, issueId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }

    @ApiOperation("이슈 문서 삭제")
    @DeleteMapping("/{issueId}")
    public ResponseEntity issueDelete(@PathVariable String groundId, @PathVariable String issueId){
        try{
            log.info("이슈 문서 삭제");
            IssueDto.Delete.Response response = issueService.issueDelete(issueId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("이슈 문서 수정")
    @PutMapping("/{issueId}/content")
    public ResponseEntity issueContent(@PathVariable String groundId, @PathVariable String issueId, @RequestBody IssueDto.Content.Request request){
        try{
            log.info("이슈 문서 수정");
            IssueDto.Content.Response response = issueService.issueContent(request, issueId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("이슈 문서 진행 상태 변경")
    @PutMapping("/{issueId}/status")
    public ResponseEntity issueStatus(@PathVariable String groundId, @PathVariable String issueId, @RequestBody IssueDto.Status.Request request){
        try{
            log.info("이슈 문서 진행 상태 변경");
            IssueDto.Status.Response response = issueService.issueStatus(request, issueId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("이슈 문서 상위 문서 연결")
    @PutMapping("/{issueId}/connect")
    public ResponseEntity issueConnect(@PathVariable String groundId, @PathVariable String issueId, @RequestBody IssueDto.Connect.Request request){
        try{
            log.info("이슈 문서 상위 문서 연결");
            IssueDto.Connect.Response response = issueService.issueConnect(request, issueId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("이슈 문서 시간 변경")
    @PutMapping("/{issueId}/time")
    public ResponseEntity issueTime(@PathVariable String groundId, @PathVariable String issueId, @RequestBody IssueDto.Time.Request request){
        try{
            log.info("이슈 문서 시간 변경");
            IssueDto.Time.Response response = issueService.issueTime(request, issueId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("이슈 문서 스프린트 연결")
    @PutMapping("/{issueId}/sprint")
    public ResponseEntity issueSprint(@PathVariable String groundId, @PathVariable String issueId, @RequestBody IssueDto.Sprint.Request request){
        try{
            log.info("이슈 문서 스프린트 연결");
            IssueDto.Sprint.Response response = issueService.issueSprint(request, issueId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
