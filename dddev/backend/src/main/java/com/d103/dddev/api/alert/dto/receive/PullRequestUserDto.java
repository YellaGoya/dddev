package com.d103.dddev.api.alert.dto.receive;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PullRequestUserDto {
	private String login;
	private Integer id;
	private String nodeId;
}
