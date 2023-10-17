package com.d103.dddev.api.user.repository.dto;

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

	@Column(name = "github_id")
	private Integer githubId;

	private String nickname;

	@CreationTimestamp
	@JoinColumn(name = "create_time")
	private Date createTime;

	private Boolean valid;

	@JoinColumn(name = "refresh_token")
	private String refreshToken;

	public void updateRefreshToken(String updateRefreshToken){
		this.refreshToken = updateRefreshToken;
	}

}
