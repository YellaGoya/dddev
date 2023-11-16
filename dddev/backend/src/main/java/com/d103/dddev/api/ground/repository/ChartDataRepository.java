package com.d103.dddev.api.ground.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.ground.repository.entity.ChartData;
import com.d103.dddev.api.sprint.TimeType;

public interface ChartDataRepository extends JpaRepository<ChartData, Integer> {
	List<ChartData> findByGround_IdAndTimeType(Integer groundId, TimeType timeType);
}
