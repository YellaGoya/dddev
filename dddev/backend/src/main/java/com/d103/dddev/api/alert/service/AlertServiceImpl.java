package com.d103.dddev.api.alert.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.d103.dddev.api.alert.dto.CreateWebhookResponseDto;
import com.d103.dddev.api.alert.dto.ReceiveWebhookDto;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.user.repository.dto.UserDto;
import com.d103.dddev.api.user.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertServiceImpl implements AlertService {

	private final JwtService jwtService;
	private final UserServiceImpl userService;

	// @Value("${}")
	// String serverKey;

	@Override
	public void addCommitWebhook(String header, String repoName) throws Exception {

		// UserDto userDto = jwtService.getUser(header).orElseThrow(
		// 	() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

		// test
		UserDto userDto = UserDto.builder()
			.personalAccessToken(userService.encryptPersonalAccessToken("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"))
			.nickname("gayun0303")
			.build();


		String token = userService.decryptPersonalAccessToken(userDto.getPersonalAccessToken());

		HashMap<String, Object> body = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/vnd.github+json");
		headers.add("Authorization", "Bearer "+token);
		headers.add("X-GitHub-Api-Version", "2022-11-28");

		body.put("name", "web");
		body.put("active", true);
		body.put("events", new String[]{"push"});
		HashMap<String, Object> configHashMap = new HashMap<>();
		configHashMap.put("url", "https://k9d103.p.ssafy.io/alert-service/receive-webhook");
		configHashMap.put("content_type", "json");
		// configHashMap.put("insecure_ssl", "0");
		body.put("config", configHashMap);

		HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<CreateWebhookResponseDto> response = restTemplate.exchange(
			"https://api.github.com/repos/"+userDto.getNickname()+"/"+repoName+"/hooks",
			HttpMethod.POST,
			entity,
			CreateWebhookResponseDto.class
		);

		// 201 Created 가 아닌 경우
		if(response.getStatusCode().value() != 201) {
			throw new Exception("알림을 생성하지 못했습니다.");
		}

		// log.info("response {}", response);
		// 알림 생성된 정보 저장 - id, push/PR

		// ModelMapper mapper = new ModelMapper();
		// mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		//
		// UserEntity userEntity = mapper.map(userDto, UserEntity.class);




		System.out.println("response "+ response);

	}

	@Override
	public void receiveWebhook(Map<String, Object> headerMap, ReceiveWebhookDto receiveWebhookDto) throws Exception {

		// 해당 레포의 그라운드를 찾고 그 멤버를 찾음


		// 멤버의 알림 수신 여부를 확인하고 수신하는 멤버들 돌면서 (파일, 키워드) 검색,


		// 리스트에 해당되는 멤버들 기기 알림 토큰 추가하고

		List<String> deviceTokenList = new ArrayList<>();
		deviceTokenList.add("fGhyTsSkUvIKfHFsZyZKNh:APA91bFoJv0MB3z5ZKYo6baDzXbwYWevrUieBlyPe3OYzRCp1UNJ1rKH_CvjMnr6-Uc5i9sO9CJpvyrTzS-m4Jp_884nkhsMCfWTzXB-9ytTby0WqccmFKq4oxaJ8lYNl9QmGoYjh-Ij");


		// 백 -> fcm 요청

		// test
		String serverKey = "AAAA-JR50zk:APA91bF4mudANm2i7fUAWnk9SGV2C4wvnqbal6AsiIwG9P8sxiQxNBU7eaEkPZ6RVQpJ8M5POblq43u94Wpja7qXL01GKE4yjAk9pE8a-yDYbdB98_LJ1lOBftUVoloFaCY6IAE7MuDs";

		HashMap<String, Object> body = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "key="+serverKey);

		HashMap<String, String> notificationContentMap = new HashMap<>();
		notificationContentMap.put("title", "@@@@@@@.java");
		notificationContentMap.put("body", "파일에 변경사항이 발생했어요!");
		body.put("notification", notificationContentMap);

		for (String token: deviceTokenList) {
			body.put("to", token);

			HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(body, headers);

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<Object> response = restTemplate.exchange(
				"https://fcm.googleapis.com/fcm/send",
				HttpMethod.POST,
				entity,
				Object.class
			);
		}

		// 알림 내역을 db에 저장

	}
}
