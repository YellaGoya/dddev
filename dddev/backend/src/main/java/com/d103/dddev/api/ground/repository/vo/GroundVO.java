package com.d103.dddev.api.ground.repository.vo;

import com.d103.dddev.api.ground.repository.dto.GroundDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class GroundVO {
	private Boolean isOwner;
	private GroundDto groundDto;
}
