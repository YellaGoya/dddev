package com.dddev.log.controller;

import com.dddev.log.dto.ResponseVO;
import com.dddev.log.service.UserLogAccessService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"로그 관련 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
@Slf4j
public class RedisController {

    private final UserLogAccessService userLogAccessService;

    //로그 요청 시 토큰 저장
    @ApiOperation(value = "Redis 테스트")
    @PostMapping("/auth")
    public ResponseEntity<?> userAuthSave(
            @ApiParam(value = "레디스 테스트", required = true) @RequestHeader String ground_id) {
        try{
            userLogAccessService.count(ground_id);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseVO<>(HttpStatus.CREATED.value(),
                    "토큰 저장 완료", null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }
}