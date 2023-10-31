package com.d103.dddev.api.user.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.user.repository.dto.UserDto;

public interface UserService {
	Optional<UserDto> getUserInfo(Integer github_id) throws Exception;	// 유저 정보 조회
	byte[] getProfile(UserDto userDto) throws Exception;	// 유저 프로필 사진 조회
	String getPersonalAccessToken(UserDto userDto) throws Exception;
	Boolean checkDupNickname(String newNickname, Integer userId) throws Exception;	// 유저 닉네임 중복체크
	UserDto modifyNickname(String nickname, UserDto userDto) throws Exception;	// 유저 닉네임 수정
	UserDto modifyProfile(MultipartFile file, UserDto userDto) throws Exception;	// 유저 프로필 사진 수정
	UserDto modifyStatusMsg(String statusMsg, UserDto userDto) throws Exception;
	UserDto savePersonalAccessToken(String personalAccessToken, UserDto userDto) throws Exception;	// 유저 personal access token 수정
	UserDto deleteProfile(UserDto userDto) throws Exception;		// 유저 프로필 사진 삭제
	void deleteUser(UserDto userDto) throws Exception;	// 유저 탈퇴
	UserDto deleteStatusMsg(UserDto userDto);
	Map<String, String> githubToken(String code) throws Exception;
	Boolean unlink(String oauthAccessToken) throws Exception;

	void saveDeviceToken(UserDto userDto, String deviceToken) throws Exception;	// 유저가 알림 허용한 기기 토큰 등록
}
