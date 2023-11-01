package com.d103.dddev.api.alert.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.d103.dddev.api.alert.dto.ReceiveWebhookDto;
import com.d103.dddev.api.alert.service.AlertServiceImpl;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.user.repository.dto.UserDto;
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
	private final JwtService jwtService;

	// 새 알림 생성
	@PostMapping("/create-webhook")
	public ResponseEntity<?> createCommitWebhook(@RequestHeader("Authorization") String header,
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

	@PutMapping("/commit/{alertId}")
	public ResponseEntity<?> updateAlert(@RequestBody Map<String, List<String>> map, @PathVariable(name = "alertId") Integer alertId) {
		try {
			alertService.updateAlert(map.get("keyword"), alertId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	}

	@GetMapping("/commit")
	public ResponseEntity<?> alertList(@RequestHeader String Authorization) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));
			return new ResponseEntity<>(alertService.alertList(userDto), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	}

	@DeleteMapping("/commit/{alertId}")
	public ResponseEntity<?> deleteAlert(@RequestHeader String Authorization, @PathVariable(name = "alertId") Integer alertId) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));
			alertService.deleteAlert(userDto, alertId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	}






}
