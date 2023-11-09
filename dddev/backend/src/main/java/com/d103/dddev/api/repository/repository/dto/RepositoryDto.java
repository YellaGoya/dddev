package com.d103.dddev.api.repository.repository.dto;

import com.d103.dddev.api.repository.repository.entity.Repository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryDto {
	private Integer id;
	private Integer repoId;
	private String name;
	private Boolean isPrivate;
	private String defaultBranch;
	private Boolean isGround;

	public Repository convertToEntity(){
		return Repository.builder()
				.id(this.id)
				.repoId(this.repoId)
				.name(this.name)
				.isPrivate(this.isPrivate)
				.defaultBranch(this.defaultBranch)
				.isGround(this.isGround)
				.build();
	}
}
