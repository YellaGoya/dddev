package com.d103.dddev.api.repository.service;

import java.util.List;
import java.util.Optional;

import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.user.repository.dto.UserDto;

public interface RepositoryService {
	List<RepositoryDto> getRepositoryListFromGithub(UserDto userDto) throws Exception;

	Optional<RepositoryDto> getRepository(Integer repoId);

	Optional<RepositoryDto> getAndUpdateRepository(Integer repoId, String repoName);

	RepositoryDto saveRepository(RepositoryDto repositoryDto);
}
