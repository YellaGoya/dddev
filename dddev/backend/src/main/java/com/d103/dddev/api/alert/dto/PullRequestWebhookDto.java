package com.d103.dddev.api.alert.dto;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PullRequestWebhookDto {
	private Integer id;
	private String action;
	private Integer number;
	private PullRequestDto pullRequest;
	private RepositoryWebhookDto repository;
	private Map<String, String> sender;
}
