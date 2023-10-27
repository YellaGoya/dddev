package com.d103.dddev.api.ground.service;

import java.util.List;

import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.user.repository.dto.UserDto;

public interface GroundUserService {
	GroundUserDto updateGroundOwner(GroundDto groundDto, UserDto userDto);

	Boolean checkIsGroundOwner(Integer groundId, Integer userId);

	Boolean checkIsGroundMember(Integer groundId, Integer userId);

	List<GroundUserDto> getGroundUser(Integer groundId) throws Exception;
}
