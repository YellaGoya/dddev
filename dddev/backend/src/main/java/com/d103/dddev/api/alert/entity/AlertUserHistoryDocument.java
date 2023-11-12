package com.d103.dddev.api.alert.entity;

import java.util.List;

import com.d103.dddev.api.user.repository.dto.UserDto;

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
public class AlertUserHistoryDocument {
	private Integer githubId;	// 수신자 id
	private String id;	// commit SHA / pull request id
	private Boolean isRead;
	private List<String> keyword;
}
