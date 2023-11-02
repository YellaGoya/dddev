package com.dddev.log.controller;

import com.dddev.log.dto.ElasticSearchLog;
import com.dddev.log.dto.ResponseVO;
import com.dddev.log.dto.req.LogReq;
import com.dddev.log.dto.res.LogRes;
import com.dddev.log.exception.ElasticSearchException;
import com.dddev.log.service.ElasticSearchLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
@Slf4j
public class logController {

    private final ElasticSearchLogService elasticSearchLogService;

    //로그 저장
    @PostMapping("")
    public ResponseEntity<?> saveLog(HttpServletRequest request, @RequestBody LogReq log) {
        // 문자열을 파일로 저장
        String groupId = request.getHeader("group_id");
        try{
            LocalDateTime localDateTime = LocalDateTime.now();
            elasticSearchLogService.save(groupId, ElasticSearchLog.builder().localDateTime(localDateTime).log(log.getLog()).build());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseVO<>(HttpStatus.CREATED.value(),
                            "로그 저장 완료", new LogRes(localDateTime, log.getLog())));
        }catch (ElasticSearchException.NoContentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (ElasticSearchException.NoIndexException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }
    
    //인덱스별 정해진 특정 개수 만큼 가져오기
    @GetMapping("/size/{size}")
    public ResponseEntity<?> getSizeLogFile(HttpServletRequest request, @PathVariable("size") int size) {
        try{
            String result = "최근 로그 " + size + "개 불러오기 완료";
            List<ElasticSearchLog> latestLogs = elasticSearchLogService.getLatestLogs(request.getHeader("group_id"), size);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, latestLogs));
        }catch (ElasticSearchException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (NoSuchIndexException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    "잘못된 Group id 접근입니다.", null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //인덱스별 정해진 키워드 가져오기
    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<?> getKeywordLogFile(HttpServletRequest request, @PathVariable("keyword") String keyword) {
        try {
            String result = "로그 키워드 " + keyword + " 불러오기 완료";
            List<ElasticSearchLog> latestLogs = elasticSearchLogService.getKeywordtLogs(request.getHeader("group_id"), keyword);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, latestLogs));
        }catch (ElasticSearchException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (NoSuchIndexException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    "잘못된 Group id 접근입니다.", null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //인덱스별 정규표현식으로 가져오기
    @GetMapping("/regexp")
    public ResponseEntity<?> getRegexpLogFile(HttpServletRequest request, @RequestParam String regexp) {
        try{
            String result = "로그 정규표현식 " + regexp+ " 불러오기 완료";
            List<ElasticSearchLog> latestLogs = elasticSearchLogService.getRegexptLogs(request.getHeader("group_id"), regexp);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, latestLogs));
        }catch (ElasticSearchException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (NoSuchIndexException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    "잘못된 Group id 접근입니다.", null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //인덱스별 시작 시간 끝 시간으로 가져오기
    @GetMapping("/time")
    public ResponseEntity<?> getTimeLogFile(HttpServletRequest request, @RequestParam String startDateTime, @RequestParam String endDateTime) {
        try{
            String result = "로그 시간대 별로 불러오기 완료";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(startDateTime, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endDateTime, formatter);
            List<ElasticSearchLog> latestLogs = elasticSearchLogService.getTimetLogs(request.getHeader("group_id"), startTime, endTime);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, latestLogs));
        }catch (ElasticSearchException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (NoSuchIndexException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    "잘못된 Group id 접근입니다.", null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }
    //최근 로그 20줄 불러와서 분석하기
    @GetMapping("/analyze")
    public ResponseEntity<?> analyze(HttpServletRequest request) {
        try{
            String result = "최근 로그를 불러 분석 완료";
            String response = elasticSearchLogService.analyze(request.getHeader("group_id")).trim().replaceAll("n", "");
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, response));
        }catch (ElasticSearchException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (NoSuchIndexException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    "잘못된 Group id 접근입니다.", null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }
}
