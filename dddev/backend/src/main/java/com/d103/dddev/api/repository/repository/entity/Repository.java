package com.d103.dddev.api.repository.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.user.repository.dto.UserDto;
import org.hibernate.annotations.DynamicInsert;

import com.d103.dddev.api.user.repository.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "repository")
@Getter
@Setter
@ToString
@Builder
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class Repository {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "repo_id")
	private Integer repoId;

	private String name;

	@Column(name = "is_private")
	private Boolean isPrivate;

	private Boolean fork;

	@Column(name = "default_branch")
	private String defaultBranch;

	@Column(name = "is_ground")
	private Boolean isGround;

	public RepositoryDto convertToDto() {
		return RepositoryDto.builder()
				.id(this.id)
				.repoId(this.repoId)
				.name(this.name)
				.isPrivate(this.isPrivate)
				.defaultBranch(this.defaultBranch)
				.isGround(this.isGround)
				.build();
	}

}
