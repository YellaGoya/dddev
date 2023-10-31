package com.d103.dddev.api.user.repository.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.d103.dddev.api.common.oauth2.Role;
import com.d103.dddev.api.file.repository.dto.ProfileDto;

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

	@Column(name = "github_name")
	private String githubName;

	@Column(name = "status_msg")
	private String statusMsg;

	@CreationTimestamp
	@JoinColumn(name = "create_time")
	private Date createTime;

	private Boolean valid;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "personal_access_token")
	private String personalAccessToken;

	@Enumerated(EnumType.STRING)
	private Role role;

	public void updateRefreshToken(String updateRefreshToken){
		this.refreshToken = updateRefreshToken;
	}

}
