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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ground/{groundId}/issue")
@AllArgsConstructor
@Api(tags = "이슈 문서 API")
@Slf4j
public class IssueController {
    private final IssueService issueService;
    @ApiOperation(value="이슈 문서 생성", notes = "모든 값이 들어가지 않아도 생성 가능(부모문서 => 미분류, 스프린트 ID => 0)", response = IssueDto.Create.Response.class)
    @PostMapping("/create")
    public ResponseEntity<IssueDto.Create.Response> createIssue(@PathVariable Integer groundId,
                                      @RequestBody @ApiParam(value = "이슈 생성 요청") IssueDto.Create.Request request,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestHeader String Authorization) {
        try{
            log.info("이슈 문서 생성");
            IssueDto.Create.Response response = issueService.issueCreate(groundId, request, userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(), HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation(value = "이슈 문서 목록 조회",notes = "스프린트 종료 된 이슈 제외 조회")
    @GetMapping("{checkId}/list")
    public ResponseEntity<IssueDto.List.Response> issueList(@PathVariable Integer groundId,
                                    @PathVariable String checkId,
                                    @RequestHeader String Authorization){
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
    public ResponseEntity<IssueDto.Detail.Response> issueDetail(@PathVariable Integer groundId,
                                      @PathVariable String issueId,
                                      @RequestHeader String Authorization){
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
    public ResponseEntity<IssueDto.Delete.Response> issueDelete(@PathVariable Integer groundId,
                                      @PathVariable String issueId,
                                      @RequestHeader String Authorization){
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

    @ApiOperation(value = "이슈 문서 수정",notes = "이슈 문서 수정 => 들어오는 값 그대로 저장")
    @PutMapping("/{issueId}")
    public ResponseEntity<IssueDto.Content.Response> issueContent(@PathVariable Integer groundId,
                                       @PathVariable String issueId,
                                       @RequestBody IssueDto.Content.Request request,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       @RequestHeader String Authorization){
        try{
            log.info("이슈 문서 수정");
            IssueDto.Content.Response response = issueService.issueContent(request, issueId,userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation(value = "이슈 문서 진행 상태 변경", notes = "0 : 진행 예정 \n" +
                                                        "1 : 진행 중 \n" +
                                                        "2 : 완료 \n" +
                                                        " + 진행 상태에 따라 startDate, endDate 생성\n" +
                                                        " + 완료 -> 진행 중 -> 진행 예정 처럼 뒤로 가는 경우 startDate, endDate 삭제됨")
    @PutMapping("/{issueId}/status")
    public ResponseEntity<IssueDto.Status.Response> issueStatus(@PathVariable Integer groundId,
                                      @PathVariable String issueId,
                                      @RequestBody IssueDto.Status.Request request,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestHeader String Authorization){
        try{
            log.info("이슈 문서 진행 상태 변경");
            IssueDto.Status.Response response = issueService.issueStatus(request, issueId,userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("체크포인트 문서와 연결")
    @PutMapping("/{issueId}/connect")
    public ResponseEntity<IssueDto.Connect.Response> issueConnect(@PathVariable Integer groundId,
                                       @PathVariable String issueId,
                                       @RequestBody IssueDto.Connect.Request request,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       @RequestHeader String Authorization){
        try{
            log.info("이슈 문서 상위 문서 연결");
            IssueDto.Connect.Response response = issueService.issueConnect(request, issueId,userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation(value = "이슈 문서 시간 변경",notes = "이슈 문서 시간 변경 => 들어오는 값 그대로 저장")
    @PutMapping("/{issueId}/time")
    public ResponseEntity<IssueDto.Time.Response> issueTime(@PathVariable Integer groundId,
                                    @PathVariable String issueId,
                                    @RequestBody IssueDto.Time.Request request,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    @RequestHeader String Authorization){
        try{
            log.info("이슈 문서 시간 변경");
            IssueDto.Time.Response response = issueService.issueTime(request, issueId,userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation("이슈 문서 스프린트 연결")
    @PutMapping("/{issueId}/sprint")
    public ResponseEntity issueSprint(@PathVariable Integer groundId,
                                      @PathVariable String issueId,
                                      @RequestBody IssueDto.Sprint.Request request,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestHeader String Authorization){
        try{
            log.info("이슈 문서 스프린트 연결");
            IssueDto.Sprint.Response response = issueService.issueSprint(request, issueId,userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation(value = "이슈 문서 제목 변경",notes = "이슈 문서 제목 변경 => 들어오는 값 그대로 저장")
    @PutMapping("/{issueId}/title")
    public ResponseEntity<IssueDto.Title.Response> issueTitle(@PathVariable Integer groundId,
                                                            @PathVariable String issueId,
                                                            @RequestBody IssueDto.Title.Request request,
                                                            @AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestHeader String Authorization){
        try{
            log.info("이슈 문서 제목 변경");
            IssueDto.Title.Response response = issueService.issueTitle(request, issueId,userDetails);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation(value = "이슈 문서(완료 포함) 목록 조회", notes = "완료 여부에 상관 없이 모두 조회")
    @GetMapping("{checkId}/list/total")
    public ResponseEntity<IssueDto.List.Response> issueTotalList(@PathVariable Integer groundId,
                                                            @PathVariable String checkId,
                                                            @RequestHeader String Authorization){
        try{
            log.info("이슈 문서 목록(완료 포함) 조회");
            IssueDto.List.Response response = issueService.issueTotalList(groundId, checkId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ApiOperation(value = "스프린트 소속 및 종료 제외 이슈 문서 목록 조회",notes = "현재 소속 스프린트 및 스프린트 종료 된 이슈 제외 조회")
    @GetMapping("{sprintId}/sprint")
    public ResponseEntity<IssueDto.List.Response> issueBackLog(@PathVariable Integer groundId,
                                                            @PathVariable Integer sprintId,
                                                            @RequestHeader String Authorization){
        try{
            log.info("현재 소속 스프린트 및 스프린트 종료 된 이슈 제외 조회");
            IssueDto.List.Response response = issueService.issueBackLog(groundId, sprintId);
            return Response.success(response);
        }catch(NoSuchElementException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.BAD_REQUEST));
        }catch(RuntimeException response){
            return Response.error(Error.error(response.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
