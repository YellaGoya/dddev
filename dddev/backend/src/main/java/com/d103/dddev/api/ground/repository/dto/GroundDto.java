package com.d103.dddev.api.ground.repository.dto;

import com.d103.dddev.api.ground.repository.entity.Ground;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class GroundDto {
	private Boolean isOwner;
	private Ground ground;
}
