package com.d103.dddev.api.alert.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PushWebhookDto {

	String ref;		// "refs/heads/main"
	String before;	// "8df4c8d2fcea338fbd44a79ba9e6954ec82306a9"
	String after;	// "1be5342d1cc8d8e0f39edbaeb924d0b450abde83"

	RepositoryWebhookDto repository;

	String compare;	// "https://github.com/gayun0303/webhook-test/compare/8df4c8d2fcea...1be5342d1cc8"

	Map<String, String> pusher;

	Map<String, String> sender;

	List<CommitDataDto> commits;

	// CommitDataDto headCommit;


}
