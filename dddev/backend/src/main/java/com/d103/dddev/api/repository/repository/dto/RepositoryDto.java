package com.d103.dddev.api.repository.repository.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;

import com.d103.dddev.api.user.repository.dto.UserDto;

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
public class RepositoryDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserDto userDto;

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

}
