package com.d103.dddev.api.alert.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RepositoryWebhookDto {
	/*
	* 웹훅 응답 안에 있는 repository
	*/
	private Integer id;
	private String name;
	private String fullName;
	private String visibility;
}
