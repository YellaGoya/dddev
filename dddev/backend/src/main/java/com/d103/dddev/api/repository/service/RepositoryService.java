package com.d103.dddev.api.repository.service;

import java.util.List;
import java.util.Optional;

import com.d103.dddev.api.repository.repository.entity.Repository;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.user.repository.entity.User;

public interface RepositoryService {
	List<RepositoryDto> getRepositoryListFromGithub(User user) throws Exception;

	Optional<Repository> getRepository(Integer repoId);

	Optional<Repository> getAndUpdateRepository(Integer repoId, String repoName);

	Repository saveRepository(Repository repository);

	RepositoryDto updateIsGround(Repository repository, Boolean isGround) throws Exception;
}
