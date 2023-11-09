package com.d103.dddev.api.ground.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import org.hibernate.annotations.DynamicInsert;

import com.d103.dddev.api.user.repository.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "ground_user")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@DynamicInsert
@AllArgsConstructor
public class GroundUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "ground_id")
	private Ground ground;

	@Column(name = "is_owner")
	private Boolean isOwner;

	public GroundDto convertToGroundDto() {
		return GroundDto.builder()
				.isOwner(this.isOwner)
				.ground(this.ground)
				.build();
	}

	public GroundUserDto convertToGroundUserDto(){
		return GroundUserDto.builder()
				.isOwner(this.isOwner)
				.userId(this.user.getId())
				.profileDto(this.user.getProfileDto())
				.githubId(this.user.getGithubId())
				.nickname(this.user.getNickname())
				.statusMsg(this.user.getStatusMsg())
				.build();
	}
}
