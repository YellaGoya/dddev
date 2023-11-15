package com.d103.dddev.api.ground.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.ground.repository.entity.LogEnv;

public interface LogEnvRepository extends JpaRepository<LogEnv, Integer> {
	List<LogEnv> findByGround_Id(Integer groundId);
}
