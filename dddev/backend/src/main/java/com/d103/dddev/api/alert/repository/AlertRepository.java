package com.d103.dddev.api.alert.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.d103.dddev.api.alert.dto.AlertUserDto;
import com.d103.dddev.api.alert.entity.AlertEntity;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, Integer> {
	List<AlertEntity> findAllByRepositoryIdAndType(Integer repositoryId, String type);
	Optional<AlertEntity> findByUserDto_IdAndRepositoryIdAndType(Integer userId, Integer repositoryId, String type);

	List<AlertUserDto> findByRepositoryIdAndType(Integer repositoryId, String type);
}
