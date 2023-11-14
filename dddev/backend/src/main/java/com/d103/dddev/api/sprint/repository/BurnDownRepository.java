package com.d103.dddev.api.sprint.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.d103.dddev.api.sprint.repository.entity.BurnDown;

public interface BurnDownRepository extends JpaRepository<BurnDown, Integer> {
	List<BurnDown> findBySprint_Id(Integer sprintId);

	@Transactional
	void deleteByGround_Id(Integer groundId);
}
