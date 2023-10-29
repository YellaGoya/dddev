package com.d103.dddev.api.ground.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.d103.dddev.api.ground.repository.GroundRepository;
import com.d103.dddev.api.ground.repository.GroundUserRepository;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.user.repository.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroundServiceImpl implements GroundService {

	private final GroundRepository groundRepository;
	private final GroundUserService groundUserService;

	/**
	 * 그라운드 생성
	 * 1. groundDto에 repository 연결
	 * 2. groundUserDto owner 설정
	 * */
	@Override
	public GroundDto createGround(GroundDto groundDto, UserDto userDto, RepositoryDto repositoryDto) throws Exception {
		log.info("service - createGround :: 그라운드 생성 진입");
		groundDto.setRepositoryDto(repositoryDto);
		groundDto = groundRepository.saveAndFlush(groundDto);
		groundUserService.updateGroundOwner(groundDto, userDto);
		return groundDto;
	}
}
