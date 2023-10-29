package com.d103.dddev.api.alert.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
// @Entity(name = "webhookEntity")
public class WebhookEntity {

	private String webhookId;

	@CreationTimestamp
	private LocalDateTime created_date;

	private String type;
}
