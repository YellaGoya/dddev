package com.d103.dddev.api.ground.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.ground.repository.entity.GroundUser;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.user.repository.entity.User;

public interface GroundUserService {
	List<Map<String, String>> findUserByEmail(Integer groundId, String email) throws Exception;

	List<GroundUserDto> inviteMemberToGround(Ground ground, User newMember) throws Exception;

	GroundUser updateGroundOwner(Ground ground, User user) throws Exception;

	Boolean checkIsGroundOwner(Integer groundId, Integer userId) throws Exception;

	Boolean checkIsGroundMember(Integer groundId, Integer userId) throws Exception;

	List<GroundUser> getGroundMembers(Integer groundId) throws Exception;

	List<GroundUserDto> getGroundMembersAsDto(Integer groundId) throws Exception;

	Optional<GroundUser> getGroundMember(Integer groundId, Integer userId) throws Exception;

	void deleteGroundUser(GroundUser groundUser) throws Exception;
}
