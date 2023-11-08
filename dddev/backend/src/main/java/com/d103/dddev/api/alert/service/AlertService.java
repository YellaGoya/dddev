package com.d103.dddev.api.alert.service;

import java.util.List;
import java.util.Map;

import com.d103.dddev.api.alert.dto.CreateWebhookRequestDto;
import com.d103.dddev.api.alert.dto.PushWebhookDto;
import com.d103.dddev.api.alert.dto.UpdateAlertDto;
import com.d103.dddev.api.alert.dto.PullRequestWebhookDto;
import com.d103.dddev.api.alert.entity.AlertEntity;
import com.d103.dddev.api.user.repository.dto.UserDto;

public interface AlertService {
	void createAlert(String header, CreateWebhookRequestDto createWebhookRequestDto) throws Exception;

	void receivePushWebhook(Map<String, Object> headerMap, PushWebhookDto pushWebhookDto) throws Exception;

	void updateAlert(UpdateAlertDto updateAlertDto, Integer alertId) throws Exception;

	List<AlertEntity> alertList(UserDto userDto) throws Exception;

	void deleteAlert(UserDto userDto, Integer alertId) throws Exception;

	void receivePullRequestWebhook(Map<String, Object> headerMap, PullRequestWebhookDto pullRequestWebhookDto) throws Exception;
}
