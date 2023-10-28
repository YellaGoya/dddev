package com.d103.dddev.api.alert.dto;

import java.util.List;

import com.d103.dddev.api.user.repository.dto.UserDto;

public interface AlertUserDto {
	UserDto getUserDto();
	List<String> getKeyword();
}
