package com.d103.dddev.api.ground.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.ground.repository.vo.GroundUserVO;
import com.d103.dddev.api.user.repository.dto.UserDto;

public interface GroundUserService {
	List<Map<String, String>> findUserByEmail(Integer groundId, String email) throws Exception;

	List<GroundUserVO> inviteMemberToGround(GroundDto groundDto, UserDto newMember) throws Exception;

	GroundUserDto updateGroundOwner(GroundDto groundDto, UserDto userDto) throws Exception;

	Boolean checkIsGroundOwner(Integer groundId, Integer userId) throws Exception;

	Boolean checkIsGroundMember(Integer groundId, Integer userId) throws Exception;

	List<GroundUserDto> getGroundMembers(Integer groundId) throws Exception;

	List<GroundUserVO> getGroundMembersAsVO(Integer groundId) throws Exception;

	Optional<GroundUserDto> getGroundMember(Integer groundId, Integer userId) throws Exception;

	void deleteGroundUser(GroundUserDto groundUserDto) throws Exception;
}
