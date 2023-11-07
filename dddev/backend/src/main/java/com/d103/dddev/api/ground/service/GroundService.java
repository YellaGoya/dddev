package com.d103.dddev.api.ground.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.user.repository.dto.UserDto;

public interface GroundService {
	GroundDto createGround(String groundName, UserDto userDto, RepositoryDto repositoryDto) throws Exception;

	Optional<GroundDto> getGroundByRepoId(Integer repoId) throws Exception;
	Optional<GroundDto> getGroundInfo(Integer groundId) throws Exception;

	Map<String, Integer> getGroundFocusTime(Integer groundId, Integer sprintId) throws Exception;
	Map<String, Integer> getGroundActiveTime(Integer groundId, Integer sprintId) throws Exception;

	GroundDto updateGroundInfo(GroundDto newGroundDto, GroundDto groundDto) throws Exception;
	GroundDto updateGroundProfile(MultipartFile file, GroundDto groundDto) throws Exception;

	GroundDto deleteGroundProfile(GroundDto groundDto) throws Exception;
	void deleteMemberFromGround(GroundDto groundDto, UserDto userDto) throws Exception;
	void deleteGround(GroundDto groundDto) throws Exception;
}
