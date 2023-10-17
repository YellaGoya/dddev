package com.d103.dddev.api.common.oauth2.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

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

import com.auth0.jwt.algorithms.Algorithm;
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
	private String USER_INFO_REQUEST_URL = "https://api.github.com/user";

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
		Map<String, Object> response = githubToken(code);
		String githubAccessToken = (String)response.get("access_token");
		String githubRefreshToken = (String)response.get("refresh_token");

		// 사용자 정보 받아오기
		Map<String, Object> userInfo = getUserInfo(githubAccessToken);
		//System.out.println(userInfo);
		String name = (String)userInfo.get("login");
		Integer githubId = (Integer)userInfo.get("id");

		// jwt로 자체 access, refresh token 만들기
		String accessToken = BEARER + jwtService.createAccessToken(githubId);
		String refreshToken = BEARER + jwtService.createRefreshToken();

		// 사용자 정보가 있으면 로그인, 없으면 db에 저장
		UserDto user = getUser(githubId).orElseGet(() -> saveUser(userInfo));

		// refresh token db 저장
		updateRefreshToken(user, refreshToken);

		// access, refresh token, 이름
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", accessToken);
		map.put("Authorization-refresh", refreshToken);
		map.put("name", name);

		log.info("login :: github api login 성공");

		return map;
	}

	public Map<String, Object> githubToken(String code) throws Exception {
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

		return response.getBody();
	}

	public Map<String, Object> getUserInfo(String githubAccessToken) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		// header
		HttpHeaders headers = new HttpHeaders();
		String userInfoAccessToken = USER_INFO_REQUEST_TOKEN + githubAccessToken;
		headers.add("Authorization", userInfoAccessToken);

		// HttpEntity
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
			USER_INFO_REQUEST_URL,
			HttpMethod.GET,
			entity,
			new ParameterizedTypeReference<Map<String, Object>>() {
			}
		);

		return response.getBody();
	}

	public Optional<UserDto> getUser(Integer githubId) {
		log.info("getUser :: DB에서 사용자 정보 가져오기");
		return userRepository.findBygithubId(githubId);
	}

	public UserDto saveUser(Map<String, Object> userInfo) {
		log.info("saveUser :: DB에 사용자 정보 저장");
		String name = (String)userInfo.get("login");
		Integer githubId = (Integer)userInfo.get("id");

		UserDto user = UserDto.builder()
			.nickname(name)
			.githubId(githubId)
			.valid(true)
			.build();

		return userRepository.save(user);
	}

	public void updateRefreshToken(UserDto user, String refreshToken) {
		user.updateRefreshToken(refreshToken);
		userRepository.saveAndFlush(user);
	}

}
