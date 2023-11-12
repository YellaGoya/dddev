package com.d103.dddev.api.alert.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class AlertHistoryDocument {

	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// private String id;

	private String title;
	private String content;


	// 찾아낸 키워드 리스트
	private List<String> keywordList;

	// 키워드 포함된 파일 리스트
	private List<String> changedFileList;

	// 키워드 포함된 커밋 메시지 리스트
	private List<String> commitMessageList;

//	private Map<String, Object> idKeywordMap;

	private String url;	// notification click event url

	private String branch;

	@CreationTimestamp
	private String sendingDate;

	private Integer creatorId;

	private Integer receiverId;

	private String alertType;

	private Boolean isRead;

	private String gitRepoName;

	private Integer gitRepoId;

}
