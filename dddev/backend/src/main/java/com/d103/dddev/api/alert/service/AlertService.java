package com.d103.dddev.api.alert.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.d103.dddev.api.alert.dto.CreateWebhookRequestDto;
import com.d103.dddev.api.alert.dto.ReceiveWebhookDto;
import com.d103.dddev.api.alert.entity.AlertEntity;
import com.d103.dddev.api.user.repository.dto.UserDto;

public interface AlertService {
	void addCommitWebhook(String header, CreateWebhookRequestDto createWebhookRequestDto) throws Exception;

	void receiveWebhook(Map<String, Object> headerMap, ReceiveWebhookDto receiveWebhookDto) throws Exception;

	void updateAlert(List<String> keywordList, Integer alertId) throws Exception;

	List<AlertEntity> alertList(UserDto userDto) throws Exception;

	void deleteAlert(UserDto userDto, Integer alertId) throws Exception;
}
