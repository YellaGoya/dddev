package com.d103.dddev.api.alert.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d103.dddev.api.alert.dto.CreateWebhookRequestDto;
import com.d103.dddev.api.alert.dto.PushWebhookDto;
import com.d103.dddev.api.alert.dto.UpdateAlertDto;
import com.d103.dddev.api.alert.dto.PullRequestWebhookDto;
import com.d103.dddev.api.alert.entity.AlertEntity;
import com.d103.dddev.api.alert.service.AlertServiceImpl;
import com.d103.dddev.api.common.ResponseDto;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.user.repository.entity.User;
import com.sun.jdi.request.DuplicateRequestException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/alert-service")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"알림 API"})
public class AlertController {
    private final AlertServiceImpl alertService;
    private final JwtService jwtService;

    // 새 알림 생성
    @PostMapping("/create-alert")
    @ApiOperation(value = "알림 생성", notes = "커밋(푸시) 알림을 생성하는 API")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "사용자 accessToken, 레포 id, 기기 토큰 오류"),
            @ApiResponse(code = 409, message = "알림 중복 생성 요청"),
            @ApiResponse(code = 500, message = "서버 오류")})
    public ResponseEntity<ResponseDto<String>> createAlert(@RequestHeader("Authorization") String header,
                                                           @RequestBody CreateWebhookRequestDto createWebhookRequestDto) {
        try {
            alertService.createAlert(header, createWebhookRequestDto);
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("알림 생성 성공")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.NOT_ACCEPTABLE.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
        } catch (DuplicateRequestException e) {
            log.error(e.getMessage());
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.CONFLICT.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.toString());
            log.error(e.getMessage());
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // @PostMapping(value = "/receive-webhook")
    // @ApiOperation(value = "hide endpoint", hidden = true)
    // public ResponseEntity<?> receiveWebhook(@RequestHeader(required = false) Map<String, Object> headerMap,
    // 	@RequestBody ReceiveWebhookDto receiveWebhookDto) {
    // 	try {
    // 		log.info("alert controller");
    // 		alertService.receiveWebhook(headerMap, receiveWebhookDto);
    // 		return new ResponseEntity<>(HttpStatus.OK);
    // 	} catch (Exception e) {
    // 		log.error(e.getMessage());
    // 		ResponseVO<String> responseVO = ResponseVO.<String>builder()
    // 			.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
    // 			.message(e.getMessage())
    // 			.build();
    // 		return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
    // 	}
    // }

    // 깃허브 이벤트 수신 - push event
    @PostMapping(value = "/push-webhook")
    @ApiOperation(value = "hide endpoint", hidden = true)
    public ResponseEntity<?> receivePushWebhook(@RequestHeader(required = false) Map<String, Object> headerMap,
                                                @RequestBody PushWebhookDto pushWebhookDto) {
        try {
            log.info("alert controller");
            alertService.receivePushWebhook(headerMap, pushWebhookDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 깃허브 이벤트 수신 - pull request event
    @PostMapping(value = "/pull-request-webhook")
    @ApiOperation(value = "hide endpoint", hidden = true)
    public ResponseEntity<?> receivePullRequestWebhook(@RequestHeader(required = false) Map<String, Object> headerMap,
                                                       @RequestBody PullRequestWebhookDto pullRequestWebhookDto) {
        try {
            log.info("pull-request-webhook controller");
            alertService.receivePullRequestWebhook(headerMap, pullRequestWebhookDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{alertId}")
    @ApiOperation(value = "알림 수정", notes = "커밋(푸시) 알림의 키워드를 수정하는 API")
    // @ApiImplicitParam(name = "map", paramType = "body", example = "{\n"
    // 	+ "  \"keyword\": [\n"
    // 	+ "    \"string\"\n"
    // 	+ "  ]\n"
    // 	+ "}")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "알림 id 오류")
    })
    public ResponseEntity<ResponseDto<String>> updateAlert(@RequestBody UpdateAlertDto updateAlertDto, @PathVariable(name = "alertId") Integer alertId) {
        try {
            alertService.updateAlert(updateAlertDto, alertId);
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("알림 수정 성공")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.NOT_ACCEPTABLE.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    @ApiOperation(value = "알림 조회", notes = "커밋(푸시) 알림을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "사용자 accessToken 오류")
    })
    public ResponseEntity<ResponseDto<List<AlertEntity>>> alertList(@RequestHeader String Authorization, HttpServletRequest request) {
        try {
            ModelAndView mav = (ModelAndView) request.getAttribute("modelAndView");
            User user = (User) mav.getModel().get("user");
            ResponseDto<List<AlertEntity>> responseDto = ResponseDto.<List<AlertEntity>>builder()
                    .code(HttpStatus.OK.value())
                    .message("알림 조회 성공")
                    .data(alertService.alertList(user))
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            ResponseDto<List<AlertEntity>> responseDto = ResponseDto.<List<AlertEntity>>builder()
                    .code(HttpStatus.NOT_ACCEPTABLE.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto<List<AlertEntity>> responseDto = ResponseDto.<List<AlertEntity>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{alertId}")
    @ApiOperation(value = "알림 삭제", notes = "커밋(푸시) 알림과 연결된 깃허브 웹훅을 삭제하는 API")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "사용자 accessToken, 깃허브 id 오류")
    })
    public ResponseEntity<ResponseDto<String>> deleteAlert(@RequestHeader String Authorization, @PathVariable(name = "alertId") Integer alertId, HttpServletRequest request) {
        try {
            ModelAndView mav = (ModelAndView) request.getAttribute("modelAndView");
            User user = (User) mav.getModel().get("user");
            alertService.deleteAlert(user, alertId);
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("알림 삭제 성공")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.NOT_ACCEPTABLE.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto<String> responseDto = ResponseDto.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
