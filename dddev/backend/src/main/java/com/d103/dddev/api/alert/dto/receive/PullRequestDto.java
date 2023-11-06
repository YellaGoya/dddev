package com.d103.dddev.api.alert.dto.receive;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PullRequestDto {
	private String url;
	private Integer id;
	private String nodeId;
	private Integer number;
	private String state;
	private Boolean locked;
	private String title;
	private PullRequestUserDto user;
	private String body;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private PullRequestTree head;
	private PullRequestTree base;
	private String authorAssociation;
	private Integer commits;	// 포함된 커밋 개수
	private Integer additions;
	private Integer deletions;
	private Integer changedFiles;	// 변경된 파일 개수
}
