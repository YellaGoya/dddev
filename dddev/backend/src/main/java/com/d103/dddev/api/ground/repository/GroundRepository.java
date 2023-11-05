package com.d103.dddev.api.ground.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.ground.repository.dto.GroundDto;

public interface GroundRepository extends JpaRepository<GroundDto, Integer> {
	Optional<GroundDto> findByRepositoryDto_Id(Integer repoId);
}
