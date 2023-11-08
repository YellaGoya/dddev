package com.d103.dddev.api.alert.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FcmResponseDto {
	/*
	* fcm 전송 후 응답
	*/
	private Long multicastId;
	private int success;
	private int failure;
	private int canonicalIds;
	private List<Map<String, String>> results;
}
