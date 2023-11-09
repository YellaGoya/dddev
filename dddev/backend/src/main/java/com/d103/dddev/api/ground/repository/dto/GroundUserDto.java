package com.d103.dddev.api.ground.repository.dto;

import com.d103.dddev.api.file.repository.dto.ProfileDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroundUserDto {
	private Boolean isOwner;
	private Integer userId;
	private ProfileDto profileDto;
	private Integer githubId;
	private String nickname;
	private String statusMsg;
}
