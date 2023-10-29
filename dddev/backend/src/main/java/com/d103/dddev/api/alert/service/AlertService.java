package com.d103.dddev.api.alert.service;

import java.util.Map;

import com.d103.dddev.api.alert.dto.ReceiveWebhookDto;

public interface AlertService {
	void addCommitWebhook(String header, String repoName) throws Exception;

	void receiveWebhook(Map<String, Object> headerMap, ReceiveWebhookDto receiveWebhookDto) throws Exception;
}
