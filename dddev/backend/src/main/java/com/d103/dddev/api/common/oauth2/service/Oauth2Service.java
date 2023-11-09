package com.d103.dddev.api.common.oauth2.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.d103.dddev.api.common.oauth2.Role;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.user.repository.UserRepository;
import com.d103.dddev.api.user.repository.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2Service {

	private final JwtService jwtService;
	private final UserRepository userRepository;

	private final String ACCESS_TOKEN_REQUEST_URL = "https://github.com/login/oauth/access_token";

	private final String API_URL = "https://api.github.com";
	private final String USER_INFO_REQUEST_TOKEN = "token ";

	private final String BEARER = "Bearer ";

	@Value("${spring.security.oauth2.client.registration.github.client-id}")
	private String CLIENT_ID;

	@Value("${spring.security.oauth2.client.registration.github.client-secret}")
	private String CLIENT_SECRET;

	@Value("${aes.secretKey}")
	private String AES_SECRET_KEY;

	public Map<String, String> login(String code) throws Exception {
		log.info("service - login :: github api login 진입");
		// github에서 access, refresh token 받아오기
		Map<String, String> response = githubToken(code);
		String githubAccessToken = response.get("access_token");
		String githubRefreshToken = response.get("refresh_token");

		// 사용자 정보 받아오기
		Map<String, Object> userInfo = getUserInfo(githubAccessToken);

		String name = (String)userInfo.get("login");
		Integer githubId = (Integer)userInfo.get("id");

		// jwt로 자체 access, refresh token 만들기
		String accessToken = jwtService.createAccessToken(githubId);
		String refreshToken = jwtService.createRefreshToken(githubId);

		User user = getUser(githubId, name).orElseGet(() -> saveUser(userInfo));

		// access, refresh token, 이름
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", accessToken);
		map.put("Authorization-refresh", refreshToken);
		map.put("nickname", user.getNickname());
		map.put("role", String.valueOf(user.getRole()));
		map.put("lastGroundId", String.valueOf(user.getLastGroundId()));

		log.info("login :: github api login 성공");

		return map;
	}

	public Map<String, String> githubToken(String code) throws Exception {
		log.info("service - gethubToken :: github에서 token 받아오기 진입");
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
		log.info("servie - getUserInfo :: github api로 사용자 정보 받아오기");
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

	public Boolean unlink(String oauthAccessToken) throws Exception {
		log.info("service - unlink :: github authorization 연결 끊기 진입");
		RestTemplate restTemplate = new RestTemplate();

		String deleteUrl = API_URL + "/applications/" + CLIENT_ID + "/grant";

		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject requestBody = new JSONObject();
		requestBody.put("access_token", oauthAccessToken);

		HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

		ResponseEntity<Object> response = restTemplate.exchange(
			deleteUrl,
			HttpMethod.DELETE,
			entity,
			Object.class
		);
		return response.getStatusCode().is2xxSuccessful();
	}

	public Optional<User> getUser(Integer githubId, String name) throws Exception {
		log.info("getUser :: DB에서 사용자 정보 가져오기");
		Optional<User> byGithubId = userRepository.findByGithubId(githubId);
		if(byGithubId.isPresent()) {
			User user = byGithubId.get();
			user.setGithubName(name);
			user = userRepository.saveAndFlush(user);
			byGithubId = Optional.of(user);
		}
		return byGithubId;
	}

	public User saveUser(Map<String, Object> userInfo) {
		log.info("saveUser :: DB에 사용자 정보 저장");
		String name = (String)userInfo.get("login");
		Integer githubId = (Integer)userInfo.get("id");

		User user = User.builder()
			.nickname(name)
			.githubName(name)
			.githubId(githubId)
			.valid(true)
			.role(Role.GUEST)
			.build();

		return userRepository.save(user);
	}

}
