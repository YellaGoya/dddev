package com.d103.dddev.api.alert.entity;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AlertDataEntity {

	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// private String id;

	private String title;
	private String content;

	private List<String> keywordList;

	private List<String> changedFileList;

	private List<String> commitMessageList;

	private String url;	// notification click event url

	private String branch;

	@CreationTimestamp
	private String sendingDate;

	private Integer creatorId;

	private Integer receiverId;

	private String alertType;

	private Boolean isRead;

	// private Integer success;
	//
	// private Integer failure;

}
