package com.d103.dddev.api.repository.repository.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryVO {
	private Integer id;
	private Integer repoId;
	private String name;
	private Boolean isPrivate;
	private String defaultBranch;
	private Boolean isGround;
}
