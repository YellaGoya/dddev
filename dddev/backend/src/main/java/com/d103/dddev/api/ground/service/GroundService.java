package com.d103.dddev.api.ground.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.repository.repository.entity.Repository;
import com.d103.dddev.api.user.repository.entity.User;

public interface GroundService {
	Ground createGround(String groundName, User user, Repository repository) throws Exception;

	Optional<Ground> getGroundByRepoId(Integer repoId) throws Exception;
	Optional<Ground> getGroundInfo(Integer groundId) throws Exception;

	Map<String, Integer> getGroundFocusTime(Integer groundId, Integer sprintId) throws Exception;
	Map<String, Integer> getGroundActiveTime(Integer groundId, Integer sprintId) throws Exception;
	Map<String, Integer> getGroundTotalTime(Integer groundId, Integer sprintId) throws Exception;

	Map<String, Integer> getGroundFocusTimeCount(Integer groundId, Integer sprintId) throws Exception;
	Map<String, Integer> getGroundActiveTimeCount(Integer groundId, Integer sprintId) throws Exception;
	Map<String, Integer> getGroundTotalTimeCount(Integer groundId, Integer sprintId) throws Exception;

	Ground updateGroundInfo(Ground newGround, Ground ground) throws Exception;
	Ground updateGroundProfile(MultipartFile file, Ground ground) throws Exception;

	Ground deleteGroundProfile(Ground ground) throws Exception;
	void deleteMemberFromGround(Ground ground, User user) throws Exception;
	void deleteGround(Ground ground) throws Exception;
}
