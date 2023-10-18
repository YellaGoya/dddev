package com.d103.dddev.api.user.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.common.oauth2.service.Oauth2Service;
import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.d103.dddev.api.file.service.ProfileService;
import com.d103.dddev.api.user.repository.UserRepository;
import com.d103.dddev.api.user.repository.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ProfileService profileService;
	private final Oauth2Service oauth2Service;

	private final Integer DEFAULT_IMG_ID = 1;

	@Override
	public Optional<UserDto> getUserInfo(Integer github_id) throws Exception {
		log.info("getUserInfo :: 사용자 정보 조회");
		return userRepository.findBygithubId(github_id);
	}

	@Override
	public byte[] getProfile(UserDto userDto) throws Exception {
		log.info("getProfile :: 사용자 프로필 조회 진입");
		log.info("getProfile :: 사용자 프로필 dto 조회");
		ProfileDto profileDto = userDto.getProfileDto();

		return profileService.getProfileByPath(profileDto.getFilePath());
	}

	@Override
	public Boolean checkDupNickname(String newNickname, Integer userId) throws Exception {
		UserDto userDto = userRepository.findByIdNotAndNickname(userId, newNickname).orElseGet(() -> null);
		return userDto == null;
	}

	@Override
	public UserDto modifyNickname(String nickname, UserDto userDto) throws Exception {
		userDto.setNickname(nickname);
		return userRepository.save(userDto);
	}

	@Override
	public UserDto modifyProfile(MultipartFile file, UserDto userDto) throws Exception {
		// 기존 프로필
		ProfileDto prevProfile = userDto.getProfileDto();

		// 새 프로필 사진 서버/db에 저장
		ProfileDto newProfile = profileService.saveProfile(file);

		// 새 프로필 사진 userDto에 저장
		userDto.setProfileDto(newProfile);

		// 기존 프로필 사진 서버/db에서 삭제
		if(prevProfile != null && prevProfile.getId() != DEFAULT_IMG_ID) {
			profileService.deleteProfile(prevProfile);
		}

		return userRepository.save(userDto);
	}

	@Override
	public UserDto deleteProfile(UserDto userDto) throws Exception {
		// 사용자 프로필 dto
		ProfileDto profileDto = userDto.getProfileDto();

		// 기본 프로필 사진 받아오기
		ProfileDto defaultProfile = profileService.getProfileDto(DEFAULT_IMG_ID);

		userDto.setProfileDto(defaultProfile);

		// 프로필 사진 서버/db에서 삭제
		if(profileDto != null && profileDto.getId() != DEFAULT_IMG_ID) {
			profileService.deleteProfile(profileDto);
		}

		return userRepository.save(userDto);
	}

	@Override
	public void deleteUser(UserDto userDto) throws Exception {
		// 프로필 사진 받아오기
		ProfileDto profileDto = userDto.getProfileDto();


		// 사용자 db에서 삭제
		userRepository.delete(userDto);

		// 서버/db에서 프로필 사진 삭제
		if(profileDto != null && profileDto.getId() != DEFAULT_IMG_ID) {
			profileService.deleteProfile(profileDto);
		}
	}

	@Override
	public Map<String, Object> githubToken(String code) throws Exception {
		return oauth2Service.githubToken(code);
	}

	// TODO : 테스트해보기.. 확실하지않음.....
	@Override
	public Boolean unlink(String accessToken) throws Exception {
		return oauth2Service.unlink(accessToken);
	}

}
