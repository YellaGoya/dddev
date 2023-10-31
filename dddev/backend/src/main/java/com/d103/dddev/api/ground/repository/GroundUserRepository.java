package com.d103.dddev.api.ground.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.ground.repository.dto.GroundUserDto;

public interface GroundUserRepository extends JpaRepository<GroundUserDto, Integer> {
	List<GroundUserDto> findByGroundDto_Id(Integer groundId);

	List<GroundUserDto> findByUserDto_Id(Integer userId);

	Optional<GroundUserDto> findByGroundDto_IdAndUserDto_IdAndIsOwnerIsTrue(Integer groundId, Integer userId);

	Optional<GroundUserDto> findByGroundDto_IdAndUserDto_Id(Integer groundId, Integer userId);
}
