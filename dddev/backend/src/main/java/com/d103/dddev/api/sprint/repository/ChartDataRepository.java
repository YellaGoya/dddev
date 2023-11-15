package com.d103.dddev.api.sprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.sprint.repository.entity.ChartData;

public interface ChartDataRepository extends JpaRepository<ChartData, Integer> {

}
