package com.d103.dddev.api.alert.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
@Data
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateWebhookResponseDto {
	Integer id; // 439840320
	List<String> events; // ["push", "pull request"]
	LocalDateTime createdAt;
}
