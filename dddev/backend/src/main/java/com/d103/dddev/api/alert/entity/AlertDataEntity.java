package com.d103.dddev.api.alert.entity;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// @Entity(name = "alert_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AlertDataEntity {
	// @Id
	// // @GeneratedValue(strategy = GenerationType.IDENTITY)
	// private String id;

	private String title;
	private String content;

	@Column(name = "sending_date")
	@CreationTimestamp
	private String sendingDate;

	@Column(name = "creator_id")
	private Integer creatorId;

	@Column(name = "receiver_id")
	private Integer receiverId;

	@Column(name = "alert_type")
	private String alertType;

}
