package com.d103.dddev.api.sprint.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.sprint.repository.entity.BurnDown;

public interface BurnDownRepository extends JpaRepository<BurnDown, Integer> {
	List<BurnDown> findBySprint_Id(Integer sprintId);
}
