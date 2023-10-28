package com.d103.dddev.api.alert.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.alert.dto.AlertUserDto;
import com.d103.dddev.api.alert.entity.AlertDto;

public interface AlertRepository extends JpaRepository<AlertDto, Integer> {
	List<AlertDto> findAllByRepositoryIdAndType(Integer repositoryId, String type);
	Optional<AlertDto> findByUserDto_IdAndRepositoryIdAndType(Integer userId, Integer repositoryId, String type);

	List<AlertUserDto> findByRepositoryIdAndType(Integer repositoryId, String type);
}
