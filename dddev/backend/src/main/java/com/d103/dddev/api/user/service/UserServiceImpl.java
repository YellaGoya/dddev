package com.d103.dddev.api.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.common.oauth2.Role;
import com.d103.dddev.api.common.oauth2.service.Oauth2Service;
import com.d103.dddev.api.common.oauth2.utils.AesUtil;
import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.d103.dddev.api.file.service.ProfileService;
import com.d103.dddev.api.ground.repository.GroundUserRepository;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.user.repository.UserRepository;
import com.d103.dddev.api.user.repository.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final GroundUserRepository groundUserRepository;
	private final ProfileService profileService;
	private final Oauth2Service oauth2Service;

	private final AesUtil aesUtil;

	// private final Integer DEFAULT_USER_IMG_ID = 1;
	private final String API_URL = "https://api.github.com";
	private final String USER_INFO_REQUEST_TOKEN = "token ";

	private final String SERVERKEY = "AAAA-JR50zk:APA91bF4mudANm2i7fUAWnk9SGV2C4wvnqbal6AsiIwG9P8sxiQxNBU7eaEkPZ6RVQpJ8M5POblq43u94Wpja7qXL01GKE4yjAk9pE8a-yDYbdB98_LJ1lOBftUVoloFaCY6IAE7MuDs";

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
	public List<GroundUserDto> getGroundList(UserDto userDto) throws Exception {
		log.info("service - getGroundList :: 사용자가 가입된 그라운드 리스트 반환");
		return groundUserRepository.findByUserDto_Id(userDto.getId());
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
	public UserDto updateUserInfo(UserDto newUserDto, UserDto userDto) throws Exception {
		log.info("service - updateNickname :: 사용자 정보 수정 진입");
		userDto.setNickname(newUserDto.getNickname());
		userDto.setStatusMsg(newUserDto.getStatusMsg());
		return userRepository.saveAndFlush(userDto);
	}

	@Override
	public UserDto updateProfile(MultipartFile file, UserDto userDto) throws Exception {
		log.info("service - modifyProfile :: 사용자 프로필 사진 수정 진입");
		// 기존 프로필
		ProfileDto prevProfile = userDto.getProfileDto();

		// 새 프로필 사진 서버/db에 저장
		ProfileDto newProfile = profileService.saveUserProfile(file);

		// 새 프로필 사진 userDto에 저장
		userDto.setProfileDto(newProfile);
		userDto = userRepository.saveAndFlush(userDto);

		// 기존 프로필 사진 서버/db에서 삭제
		if(prevProfile != null) {
			profileService.deleteProfile(prevProfile);
		}

		return userDto;
	}

	@Override
	public UserDto updateLastVisitedGround(Integer lastGroundId, UserDto userDto) throws Exception {
		log.info("service - modifylastVisitedGround :: 마지막으로 방문한 그라운드 수정 진입");
		userDto.setLastGroundId(lastGroundId);
		return userRepository.saveAndFlush(userDto);
	}

	@Override
	public UserDto savePersonalAccessToken(String newPersonalAccessToken, UserDto userDto) throws Exception {
		log.info("service - savePersonalAccessToken :: 사용자 personal access token 저장 진입");
		String encrypted = encryptPersonalAccessToken(newPersonalAccessToken);

		// 사용자 이메일이 저장되어 있지 않으면 pat로 email 조회해서 저장한다.
		if(userDto.getEmail() == null) {
			RestTemplate restTemplate = new RestTemplate();

			String emailUrl = API_URL + "/user/emails";

			// header
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", USER_INFO_REQUEST_TOKEN + newPersonalAccessToken);

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
				emailUrl,
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<List<Map<String, Object>>>() {
				}
			);

			String email = (String)response.getBody().get(0).get("email");
			userDto.setEmail(email);
		}

		userDto.updatePersonalAccessToken(encrypted);
		return userRepository.saveAndFlush(userDto);
	}

	@Override
	public UserDto deleteProfile(UserDto userDto) throws Exception {
		log.info("service - deleteProfile :: 사용자 프로필 사진 삭제 진입");
		// 사용자 프로필 dto
		ProfileDto profileDto = userDto.getProfileDto();

		userDto.setProfileDto(null);
		userDto = userRepository.saveAndFlush(userDto);

		// 프로필 사진 서버/db에서 삭제
		if(profileDto != null) {
			profileService.deleteProfile(profileDto);
		}

		return userDto;
	}

	@Override
	public void deleteUser(UserDto userDto) throws Exception {
		log.info("service - deleteUser :: 사용자 db/서버 삭제 진입");
		// 프로필 사진 받아오기
		ProfileDto profileDto = userDto.getProfileDto();

		// 사용자 db에서 삭제
		userRepository.delete(userDto);

		// 서버/db에서 프로필 사진 삭제
		if(profileDto != null) {
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

	@Override
	public void saveDeviceToken(UserDto userDto, String deviceToken) throws Exception {

		Set<String> deviceTokenList = userDto.getDeviceToken();
		deviceTokenList.add(deviceToken);
		userDto.setDeviceToken(deviceTokenList);

		userRepository.save(userDto);
	}

	@Override
	public void deleteDeviceToken(UserDto userDto, String deviceToken) throws Exception {
		Set<String> deviceTokenList = userDto.getDeviceToken();
		deviceTokenList.remove(deviceToken);
		userDto.setDeviceToken(deviceTokenList);

		userRepository.save(userDto);
	}

	private void createNewDeviceToken(UserDto userDto, String deviceToken) {

		HashMap<String, Object> body = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "key="+SERVERKEY);
		headers.add("project_id", "1067642901305");

		body.put("operation", "create");
		body.put("notification_key_name", userDto.getId().toString());
		List<String> list = new ArrayList<>();
		list.add(deviceToken);
		body.put("registration_ids", list);

		// log.info("new deviceToken arrayList :: {}", list);

		HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Map<String, String>> response = restTemplate.exchange(
			"https://fcm.googleapis.com/fcm/notification",
			HttpMethod.POST,
			entity,
			new ParameterizedTypeReference <Map<String, String>>(){}
		);

		log.info("new deviceToken response :: {}", response);

		Set<String> deviceTokenList = userDto.getDeviceToken();
		deviceTokenList.add(response.getBody().get("notification_key"));

		userDto.setDeviceToken(deviceTokenList);

		userRepository.save(userDto);
	}

	public String encryptPersonalAccessToken(String personalAccessToken) throws Exception {
		return aesUtil.aes256Encrypt(personalAccessToken);
	}

	public String decryptPersonalAccessToken(String personalAccessToken) throws Exception {
		return aesUtil.aes256Decrypt(personalAccessToken);
	}
}
