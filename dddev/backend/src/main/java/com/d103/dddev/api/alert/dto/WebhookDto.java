package com.d103.dddev.api.alert.dto;

import java.time.LocalDateTime;

import com.d103.dddev.api.user.repository.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WebhookDto {
	String id;
	String type;
	String keyword;
	User user;
	LocalDateTime updatedAt;
	LocalDateTime createdAt;
}
