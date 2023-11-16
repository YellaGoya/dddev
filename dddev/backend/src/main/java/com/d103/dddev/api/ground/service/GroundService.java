package com.d103.dddev.api.ground.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.ground.repository.dto.LogEnvDto;
import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.ground.repository.entity.LogEnv;
import com.d103.dddev.api.repository.repository.entity.Repository;
import com.d103.dddev.api.user.repository.entity.User;

public interface GroundService {
	Ground createGround(String groundName, User user, Repository repository) throws Exception;

	Optional<Ground> getGroundByRepoId(Integer repoId) throws Exception;
	Optional<Ground> getGroundInfo(Integer groundId) throws Exception;

	// 로그 env
	List<LogEnvDto> getLogEnv(Integer groundId) throws Exception;
	List<LogEnvDto> createLogEnv(Ground ground, LogEnvDto newLogEnv) throws Exception;
	void deleteLogEnv(Integer logEnvId) throws Exception;


	Map<LocalDate, Integer> getSprintBurnDownChart(Integer sprintId) throws Exception;
	Map<String, Integer> getSprintFocusTime(Integer sprintId) throws Exception;
	Map<String, Integer> getSprintActiveTime(Integer sprintId) throws Exception;
	Map<String, Integer> getSprintTotalTime(Integer sprintId) throws Exception;

	Map<String, Integer> getSprintFocusTimeCount(Integer sprintId) throws Exception;
	Map<String, Integer> getSprintActiveTimeCount(Integer sprintId) throws Exception;
	Map<String, Integer> getSprintTotalTimeCount(Integer sprintId) throws Exception;

	Double getGroundFocusTimeDoneAvg(Integer groundId) throws Exception;
	Double getGroundActiveTimeDoneAvg(Integer groundId) throws Exception;
	Double getGroundTotalTimeDoneAvg(Integer groundId) throws Exception;

	Ground updateGroundInfo(Ground newGround, Ground ground) throws Exception;
	Ground updateGroundProfile(MultipartFile file, Ground ground) throws Exception;

	void deleteBurnDownChart(Integer groundId) throws Exception;
	Ground deleteGroundProfile(Ground ground) throws Exception;
	void deleteMemberFromGround(Ground ground, User user) throws Exception;
	void deleteGround(User user, Ground ground) throws Exception;
}
