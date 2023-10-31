package com.d103.dddev.api.alert.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d103.dddev.api.alert.dto.CreateWebhookRequestDto;
import com.d103.dddev.api.alert.dto.ReceiveWebhookDto;
import com.d103.dddev.api.alert.service.AlertServiceImpl;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		@RequestBody CreateWebhookRequestDto createWebhookRequestDto) {
		try {
			alertService.addCommitWebhook(header, createWebhookRequestDto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	}

	// 깃허브 이벤트 수신 - push event
	@PostMapping(value = "/receive-webhook")
	public ResponseEntity<?> receiveWebhook(@RequestHeader(required = false) Map<String, Object> headerMap,
		@RequestBody ReceiveWebhookDto receiveWebhookDto) {
		try {
			log.info("alert controller");
			alertService.receiveWebhook(headerMap, receiveWebhookDto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	}

	@PutMapping("/{alertId}")
	public ResponseEntity<?> updateAlert(@RequestBody Map<String, List<String>> map, @PathVariable(name = "alertId") Integer alertId) {
		try {
			alertService.updateAlert(map.get("keyword"), alertId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	}


}
