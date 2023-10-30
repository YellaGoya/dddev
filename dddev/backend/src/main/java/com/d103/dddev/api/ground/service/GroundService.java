package com.d103.dddev.api.ground.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.user.repository.dto.UserDto;

public interface GroundService {
	GroundDto createGround(String groundName, UserDto userDto, RepositoryDto repositoryDto) throws Exception;

	Optional<GroundDto> getGroundByRepoId(Integer repoId) throws Exception;
	Optional<GroundDto> getGroundInfo(Integer groundId) throws Exception;

	GroundDto modifyGroundName(GroundDto groundDto, String newName) throws Exception;
	GroundDto modifyGroundProfile(GroundDto groundDto, MultipartFile file) throws Exception;
	GroundDto modifyFocusTime(GroundDto groundDto, Integer focusTime) throws Exception;
	GroundDto modifyActiveTime(GroundDto groundDto, Integer activeTime) throws Exception;

	GroundDto deleteGroundProfile(GroundDto groundDto) throws Exception;
	void deleteGround(GroundDto groundDto) throws Exception;
}
