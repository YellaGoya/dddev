package com.d103.dddev.api.repository.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.repository.repository.dto.RepositoryDto;

public interface RepositoryRepository extends JpaRepository<RepositoryDto, Integer> {

	Optional<RepositoryDto> findByRepoId(Integer repoId);
}
