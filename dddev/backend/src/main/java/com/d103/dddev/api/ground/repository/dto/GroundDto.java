package com.d103.dddev.api.ground.repository.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "ground")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class GroundDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "repository_id")
	private RepositoryDto repositoryDto;

	@OneToOne
	@JoinColumn(name = "profile_id")
	private ProfileDto profileDto;

	private String name;

	@Column(name = "focus_time")
	private Integer focusTime;

	@Column(name = "active_time")
	private Integer activeTime;

	@CreationTimestamp
	@Column(name = "create_time")
	private Date createTime;

}
