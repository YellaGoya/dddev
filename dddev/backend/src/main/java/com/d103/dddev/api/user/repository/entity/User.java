package com.d103.dddev.api.user.repository.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.d103.dddev.api.common.oauth2.Role;
import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "user")
@Getter
@Setter
@DynamicInsert
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "profile_id")
	private ProfileDto profileDto;

	@Column(name = "last_ground_id")
	private Integer lastGroundId;

	@Column(name = "github_id")
	private Integer githubId;

	private String nickname;

	private String email;

	@JsonIgnore
	@Column(name = "github_name")
	private String githubName;

	@Column(name = "status_msg")
	private String statusMsg;

	@CreationTimestamp
	@JoinColumn(name = "create_time")
	private Date createTime;

	@JsonIgnore
	private Boolean valid;

	@JsonIgnore
	@Column(name = "personal_access_token")
	private String personalAccessToken;

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	private Role role;

	@ElementCollection(fetch = FetchType.EAGER)
	@JsonIgnore
	@Column(name = "device_token")
	private Set<String> deviceToken;
	
	public void updatePersonalAccessToken(String personalAccessToken) {
		this.personalAccessToken = personalAccessToken;
		this.role = Role.USER;
	}

}
