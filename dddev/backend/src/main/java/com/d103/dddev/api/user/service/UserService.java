package com.d103.dddev.api.user.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.d103.dddev.api.user.repository.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.ground.repository.entity.GroundUser;
import com.d103.dddev.api.user.repository.entity.User;

public interface UserService {
	Optional<UserDto> getUserDto(Integer github_id) throws Exception;	// 유저 정보 조회
	Optional<UserDto> getUserDtoWithName(String userName) throws Exception;
	Optional<User> getUserEntity(Integer github_id)throws Exception;
	byte[] getProfile(User user) throws Exception;	// 유저 프로필 사진 조회
	List<GroundUser> getGroundList(User user) throws Exception;
	String getPersonalAccessToken(User user) throws Exception;
	Boolean checkDupNickname(String newNickname, Integer userId) throws Exception;	// 유저 닉네임 중복체크

	UserDto updateUserInfo(UserDto newUserDto, User user) throws Exception;	// 유저 닉네임 수정
	UserDto updateProfile(MultipartFile file, User user) throws Exception;	// 유저 프로필 사진 수정
	UserDto updateLastVisitedGround(Integer lastGroundId, User user) throws Exception;
	UserDto savePersonalAccessToken(String personalAccessToken, User user) throws Exception;	// 유저 personal access token 수정

	UserDto deleteProfile(User user) throws Exception;		// 유저 프로필 사진 삭제
	void deleteUser(User user) throws Exception;	// 유저 탈퇴
	UserDto deleteStatusMsg(User user);
	Map<String, String> githubToken(String code) throws Exception;
	Boolean unlink(String oauthAccessToken) throws Exception;

	void saveDeviceToken(User user, String deviceToken) throws Exception;	// 유저가 알림 허용한 기기 토큰 등록

	void deleteDeviceToken(User user, String deviceToken) throws Exception;	// 유저가 로그아웃 할 경우 기기 토큰 삭제
}
