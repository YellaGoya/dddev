package com.d103.dddev.api.alert.dto;

import java.time.LocalDateTime;

import com.d103.dddev.api.user.repository.dto.UserDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WebhookDto {
	String id;
	String type;
	String keyword;
	UserDto userDto;
	LocalDateTime updatedAt;
	LocalDateTime createdAt;
}
