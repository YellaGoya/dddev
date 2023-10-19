package com.d103.dddev.api.common.oauth2.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.d103.dddev.api.common.oauth2.Role;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.user.repository.UserRepository;
import com.d103.dddev.api.user.repository.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2Service {

	private final JwtService jwtService;
	private final UserRepository userRepository;

	private String ACCESS_TOKEN_REQUEST_URL = "https://github.com/login/oauth/access_token";

	private String API_URL = "https://api.github.com";
	private String USER_INFO_REQUEST_TOKEN = "token ";

	private String BEARER = "Bearer ";

	@Value("${spring.security.oauth2.client.registration.github.client-id}")
	private String CLIENT_ID;

	@Value("${spring.security.oauth2.client.registration.github.client-secret}")
	private String CLIENT_SECRET;

	@Value("${aes.secretKey}")
	private String AES_SECRET_KEY;

	public Map<String, String> login(String code) throws Exception {
		log.info("login :: github api login 진입");
		// github에서 access, refresh token 받아오기
		Map<String, String> response = githubToken(code);
		String githubAccessToken = response.get("access_token");
		String githubRefreshToken = response.get("refresh_token");

		System.out.println(githubAccessToken);

		// 사용자 정보 받아오기
		Map<String, Object> userInfo = getUserInfo(githubAccessToken);
		//System.out.println(userInfo);
		String name = (String)userInfo.get("login");
		Integer githubId = (Integer)userInfo.get("id");

		// jwt로 자체 access, refresh token 만들기
		String accessToken = BEARER + jwtService.createAccessToken(githubId);
		String refreshToken = BEARER + jwtService.createRefreshToken();

		UserDto userDto = getUser(githubId).orElseGet(() -> saveUser(userInfo));

		// refresh token db 저장
		updateRefreshToken(userDto, refreshToken);

		// access, refresh token, 이름
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", accessToken);
		map.put("Authorization-refresh", refreshToken);
		map.put("name", name);
		map.put("role", String.valueOf(userDto.getRole()));

		log.info("login :: github api login 성공");

		return map;
	}

	public Map<String, String> githubToken(String code) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		// header 만들기
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");

		// body 만들기
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("client_id", CLIENT_ID);
		params.add("client_secret", CLIENT_SECRET);
		params.add("code", code);
		params.add("basicAuth", false);

		// header랑 body 합치기
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);

		// post 요청
		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
			ACCESS_TOKEN_REQUEST_URL,
			HttpMethod.POST,
			entity,
			new ParameterizedTypeReference<Map<String, Object>>() {
			}
		);

		Map<String, Object> responseBody = response.getBody();

		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", (String)responseBody.get("access_token"));
		tokens.put("refresh_token", (String)responseBody.get("refresh_token"));

		return tokens;
	}

	public Map<String, Object> getUserInfo(String githubAccessToken) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		String userInfoUrl = API_URL + "/user";

		// header
		HttpHeaders headers = new HttpHeaders();
		String userInfoAccessToken = USER_INFO_REQUEST_TOKEN + githubAccessToken;
		headers.add("Authorization", userInfoAccessToken);

		// HttpEntity
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
			userInfoUrl,
			HttpMethod.GET,
			entity,
			new ParameterizedTypeReference<Map<String, Object>>() {
			}
		);

		return response.getBody();
	}

	// TODO : 테스트해보기.. 확실하지않음.....
	public Boolean unlink(String oauthAccessToken) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		String deleteUrl = API_URL + "/applications/" + CLIENT_ID + "/grant";

		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		headers.set("Accept", "application/vnd.github+json");	// 옵션

		System.out.println(headers);

		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("access_token", oauthAccessToken);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<Object> response = restTemplate.exchange(
			deleteUrl,
			HttpMethod.DELETE,
			entity,
			Object.class
		);
		System.out.println(response);
		return response.getStatusCode().is2xxSuccessful();
	}

	public Optional<UserDto> getUser(Integer githubId) throws Exception {
		log.info("getUser :: DB에서 사용자 정보 가져오기");
		return userRepository.findByGithubId(githubId);
	}

	public UserDto saveUser(Map<String, Object> userInfo) {
		log.info("saveUser :: DB에 사용자 정보 저장");
		String name = (String)userInfo.get("login");
		Integer githubId = (Integer)userInfo.get("id");

		UserDto user = UserDto.builder()
			.nickname(name)
			.githubId(githubId)
			.valid(true)
			.role(Role.GUEST)
			.build();

		return userRepository.save(user);
	}

	public void updateRefreshToken(UserDto user, String refreshToken) {
		user.updateRefreshToken(refreshToken);
		userRepository.saveAndFlush(user);
	}

}
