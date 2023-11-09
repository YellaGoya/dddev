package com.d103.dddev.api.repository.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.repository.repository.entity.Repository;

public interface RepositoryRepository extends JpaRepository<Repository, Integer> {

	Optional<Repository> findByRepoId(Integer repoId);
}
