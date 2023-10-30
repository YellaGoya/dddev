package com.dddev.log.controller;

import com.dddev.log.dto.ElasticSearchLog;
import com.dddev.log.dto.ResponseVO;
import com.dddev.log.dto.req.LogReq;
import com.dddev.log.dto.req.RegExpReq;
import com.dddev.log.dto.req.TimeReq;
import com.dddev.log.dto.res.LogRes;
import com.dddev.log.exception.ElasticSearchException;
import com.dddev.log.service.ElasticSearchLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class logController {

    private final ElasticSearchLogService elasticSearchLogService;

    //로그 저장
    @PostMapping("/test")
    public ResponseEntity<?> test(HttpServletRequest request, @RequestBody LogReq log) {
        // 문자열을 파일로 저장
        String groupId = request.getHeader("group_id");
        try{
            LocalDateTime localDateTime = LocalDateTime.now();
            elasticSearchLogService.save(groupId, ElasticSearchLog.builder().localDateTime(localDateTime).log(log.getLog()).build());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseVO<>(HttpStatus.CREATED.value(),
                    "로그 저장 완료", new LogRes(localDateTime, log.getLog())));
        }catch (ElasticSearchException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류", null));
        }
    }
    
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
        }catch (ElasticSearchException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류", null));
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류", null));
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류", null));
        }
    }

    //인덱스별 정규표현식으로 가져오기
    @GetMapping("/regexp")
    public ResponseEntity<?> getRegexpLogFile(HttpServletRequest request, @RequestBody RegExpReq regexp) {
        try{
            String result = "로그 정규표현식 " + regexp.getRegExp()+ " 불러오기 완료";
            List<ElasticSearchLog> latestLogs = elasticSearchLogService.getRegexptLogs(request.getHeader("group_id"), regexp.getRegExp());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, latestLogs));
        }catch (ElasticSearchException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류", null));
        }
    }

    //인덱스별 시작 시간 끝 시간으로 가져오기
    @GetMapping("/time")
    public ResponseEntity<?> getTimeLogFile(HttpServletRequest request, @RequestBody TimeReq timeReq) {
        try{
            String result = "로그 시간대 별로 불러오기 완료";
            List<ElasticSearchLog> latestLogs = elasticSearchLogService.getTimetLogs(request.getHeader("group_id"), timeReq.getStartDateTime(), timeReq.getEndDateTime());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, latestLogs));
        }catch (ElasticSearchException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류", null));
        }
    }

//    //로그 요청
//    @GetMapping("")
//    public ResponseEntity<?> getLogFile(HttpServletRequest request) {
//        // 파일 전송
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
//        parts.add("file", new FileSystemResource("파일명.txt"));
//
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity("URL", requestEntity, String.class);
//
//        // 전송 결과 확인
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            return "파일 저장 및 전송 완료";
//        } else {
//            return "파일 전송 실패";
//        }
//    }
}
