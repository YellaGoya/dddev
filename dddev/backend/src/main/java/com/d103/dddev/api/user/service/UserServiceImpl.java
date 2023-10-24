package com.d103.dddev.api.user.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.common.oauth2.Role;
import com.d103.dddev.api.common.oauth2.service.Oauth2Service;
import com.d103.dddev.api.common.oauth2.utils.AesUtil;
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

	private final AesUtil aesUtil;

	private final Integer DEFAULT_IMG_ID = 1;

	@Override
	public Optional<UserDto> getUserInfo(Integer github_id) throws Exception {
		log.info("service - getUserInfo :: 사용자 정보 조회");
		return userRepository.findByGithubId(github_id);
	}

	@Override
	public byte[] getProfile(UserDto userDto) throws Exception {
		log.info("service - getProfile :: 사용자 프로필 조회 진입");
		ProfileDto profileDto = userDto.getProfileDto();

		return profileService.getProfileByPath(profileDto.getFilePath());
	}

	@Override
	public String getPersonalAccessToken(UserDto userDto) throws Exception {
		log.info("service - getPersonalAccessToken :: 사용자 personal access token 조회 진입");
		return decryptPersonalAccessToken(userDto.getPersonalAccessToken());
	}

	@Override
	public Boolean checkDupNickname(String newNickname, Integer userId) throws Exception {
		log.info("service - checkDupNickname :: 사용자 닉네임 중복 체크 진입");
		UserDto userDto = userRepository.findByIdNotAndNickname(userId, newNickname).orElseGet(() -> null);
		return userDto == null;
	}

	@Override
	public UserDto modifyNickname(String nickname, UserDto userDto) throws Exception {
		log.info("service - modifyNickname :: 사용자 닉네임 수정 진입");
		userDto.setNickname(nickname);
		return userRepository.saveAndFlush(userDto);
	}

	@Override
	public UserDto modifyProfile(MultipartFile file, UserDto userDto) throws Exception {
		log.info("service - modifyProfile :: 사용자 프로필 사진 수정 진입");
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

		return userRepository.saveAndFlush(userDto);
	}

	@Override
	public UserDto modifyStatusMsg(String statusMsg, UserDto userDto) throws Exception {
		log.info("service - modifyStatusMsg :: 사용자 상태 메시지 수정 진입");
		userDto.setStatusMsg(statusMsg);
		return userRepository.saveAndFlush(userDto);
	}

	@Override
	public UserDto savePersonalAccessToken(String newPersonalAccessToken, UserDto userDto) throws Exception {
		log.info("service - savePersonalAccessToken :: 사용자 personal access token 저장 진입");
		String encrypted = encryptPersonalAccessToken(newPersonalAccessToken);
		userDto.setPersonalAccessToken(encrypted);
		userDto.setRole(Role.USER);
		return userRepository.saveAndFlush(userDto);
	}

	@Override
	public UserDto deleteProfile(UserDto userDto) throws Exception {
		log.info("service - deleteProfile :: 사용자 프로필 사진 삭제 진입");
		// 사용자 프로필 dto
		ProfileDto profileDto = userDto.getProfileDto();

		// 기본 프로필 사진 받아오기
		ProfileDto defaultProfile = profileService.getProfileDto(DEFAULT_IMG_ID);

		userDto.setProfileDto(defaultProfile);

		// 프로필 사진 서버/db에서 삭제
		if(profileDto != null && profileDto.getId() != DEFAULT_IMG_ID) {
			profileService.deleteProfile(profileDto);
		}

		return userRepository.saveAndFlush(userDto);
	}

	@Override
	public void deleteUser(UserDto userDto) throws Exception {
		log.info("service - deleteUser :: 사용자 db/서버 삭제 진입");
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
	public UserDto deleteStatusMsg(UserDto userDto) {
		userDto.setStatusMsg(null);
		return userRepository.saveAndFlush(userDto);
	}

	@Override
	public Map<String, String> githubToken(String code) throws Exception {
		return oauth2Service.githubToken(code);
	}

	@Override
	public Boolean unlink(String oauthAccessToken) throws Exception {
		return oauth2Service.unlink(oauthAccessToken);
	}

	public String encryptPersonalAccessToken(String personalAccessToken) throws Exception {
		return aesUtil.aes256Encrypt(personalAccessToken);
	}

	public String decryptPersonalAccessToken(String personalAccessToken) throws Exception {
		return aesUtil.aes256Decrypt(personalAccessToken);
	}
}
