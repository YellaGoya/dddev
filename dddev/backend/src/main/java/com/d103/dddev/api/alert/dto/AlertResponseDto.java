package com.d103.dddev.api.alert.dto;

import java.util.List;

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
public class AlertResponseDto {
	Integer id;
	List<String> keyword;
	String groundName;
	Integer pushId;
	Integer pullRequestId;
	Integer userId;
}
