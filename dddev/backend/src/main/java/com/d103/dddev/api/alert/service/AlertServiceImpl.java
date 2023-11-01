package com.d103.dddev.api.alert.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.d103.dddev.api.alert.dto.AlertUserDto;
import com.d103.dddev.api.alert.dto.CommitDataDto;
import com.d103.dddev.api.alert.dto.CreateWebhookRequestDto;
import com.d103.dddev.api.alert.dto.CreateWebhookResponseDto;
import com.d103.dddev.api.alert.dto.ReceiveWebhookDto;
import com.d103.dddev.api.alert.entity.AlertDataEntity;
import com.d103.dddev.api.alert.entity.AlertEntity;
import com.d103.dddev.api.alert.repository.AlertDataRepo;
import com.d103.dddev.api.alert.repository.AlertRepository;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.repository.service.RepositoryService;
import com.d103.dddev.api.user.repository.dto.UserDto;
import com.d103.dddev.api.user.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlertServiceImpl implements AlertService {

	private final JwtService jwtService;
	private final UserServiceImpl userService;
	private final RepositoryService repositoryService;
	private final AlertRepository alertRepository;
	private final AlertDataRepo alertDataRepo;

	@Override
	public void addCommitWebhook(String header, CreateWebhookRequestDto createWebhookRequestDto) throws Exception {

		UserDto userDto = jwtService.getUser(header).orElseThrow(
			() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

		// TODO: 알림을 생성하려는 사용자의 기기 토큰이 없을 경우 기기 토큰을 먼저 받아야 함

		String token = userService.decryptPersonalAccessToken(userDto.getPersonalAccessToken());

		Integer repositoryId = createWebhookRequestDto.getRepositoryId();
		List<String> keyword = createWebhookRequestDto.getKeyword();

		RepositoryDto repositoryDto = repositoryService.getRepository(repositoryId).orElseThrow(
			() -> new NoSuchElementException("getRepoInfo :: 존재하지 않는 레포지터리입니다.")
		);

		// 이미 alertdto가 있는 경우 - repo id, type 비교
		List<AlertEntity> alertEntityOptional = alertRepository.findAllByRepositoryIdAndType(repositoryId, "push");

		if(!alertEntityOptional.isEmpty()) {

			Optional<AlertEntity> userAlertDto = alertRepository.findByUserDto_IdAndRepositoryIdAndType(userDto.getId(), repositoryId, "push");
			if(userAlertDto.isPresent()) {
				throw new Exception("이미 생성한 알림입니다.");
			}
			AlertEntity existAlertEntity = alertEntityOptional.get(0);
			AlertEntity alertEntity = AlertEntity.builder()
				.webhookId(existAlertEntity.getWebhookId())
				.type(existAlertEntity.getType())
				.createdDate(LocalDateTime.now())
				.userDto(userDto)
				.keyword(keyword)
				.repositoryId(existAlertEntity.getRepositoryId())
				.build();

			alertRepository.save(alertEntity);

			log.info("해당 알림이 이미 존재합니다. 사용자만 등록합니다.");

			return;
		}

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
		configHashMap.put("insecure_ssl", "0");
		body.put("config", configHashMap);

		HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<CreateWebhookResponseDto> response = restTemplate.exchange(
			"https://api.github.com/repos/"+userDto.getGithubName()+"/"+repositoryDto.getName()+"/hooks",
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

		CreateWebhookResponseDto createWebhookResponseDto = response.getBody();

		AlertEntity alertEntity = AlertEntity.builder()
			.webhookId(createWebhookResponseDto.getId())
			.createdDate(createWebhookResponseDto.getCreatedAt())
			.repositoryId(repositoryId)
			.keyword(keyword)
			.type(createWebhookResponseDto.getEvents().get(0))
			.userDto(userDto)
			.build();

		alertRepository.save(alertEntity);

	}

	@Override
	public void receiveWebhook(Map<String, Object> headerMap, ReceiveWebhookDto receiveWebhookDto) throws Exception {

		// 해당 레포, 타입 알림을 수신하는 사용자, 키워드 조회
		List<AlertUserDto> userDtoList = alertRepository.findByRepositoryIdAndType(
			receiveWebhookDto.getRepository().getId(), "push");

		log.info("AlertUserDto List {}", userDtoList);

		log.info("sender Dto {}", receiveWebhookDto.getSender().get("id"));

		// 트리거 발생자
		UserDto sender = userService.getUserInfo(Integer.valueOf(receiveWebhookDto.getSender().get("id"))).orElse(null);

		log.error("sender dto :: {}", sender);
		for (AlertUserDto alertUserDto : userDtoList) {
			// 변경될 파일 알려주기 위한 리스트
			List<String> changedFileList = new ArrayList<>();

			Set<String> keywordSet = new HashSet<>();

			for (CommitDataDto commitDataDto : receiveWebhookDto.getCommits()) {

				for (String keyword : alertUserDto.getKeyword()) {

					// 변경한 파일 검색
					for (String filename : commitDataDto.getAdded()) {
						if (filename.contains(keyword)) {
							changedFileList.add(filename);
							keywordSet.add(keyword);
						}
					}
					for (String filename : commitDataDto.getRemoved()) {
						if (filename.contains(keyword)){
							changedFileList.add(filename);
							keywordSet.add(keyword);
						}
					}
					for (String filename : commitDataDto.getModified()) {
						if (filename.contains(keyword)){
							changedFileList.add(filename);
							keywordSet.add(keyword);
						}
					}
					// commitDataDto.getAdded().stream()
					// 	.filter(filename -> filename.contains(keyword))
					// 	.forEach(filename -> changedFileList.add(filename));

				} // 키워드
			}    // 커밋 리스트

			log.error("end commit files :: ");

			if (!changedFileList.isEmpty()) {
				log.info("changedFile :: ");
				sendAlert(alertUserDto.getUserDto(), sender, keywordSet, changedFileList, 0);
				continue;
			}

			List<String> commitMessageList = new ArrayList<>();
			for (CommitDataDto commitDataDto : receiveWebhookDto.getCommits()) {
				for (String keyword : alertUserDto.getKeyword()) {
					String commitMsg = commitDataDto.getMessage();

					// 커밋 메시지 검색
					if (commitMsg.contains(keyword)) {
						commitMessageList.add(commitMsg);
						keywordSet.add(keyword);
					}
				}    // 키워드
			}	// 커밋 리스트

			log.error("end commit msg :: ");

			if (!commitMessageList.isEmpty()) {
				log.info("commitMessage :: ");
				sendAlert(alertUserDto.getUserDto(), sender, keywordSet, commitMessageList, 1);
				continue;
			}

			// TODO: 커밋 파일별 상세 변경사항 검색

		}    // 사용자+키워드
	}

	@Override
	public void updateAlert(List<String> keywordList, Integer alertId) throws Exception {
		// TODO: 알림 타입도 변경할 수 있게 변경
		AlertEntity alertEntity = alertRepository.findById(alertId).orElseThrow(() -> new Exception("알림이 존재하지 않습니다."));
		alertEntity.setKeyword(keywordList);
	}

	@Override
	public List<AlertEntity> alertList(UserDto userDto) throws Exception {
		return alertRepository.findByUserDto_Id(userDto.getId());
	}

	@Override
	public void deleteAlert(UserDto userDto, Integer alertId) throws Exception {

		// alertId로 웹훅 아이디 찾고, alert 테이블에 해당 웹훅 아이디로 만들어진 다른 알람이 없을 때
		// 알림 소유자만 삭제할 수 있도록
		AlertEntity alertEntity = alertRepository.findByIdAndUserDto_Id(alertId, userDto.getId()).orElseThrow(
			() -> new Exception("deleteAlert :: 해당 알림 정보를 찾을 수 없습니다."));
		List<AlertEntity> sameWebhookAlertList = alertRepository.findByWebhookId(alertEntity.getWebhookId());

		if(sameWebhookAlertList.size() == 1) {
			// 깃허브에서 훅 삭제

			String token = userService.decryptPersonalAccessToken(userDto.getPersonalAccessToken());
			RepositoryDto repositoryDto = repositoryService.getRepository(alertEntity.getRepositoryId()).orElseThrow(
				() -> new Exception("deleteAlert :: 알림이 생성된 리포지터리 정보를 찾을 수 없습니다.")
			);

			HttpHeaders headers = new HttpHeaders();

			headers.add("Accept", "application/vnd.github+json");
			headers.add("Authorization", "Bearer "+token);
			headers.add("X-GitHub-Api-Version", "2022-11-28");

			HashMap<String, Object> body = new HashMap<>();

			HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(body, headers);

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<Object> response = restTemplate.exchange(
				"https://api.github.com/repos/"+userDto.getGithubName()+"/"+repositoryDto.getName()+"/hooks/"+alertEntity.getWebhookId(),
				HttpMethod.DELETE,
				entity,
				Object.class
			);
			// log.info("deleteAlert :: 웹훅 삭제 응답 {}", response);

			if(response.getStatusCode().value() == 404) {
				throw new Exception("deleteAlert :: 깃허브에서 해당 웹훅을 찾을 수 없습니다. 삭제 실패");
			}
		}
		// db에서 알림 및 키워드 삭제
		alertRepository.deleteById(alertId);
	}

	public void sendAlert(UserDto userDto, UserDto sender, Set<String> titleSet, List<String> bodyList, Integer type) throws Exception {
		// 기기 토큰 불러오기
		// String token = userDto.getDeviceToken();
		String token = "fGhyTsSkUvIKfHFsZyZKNh:APA91bE0UvmvKX9C_4QylHX_WTgDChjC1cVFJRMpIfzpPb5kuHg43SYiPLWdYF6hmcFsfhY6l_tGdW_yy3X5f-rsIf9daqRWEWs7Ikq0VgQl4l3VEB-LRFcTo1o3-Zz_FcMDj-kL7LT7";

		// 백 -> fcm 요청
		// test
		String serverKey = "AAAA-JR50zk:APA91bF4mudANm2i7fUAWnk9SGV2C4wvnqbal6AsiIwG9P8sxiQxNBU7eaEkPZ6RVQpJ8M5POblq43u94Wpja7qXL01GKE4yjAk9pE8a-yDYbdB98_LJ1lOBftUVoloFaCY6IAE7MuDs";

		HashMap<String, Object> body = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "key="+serverKey);

		HashMap<String, String> notificationContentMap = new HashMap<>();

		String title = null;
		if(type == 0) {
			title = "파일이 변경되엇습니다.";
		} else if (type == 1) {
			title = "커밋 메시지가 발생했습니다.";
		}

		// notificationContentMap.put("title", String.join(",", titleSet));	// 해당 키워드 셋
		notificationContentMap.put("title", title);
		notificationContentMap.put("body", String.join(",", bodyList));

		body.put("to", token);
		body.put("notification", notificationContentMap);

		HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Object> response = restTemplate.exchange(
			"https://fcm.googleapis.com/fcm/send",
			HttpMethod.POST,
			entity,
			Object.class
		);
		// }

		log.info("fcm response :: {}", response);

		// 전송한 알림 내역을 db에 저장
		AlertDataEntity alertDataEntity = AlertDataEntity.builder()
			.title(title)
			.content(String.join(",", bodyList))
			.alertType("push")
			.receiverId(userDto.getId())
			.sendingDate(LocalDateTime.now().toString())
			.creatorId(sender.getId())
			.build();

		log.info("데이터 저장하기 전 로그");

		alertDataRepo.addAlertData(alertDataEntity);

		log.info("firestore에 데이터가 정상 저장되었습니다.");

	}

}
