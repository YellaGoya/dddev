package com.d103.dddev.api.alert.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.d103.dddev.api.user.repository.dto.UserDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "alert")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class AlertDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(insertable = false, updatable = false)
	private Integer id;

	private Integer webhookId;

	private String keyword;

	private Integer repositoryId;

	private String type;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserDto userDto;

	@CreationTimestamp
	private LocalDateTime createdDate;
}
