package com.d103.dddev.api.ground.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.ground.repository.entity.Ground;

public interface GroundRepository extends JpaRepository<Ground, Integer> {
	Optional<Ground> findByRepository_Id(Integer repoId);
}
