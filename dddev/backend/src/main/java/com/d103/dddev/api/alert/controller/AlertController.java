package com.d103.dddev.api.alert.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d103.dddev.api.alert.dto.ReceiveWebhookDto;
import com.d103.dddev.api.alert.service.AlertServiceImpl;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/alert-service")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"알림 서비스 API"})
public class AlertController {
	private final AlertServiceImpl alertService;

	// 새 알림 생성
	@PostMapping("/create-webhook")
	public ResponseEntity<?> addCommitWebhook(@RequestHeader("Authorization") String header,
		@RequestBody Map<String, String> map) {
		try {
			alertService.addCommitWebhook(header, map.get("repoName"));
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	}

	// 깃허브 이벤트 수신 - push event
	@PostMapping("/receive-webhook")
	public ResponseEntity<?> receiveAlert(@RequestHeader(required = false) Map<String, Object> headerMap,
		@RequestBody(required = false) ReceiveWebhookDto receiveWebhookDto) {
		try {
			alertService.receiveWebhook(headerMap, receiveWebhookDto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	}


}
