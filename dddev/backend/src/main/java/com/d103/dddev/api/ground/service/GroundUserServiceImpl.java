package com.d103.dddev.api.ground.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.d103.dddev.api.ground.repository.GroundUserRepository;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.user.repository.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroundUserServiceImpl implements GroundUserService {

	private final GroundUserRepository groundUserRepository;

	/**
	 * ground owner 설정
	 * */
	@Override
	public GroundUserDto updateGroundOwner(GroundDto groundDto, UserDto userDto) {
		log.info("service - updateGroundUser :: 그라운드 owner 업데이트 진입");
		GroundUserDto groundUserDto = GroundUserDto.builder()
			.groundDto(groundDto)
			.userDto(userDto)
			.isOwner(true)
			.build();

		return groundUserRepository.saveAndFlush(groundUserDto);
	}

	@Override
	public Boolean checkIsGroundOwner(Integer groundId, Integer userId) {
		log.info("service - checkIsGroundOwner :: 그라운드 오너 확인");
		Optional<GroundUserDto> groundUserDtoOptional = groundUserRepository.findByGroundDto_IdAndUserDto_IdAndIsOwnerIsTrue(
			groundId, userId);
		return groundUserDtoOptional.isPresent();
	}

	@Override
	public Boolean checkIsGroundMember(Integer groundId, Integer userId) {
		log.info("service - checkIsGroundMember :: 그라운드 멤버 확인 진입");
		Optional<GroundUserDto> groundUserDtoOptional = groundUserRepository.findByGroundDto_IdAndUserDto_Id(
			groundId, userId);

		return groundUserDtoOptional.isPresent();
	}

	@Override
	public List<GroundUserDto> getGroundUser(Integer groundId) throws Exception {
		return null;
	}
}
