package com.dddev.log.controller;

import com.dddev.log.dto.ElasticSearchLog;
import com.dddev.log.dto.ResponseVO;
import com.dddev.log.dto.req.LogReq;
import com.dddev.log.dto.req.UserAuthReq;
import com.dddev.log.dto.res.LogRes;
import com.dddev.log.dto.res.PageableRes;
import com.dddev.log.dto.res.TokenRes;
import com.dddev.log.exception.ElasticSearchException;
import com.dddev.log.exception.UserUnAuthException;
import com.dddev.log.service.ElasticSearchLogService;
import com.dddev.log.service.GroundAuthService;
import com.dddev.log.service.UserLogAccessService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api(tags = {"로그 관련 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
@Slf4j
@CrossOrigin(originPatterns = {"https://k9d103.p.ssafy.io","http://localhost:3000","https://k9d103.p.ssafy.io:8001"})
public class logController {

    private final ElasticSearchLogService elasticSearchLogService;
    private final GroundAuthService groundAuthService;
    private final UserLogAccessService userLogAccessService;

    //토큰 저장
    @PostMapping("/auth")
    @ApiOperation(value = "사용자가 토큰을 발급 받으면 토근에 대한 정보를 저장하는 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "토큰 저장 완료"),
            @ApiResponse(code = 500, message = "서버 내부 오류")})
    public ResponseEntity<ResponseVO<List<TokenRes>>> userAuthSave(
            @ApiParam(value = "로그 기능 사용을 위한 토큰 발급 시 저장", required = true) @RequestBody UserAuthReq userAuthReq) {
        try{
            List<TokenRes> tokenResList = groundAuthService.save(userAuthReq.getToken());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<List<TokenRes>>(HttpStatus.OK.value(),
                    "토큰 저장 완료", tokenResList));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<List<TokenRes>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    //토큰 인증 확인
    @ApiOperation(value = "사용자의 토큰 유효성 체크")
    @ApiResponses(
            value = {@ApiResponse(code = 202, message = "토큰 유효성 검증 완료"),
                    @ApiResponse(code = 401, message = "유효하지 않은 토큰"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @GetMapping("/auth")
    public ResponseEntity<ResponseVO<?>> userAuth(
            @ApiParam(value = "발급 받은 토큰", required = true) @RequestHeader String token) {
        try{
            groundAuthService.checkValid(token);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseVO<>(HttpStatus.ACCEPTED.value(),
                    "토큰 유효성 검증 완료", null));
        }catch (UserUnAuthException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    "유효 하지 않은 토큰", null));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 내부 에러", null));
        }
    }

    //로그 저장
    @ApiOperation(value = "로그를 저장하는 API")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "로그 요청이 비정상적으로 많을 때"),
                    @ApiResponse(code = 401, message = "토큰이 유효하지 않을 때"),
                    @ApiResponse(code = 409, message = "저장된 Ground_ID가 없을 때"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @PostMapping("")
    public ResponseEntity<ResponseVO<?>> saveLog(
            @ApiParam(value = "자동으로 저장 되는 로그", required = true) @RequestBody LogReq logReq,
            @ApiParam(value = "발급 받은 토큰", required = true) @RequestHeader String token) {
        // 문자열을 파일로 저장
        try{
            String groundId = groundAuthService.checkValid(token);
            LocalDateTime localDateTime = LocalDateTime.now();
            userLogAccessService.count(groundId);
            elasticSearchLogService.save(groundId, ElasticSearchLog.builder().localDateTime(localDateTime).log(logReq.getLog()).build());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseVO<>(HttpStatus.CREATED.value(),
                            "로그 저장 완료", new LogRes(localDateTime, logReq.getLog())));
        }catch (UserUnAuthException.UnusualRequest e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVO<>(HttpStatus.BAD_REQUEST.value(),
                    "비정상적인 많은 요청으로 Token을 삭제합니다. 재발급 받으세요.", null));
        }catch (UserUnAuthException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    "유효 하지 않은 토큰", null));
        }catch (ElasticSearchException.NoIndexException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseVO<>(HttpStatus.CONFLICT.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //로그 저장 테스트
    @ApiOperation(value = "로그 저장 테스트")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "로그 요청이 비정상적으로 많을 때"),
                    @ApiResponse(code = 401, message = "토큰이 유효하지 않을 때"),
                    @ApiResponse(code = 409, message = "저장된 groundId가 없을 때"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @PostMapping("test")
    public ResponseEntity<ResponseVO<LogRes>> testLog(
            @ApiParam(value = "자동으로 저장 되는 로그", required = true) @RequestBody LogReq logReq,
            @ApiParam(value = "그라운드 ID", required = true) @RequestHeader String groundId) {
        // 문자열을 파일로 저장
        try{
            LocalDateTime localDateTime = LocalDateTime.now();
            userLogAccessService.count(groundId);
            elasticSearchLogService.save(groundId, ElasticSearchLog.builder().localDateTime(localDateTime).log(logReq.getLog()).build());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseVO<LogRes>(HttpStatus.CREATED.value(),
                    "로그 저장 완료", new LogRes(localDateTime, logReq.getLog())));
        }catch (UserUnAuthException.UnusualRequest e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVO<LogRes>(HttpStatus.BAD_REQUEST.value(),
                    "비정상적인 많은 요청으로 Token을 삭제합니다. 재발급 받으세요.", null));
        }catch (UserUnAuthException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<LogRes>(HttpStatus.UNAUTHORIZED.value(),
                    "유효 하지 않은 토큰", null));
        }catch (ElasticSearchException.NoIndexException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseVO<LogRes>(HttpStatus.CONFLICT.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<LogRes>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //인덱스 삭제
    @ApiOperation(value = "인덱스 삭제 API")
    @ApiResponses(
            value = {@ApiResponse(code = 401, message = "header의 groundId가 존재하지 않을 때"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @DeleteMapping("")
    public ResponseEntity<ResponseVO<?>> deleteLog(
            @ApiParam(value = "그라운드 ID", required = true) @RequestHeader String groundId) {
        // 문자열을 파일로 저장
        try{
            elasticSearchLogService.deleteIndex(groundId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    groundId + " 인덱스 삭제 완료", null));
        }catch (ElasticSearchException.NoContentException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (ElasticSearchException.NoIndexException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //전체 로그 불러오기
    @ApiOperation(value = "저장된 로그를 최신 순으로 30개 씩 가져오는 API")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @GetMapping("")
    public ResponseEntity<ResponseVO<PageableRes>> getSizeLogFile(
            @ApiParam(value = "그라운드 ID", required = true) @RequestHeader String groundId,
            @ApiParam(value = "페이지 번호 (1부터 시작)", defaultValue = "1") @RequestParam(name = "page", defaultValue = "1") int page){
        String result = "로그 불러오기 완료";
        try {
            Page<ElasticSearchLog> latestLogs = elasticSearchLogService.getLatestLogs(groundId, page - 1);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, new PageableRes(latestLogs.getNumber() + 1, latestLogs.getTotalPages(), latestLogs.getContent())));
        }catch (NoSuchIndexException e){
                log.error(e.getMessage());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                        "불러 올 로그가 없습니다", new PageableRes(0, -1, null)));
        }catch (ElasticSearchException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //인덱스별 정해진 키워드 가져오기
    @ApiOperation(value = "저장된 로그를 최신 순으로 요청하는 키워드로 가져오는 API")
    @ApiResponses(
            value = {@ApiResponse(code = 401, message = "header의 groundId가 존재하지 않을 때"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<ResponseVO<PageableRes>> getKeywordLogFile(
            @ApiParam(value = "그라운드 ID", required = true)  @RequestHeader String groundId,
            @ApiParam(value = "검색 할 키워드", required = true)  @PathVariable("keyword") String keyword,
            @ApiParam(value = "페이지 번호 (1부터 시작)", defaultValue = "1") @RequestParam(name = "page", defaultValue = "1") int page)
    {
        try {
            String result = "로그 키워드 " + keyword + " 불러오기 완료";
            Page<ElasticSearchLog> keywordtLogs = elasticSearchLogService.getKeywordtLogs(groundId, keyword, page-1);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, new PageableRes(keywordtLogs.getNumber()+1, keywordtLogs.getTotalPages(),keywordtLogs.getContent())));
        }catch (ElasticSearchException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (NoSuchIndexException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    "불러 올 로그가 없습니다", new PageableRes(0, -1, null)));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //인덱스별 정규표현식으로 가져오기
    @ApiOperation(value = "저장된 로그를 최신 순으로 요청하는 정규표현식으로 가져오는 API")
    @ApiResponses(
            value = {@ApiResponse(code = 401, message = "header의 groundId가 존재하지 않을 때"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @GetMapping("/regexp")
    public ResponseEntity<ResponseVO<PageableRes>> getRegexpLogFile(
            @ApiParam(value = "그라운드 ID", required = true)  @RequestHeader String groundId,
            @ApiParam(value = "검색 할 정규표현식", required = true)  @RequestParam String regexp,
            @ApiParam(value = "페이지 번호 (1부터 시작)", defaultValue = "1") @RequestParam(name = "page", defaultValue = "1") int page)
    {
        try{
            String result = "로그 정규표현식 " + regexp+ " 불러오기 완료";
            Page<ElasticSearchLog> regexptLogs = elasticSearchLogService.getRegexptLogs(groundId, regexp, page-1);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, new PageableRes(regexptLogs.getNumber()+1, regexptLogs.getTotalPages(),regexptLogs.getContent())));
        }catch (ElasticSearchException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (NoSuchIndexException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    "불러 올 로그가 없습니다", new PageableRes(0, -1, null)));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //인덱스별 시작 시간 끝 시간으로 가져오기
    @ApiOperation(value = "저장된 로그를 최신 순으로 요청하는 시작 시간과 끝 시간으로 가져오는 API")
    @ApiResponses(
            value = {@ApiResponse(code = 401, message = "header의 groundId가 존재하지 않을 때"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @GetMapping("/time")
    public ResponseEntity<ResponseVO<PageableRes>> getTimeLogFile(
            @ApiParam(value = "그라운드 ID", required = true) @RequestHeader String groundId,
            @ApiParam(value = "검색 시작 날짜와 시간 (yyyy-MM-dd'T'HH:mm:ss)", required = true) @RequestParam String startDateTime,
            @ApiParam(value = "검색 종료 날짜와 시간 (yyyy-MM-dd'T'HH:mm:ss)", required = true)  @RequestParam String endDateTime,
            @ApiParam(value = "페이지 번호 (1부터 시작)", defaultValue = "1") @RequestParam(name = "page", defaultValue = "1") int page)
    {
        try{
            String result = "로그 시간대 별로 불러오기 완료";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(startDateTime, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endDateTime, formatter);
            Page<ElasticSearchLog> timetLogs = elasticSearchLogService.getTimetLogs(groundId, startTime, endTime, page-1);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, new PageableRes(timetLogs.getNumber()+1, timetLogs.getTotalPages(),timetLogs.getContent())));
        }catch (ElasticSearchException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (NoSuchIndexException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    "불러 올 로그가 없습니다", new PageableRes(0, -1, null)));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //인덱스별 시작 시간 끝 시간으로 키워드로 가져오기
    @ApiOperation(value = "저장된 로그를 최신 순으로, 요청하는 시작 시간과 끝 시간 그리고 키워드로 가져오는 API")
    @ApiResponses(
            value = {@ApiResponse(code = 401, message = "header의 groundId가 존재하지 않을 때"),
                    @ApiResponse(code = 404, message = "저장된 로그가 없을 때"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @GetMapping("/timeandkeyword")
    public ResponseEntity<ResponseVO<PageableRes>> getTimeAndKeywordLogFile(
            @ApiParam(value = "그라운드 ID", required = true) @RequestHeader String groundId,
            @ApiParam(value = "검색 시작 날짜와 시간 (yyyy-MM-dd'T'HH:mm:ss)", required = true)  @RequestParam String startDateTime,
            @ApiParam(value = "검색 종료 날짜와 시간 (yyyy-MM-dd'T'HH:mm:ss)", required = true) @RequestParam String endDateTime,
            @ApiParam(value = "검색 키워드", required = true) @RequestParam String keyword,
            @ApiParam(value = "페이지 번호 (1부터 시작)", defaultValue = "1") @RequestParam(name = "page", defaultValue = "1") int page) {
        try {
            String result = "로그 시간대 별 및 키워드 " + keyword + "로 불러오기 완료";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(startDateTime, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endDateTime, formatter);
            Page<ElasticSearchLog> timeAndKeywordLogs = elasticSearchLogService.getTimeAndKeywordLogs(groundId, startTime, endTime, keyword, page-1);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, new PageableRes(timeAndKeywordLogs.getNumber()+1, timeAndKeywordLogs.getTotalPages(),timeAndKeywordLogs.getContent())));
        } catch (ElasticSearchException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        } catch (NoSuchIndexException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    "불러 올 로그가 없습니다", new PageableRes(0, -1, null)));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }
}
