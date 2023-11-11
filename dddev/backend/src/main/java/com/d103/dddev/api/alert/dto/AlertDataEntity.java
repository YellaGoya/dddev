package com.d103.dddev.api.alert.dto;

import java.util.List;

import javax.persistence.Entity;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// @Entity("alert_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AlertDataEntity {
	private String title;
	private String content;

	private List<String> keywordList;


	// 키워드가 포함된 파일 리스트
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

	private String gitRepoName;

	private String gitRepoId;

	private String groundId;
}
