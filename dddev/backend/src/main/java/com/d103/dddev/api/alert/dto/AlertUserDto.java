package com.d103.dddev.api.alert.dto;

import java.util.List;

import com.d103.dddev.api.user.repository.dto.UserDto;

import lombok.Getter;
import lombok.Setter;

public interface AlertUserDto {
	/*
	* 알림이 연결된 repository, 알림 타입으로 검색
	* userDto, 키워드 찾기
	*/
	UserDto getUserDto();
	List<String> getKeyword();
}
