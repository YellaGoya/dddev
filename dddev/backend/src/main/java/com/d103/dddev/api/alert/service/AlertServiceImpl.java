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

import com.d103.dddev.api.user.repository.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.d103.dddev.api.alert.dto.AlertUserKeyword;
import com.d103.dddev.api.alert.dto.CommitDataDto;
import com.d103.dddev.api.alert.dto.CreateWebhookRequestDto;
import com.d103.dddev.api.alert.dto.CreateWebhookResponseDto;
import com.d103.dddev.api.alert.dto.FcmResponseDto;
import com.d103.dddev.api.alert.dto.PushWebhookDto;
import com.d103.dddev.api.alert.dto.UpdateAlertDto;
import com.d103.dddev.api.alert.dto.PullRequestWebhookDto;
import com.d103.dddev.api.alert.entity.AlertDataEntity;
import com.d103.dddev.api.alert.entity.AlertEntity;
import com.d103.dddev.api.alert.repository.AlertDataRepo;
import com.d103.dddev.api.alert.repository.AlertRepository;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.repository.repository.entity.Repository;
import com.d103.dddev.api.repository.service.RepositoryService;
import com.d103.dddev.api.user.repository.entity.User;
import com.d103.dddev.api.user.service.UserServiceImpl;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.request.DuplicateRequestException;

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

	@Value("${firebase.server.key}")
	private String SERVER_KEY;
	private final static String PUSH_WEBHOOK_URL = "https://k9d103.p.ssafy.io/alert-service/push-webhook";
	private final static String PULL_REQUEST_WEBHOOK_URL = "https://k9d103.p.ssafy.io/alert-service/pull-request-webhook";

	@Override
	public void createAlert(String header, CreateWebhookRequestDto createWebhookRequestDto) throws Exception {

		User user = jwtService.getUser(header).orElseThrow(
			() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

		if (user.getDeviceToken().size() == 0) {
			throw new NoSuchElementException("createWebhook :: 사용자 알림 허용이 필요합니다.");
		}

		Integer repositoryId = createWebhookRequestDto.getRepoId();
		List<String> keyword = createWebhookRequestDto.getKeyword();
		String type = createWebhookRequestDto.getType();

		Repository repository = repositoryService.getRepository(repositoryId).orElseThrow(
			() -> new NoSuchElementException("getRepoInfo :: 존재하지 않는 레포지터리입니다.")
		);

		// 이미 alertdto가 있는 경우 - repo id, type 비교
		List<AlertEntity> alertEntityOptional = alertRepository.findAllByRepositoryIdAndType(repositoryId, type);

		if (!alertEntityOptional.isEmpty()) {

			Optional<AlertEntity> userAlertDto = alertRepository.findByUser_IdAndRepositoryIdAndType(user.getId(),
				repositoryId, type);
			if (userAlertDto.isPresent()) {
				throw new DuplicateRequestException("이미 생성한 알림입니다.");
			}
			AlertEntity existAlertEntity = alertEntityOptional.get(0);
			AlertEntity alertEntity = AlertEntity.builder()
				.webhookId(existAlertEntity.getWebhookId())
				.type(existAlertEntity.getType())
				.createdDate(LocalDateTime.now())
				.user(user)
				.keyword(keyword)
				.repositoryId(existAlertEntity.getRepositoryId())
				.build();

			alertRepository.save(alertEntity);

			log.info("해당 알림이 이미 존재합니다. 사용자만 등록합니다.");

			return;
		}
		String url = null;

		if (type.equals("push")) {
			url = PUSH_WEBHOOK_URL;
		} else if (type.equals("pull_request")) {
			url = PULL_REQUEST_WEBHOOK_URL;
		} else {
			throw new InvalidTypeException("createWebhook :: 존재하지 않는 알림 타입입니다.");
		}

		CreateWebhookResponseDto createWebhookResponseDto = createWebhook(user, repository, keyword, type, url);

		AlertEntity alertEntity = AlertEntity.builder()
			.webhookId(createWebhookResponseDto.getId())
			.createdDate(createWebhookResponseDto.getCreatedAt())
			.repositoryId(repositoryId)
			.keyword(keyword)
			.type(createWebhookResponseDto.getEvents().get(0))
			.user(user)
			.build();

		alertRepository.save(alertEntity);

	}

	private CreateWebhookResponseDto createWebhook(User user, Repository repository, List<String> keyword,
												   String type, String url) throws Exception {

		String token = userService.decryptPersonalAccessToken(user.getPersonalAccessToken());
		Integer repositoryId = repository.getRepoId();

		HashMap<String, Object> body = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/vnd.github+json");
		headers.add("Authorization", "Bearer " + token);
		headers.add("X-GitHub-Api-Version", "2022-11-28");

		body.put("name", "web");
		body.put("active", true);
		body.put("events", new String[] {type});
		HashMap<String, Object> configHashMap = new HashMap<>();
		configHashMap.put("url", url);
		configHashMap.put("content_type", "json");
		configHashMap.put("insecure_ssl", "0");
		body.put("config", configHashMap);

		HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();

		// log.info("before ::::::::::::");

		ResponseEntity<CreateWebhookResponseDto> response = null;

		// try {
		response = restTemplate.exchange(
			"https://api.github.com/repos/" + user.getGithubName() + "/" + repository.getName() + "/hooks",
			HttpMethod.POST,
			entity,
			CreateWebhookResponseDto.class
		);

		// log.info("response {}", response);

		// 201 Created 가 아닌 경우 에러 처리
		if (response.getStatusCode().value() != 201) {
			throw new Exception("알림을 생성하지 못했습니다.");
		}

		// 알림 생성된 정보 저장 - id, push/PR

		CreateWebhookResponseDto createWebhookResponseDto = response.getBody();

		return createWebhookResponseDto;

	}

	@Override
	public void receivePushWebhook(Map<String, Object> headerMap, PushWebhookDto pushWebhookDto) throws Exception {

		// 해당 레포, 타입 알림을 수신하는 사용자, 키워드 조회
		List<AlertUserKeyword> userKeyowrdList = alertRepository.findByRepositoryIdAndType(
			pushWebhookDto.getRepository().getId(), "push");

		// 트리거 발생자
		UserDto sender = userService.getUserDto(Integer.valueOf(pushWebhookDto.getSender().get("id"))).orElse(null);

		for (AlertUserKeyword alertUserKeyword : userKeyowrdList) {
			FcmResponseDto fcmResponseDto = null;
			String title = null;
			String content = null;
			// 변경될 파일 알려주기 위한 리스트
			List<String> changedFileList = new ArrayList<>();

			Set<String> keywordSet = new HashSet<>();

			for (CommitDataDto commitDataDto : pushWebhookDto.getCommits()) {

				for (String keyword : alertUserKeyword.getKeyword()) {

					// 변경한 파일 검색
					for (String filename : commitDataDto.getAdded()) {
						if (filename.contains(keyword)) {
							changedFileList.add(filename);
							keywordSet.add(keyword);
						}
					}
					for (String filename : commitDataDto.getRemoved()) {
						if (filename.contains(keyword)) {
							changedFileList.add(filename);
							keywordSet.add(keyword);
						}
					}
					for (String filename : commitDataDto.getModified()) {
						if (filename.contains(keyword)) {
							changedFileList.add(filename);
							keywordSet.add(keyword);
						}
					}

				} // 키워드
			}    // 커밋 리스트

			if (!changedFileList.isEmpty()) {
				title = "❗ 파일이 변경되엇습니다 ❗";
				content = "키워드 " + String.join(",", keywordSet) + " 발생!!";
				// 응답, 보낸 알림 제목, 보낸 알림 내용, 수신자, 발신자, 발생 브랜치, 비교 url, 알림 타입
				String docId = saveAlertData(title, content, alertUserKeyword.getUser(), sender,
					pushWebhookDto.getRef(), pushWebhookDto.getCompare(), "push", keywordSet, null, changedFileList);

				fcmResponseDto = sendAlert(alertUserKeyword.getUser(), title, content, pushWebhookDto.getCompare(), docId);

				// TODO: fcmResponseDto 의 success, failure 데이터 firestore에 저장
				continue;
			}

			List<String> commitMessageList = new ArrayList<>();
			for (CommitDataDto commitDataDto : pushWebhookDto.getCommits()) {
				for (String keyword : alertUserKeyword.getKeyword()) {
					String commitMsg = commitDataDto.getMessage();

					// 커밋 메시지 검색
					if (commitMsg.contains(keyword)) {
						commitMessageList.add(commitMsg);
						keywordSet.add(keyword);
					}
				}    // 키워드
			}    // 커밋 리스트

			if (!commitMessageList.isEmpty()) {
				title = "❗ 커밋 메시지가 발생했습니다 ❗";
				content = "키워드 " + String.join(",", keywordSet) + " 발생!!";

				// 응답, 보낸 알림 제목, 보낸 알림 내용, 수신자, 발신자, 발생 브랜치, 비교 url, 알림 타입
				String docId = saveAlertData(title, content, alertUserKeyword.getUser(), sender,
					pushWebhookDto.getRef(), pushWebhookDto.getCompare(), "push", keywordSet, commitMessageList, null);

				fcmResponseDto = sendAlert(alertUserKeyword.getUser(), title, content, pushWebhookDto.getCompare(), docId);


				continue;
			}

			// TODO: 커밋 파일별 상세 변경사항 검색

		}    // 사용자+키워드
	}

	private String saveAlertData(String title, String content, User receiver,
		UserDto sender, String branch, String url, String type, Set<String> keywordSet, List<String> commitMessageList,
		List<String> changedFileList) throws Exception {

		// 전송한 알림 내역을 db에 저장
		AlertDataEntity alertDataEntity = AlertDataEntity.builder()
			.title(title)
			.content(content)
			.alertType(type)
			.branch(branch)
			.receiverId(receiver.getGithubId())
			.sendingDate(LocalDateTime.now().toString())
			.url(url)
			.creatorId(sender.getGithubId())
			.keywordList(new ArrayList<>(keywordSet))
			.commitMessageList(commitMessageList)
			.changedFileList(changedFileList)
			.isRead(false)
			// .success(fcmResponseDto.getSuccess())
			// .failure(fcmResponseDto.getFailure())
			.build();

		// log.info("데이터 저장하기 전 로그");

		return alertDataRepo.addAlertData(alertDataEntity);

		// log.info("firestore에 데이터가 정상 저장되었습니다.");
	}

	@Override
	public void updateAlert(UpdateAlertDto updateAlertDto, Integer alertId) throws Exception {
		// TODO: 알림 타입도 변경할 수 있게 변경
		AlertEntity alertEntity = alertRepository.findById(alertId).orElseThrow(
			() -> new NoSuchElementException("updateAlert :: 알림이 존재하지 않습니다."));

		String type = updateAlertDto.getType();
		Integer repositoryId = alertEntity.getRepositoryId();
		List<String> keyword = updateAlertDto.getKeyword();

		/*
		 * 알림 타입을 변경할 경우
		 * 삭제하려는 타입 알림이 하나밖에 안 남은 경우 - 웹훅 삭제 필요
		 * 생성하려는 타입 알림이 없는 경우 - 새로운 웹훅 생성 필요
		 * */
		if (!type.equals(alertEntity.getType())) {
			User user = alertEntity.getUser();
			// 삭제하려는 타입 알림 조회 - 하나뿐이면 웹훅 삭제 -> 삭제 메소드에서 조건 수행
			// List<AlertEntity> existAlertEntityList = alertRepository.findAllByRepositoryIdAndType(repoId, alertEntity.getType());
			// if(existAlertEntityList.size() == 1) {
			// 	deleteAlert(userDto, alertId);
			// }

			deleteAlertCondition(user, alertEntity);

			// 생성하려는 타입 알림 조회
			List<AlertEntity> alertEntityList = alertRepository.findAllByRepositoryIdAndType(repositoryId, type);

			// 생성하려는 타입 알림 웹훅이 이미 존재하는 경우
			// 해당 웹훅 정보 저장하고 수정 저장하고 끝
			if (!alertEntityList.isEmpty()) {
				AlertEntity existAlertEntity = alertEntityList.get(0);
				alertEntity.setWebhookId(existAlertEntity.getWebhookId());
				alertEntity.setType(type);
				alertEntity.setKeyword(keyword);
				//save
				return;
			}

			// 생성하려는 타입 알림 웹훅이 없을 경우 - 새로 생성해야 함

			String url = null;

			if (type.equals("push")) {
				url = PUSH_WEBHOOK_URL;
			} else if (type.equals("pull_request")) {
				url = PULL_REQUEST_WEBHOOK_URL;
			} else {
				throw new InvalidTypeException("updateAlert :: 존재하지 않는 알림 타입입니다.");
			}

			Repository repository = repositoryService.getRepository(repositoryId).orElseThrow(
				() -> new NoSuchElementException("updateAlert :: 존재하지 않는 레포지터리입니다.")
			);

			CreateWebhookResponseDto createWebhookResponseDto = createWebhook(user, repository, keyword, type,
				url);

			alertEntity.setType(type);
			alertEntity.setKeyword(keyword);
			alertEntity.setWebhookId(createWebhookResponseDto.getId());
			return;
		}

		// 타입 바꾸는게 아니라면 키워드 수정하고 끝
		alertEntity.setKeyword(updateAlertDto.getKeyword());

		// TODO: if문 조건을 반전해서 타입 안바꾸는게 안으로 들어가게 수정
	}

	@Override
	public List<AlertEntity> alertList(User user) throws Exception {
		return alertRepository.findByUser_Id(user.getId());
	}

	@Override
	public void deleteAlert(User user, Integer alertId) throws Exception {

		// alertId로 웹훅 아이디 찾고, alert 테이블에 해당 웹훅 아이디로 만들어진 다른 알람이 없을 때
		// 알림 소유자만 삭제할 수 있도록
		AlertEntity alertEntity = alertRepository.findByIdAndUser_Id(alertId, user.getId()).orElseThrow(
			() -> new Exception("deleteAlert :: 해당 알림 정보를 찾을 수 없습니다."));

		deleteAlertCondition(user, alertEntity);

		// db에서 알림 및 키워드 삭제
		alertRepository.deleteById(alertId);
	}

	@Override
	public void receivePullRequestWebhook(Map<String, Object> headerMap,
		PullRequestWebhookDto pullRequestWebhookDto) throws Exception {

		FcmResponseDto fcmResponseDto = null;

		// 해당 레포, 타입 알림을 수신하는 사용자, 키워드 조회
		List<AlertUserKeyword> userKeywordList = alertRepository.findByRepositoryIdAndType(
			pullRequestWebhookDto.getRepository().getId(), "pull_request");
		// 트리거 발생자
		UserDto sender = userService.getUserDto(Integer.valueOf(pullRequestWebhookDto.getSender().get("id")))
			.orElse(null);

		String headBranch = pullRequestWebhookDto.getPullRequest().getHead().getRef();
		String baseBranch = pullRequestWebhookDto.getPullRequest().getBase().getRef();
		String url = pullRequestWebhookDto.getPullRequest().getUrl();

		for (AlertUserKeyword alertUserKeyword : userKeywordList) {

			String alertTitle = null;
			String alertContent = null;

			Set<String> keywordSet = new HashSet<>();
			String title = pullRequestWebhookDto.getPullRequest().getTitle();
			String body = pullRequestWebhookDto.getPullRequest().getBody();

			// log.info("title, body :: {}, {}", title, body);

			for (String keyword : alertUserKeyword.getKeyword()) {

				if (title != null && title.contains(keyword)) {
					keywordSet.add(keyword);
				}

				if (body != null && body.contains(keyword)) {
					keywordSet.add(keyword);
				}

			} // 키워드
			if (!keywordSet.isEmpty()) {
				alertTitle = "❗ 풀 리퀘스트가 발생했습니다 ❗";
				alertContent = "키워드 " + String.join(",", keywordSet) + " 발생!!";
				// 응답, 보낸 알림 제목, 보낸 알림 내용, 수신자, 발신자, 발생 브랜치, 비교 url, 알림 타입
				String docId = saveAlertData(alertTitle, alertContent, alertUserKeyword.getUser(), sender,
					headBranch + "->" + baseBranch, url, "push", keywordSet, null, null);

				fcmResponseDto = sendAlert(alertUserKeyword.getUser(), alertTitle, alertContent,
					pullRequestWebhookDto.getPullRequest().getHtmlUrl(), docId);

			}
		}

		// TODO: 커밋 파일별 상세 변경사항 검색

	}    // 사용자+키워드

	private void deleteAlertCondition(User user, AlertEntity alertEntity) throws Exception {

		// alert 테이블에 해당 웹훅 아이디로 만들어진 다른 알람이 없을 때
		List<AlertEntity> sameWebhookAlertList = alertRepository.findByWebhookId(alertEntity.getWebhookId());

		if (sameWebhookAlertList.size() == 1) {
			// 깃허브에서 훅 삭제

			String token = userService.decryptPersonalAccessToken(user.getPersonalAccessToken());
			Repository repository = repositoryService.getRepository(alertEntity.getRepositoryId()).orElseThrow(
				() -> new Exception("deleteAlert :: 알림이 생성된 리포지터리 정보를 찾을 수 없습니다.")
			);

			HttpHeaders headers = new HttpHeaders();

			headers.add("Accept", "application/vnd.github+json");
			headers.add("Authorization", "Bearer " + token);
			headers.add("X-GitHub-Api-Version", "2022-11-28");

			HashMap<String, Object> body = new HashMap<>();

			HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(body, headers);

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<Object> response = restTemplate.exchange(
				"https://api.github.com/repos/" + user.getGithubName() + "/" + repository.getName() + "/hooks/"
					+ alertEntity.getWebhookId(),
				HttpMethod.DELETE,
				entity,
				Object.class
			);
			// log.info("deleteAlert :: 웹훅 삭제 응답 {}", response);

			if (response.getStatusCode().value() == 404) {
				throw new Exception("deleteAlert :: 깃허브에서 해당 웹훅을 찾을 수 없습니다. 삭제 실패");
			}
		}

	}

	public FcmResponseDto sendAlert(User user, String alertTitle, String alertBody, String alertUrl, String docId) throws
		Exception {
		// 기기 토큰 불러오기
		Set<String> token = user.getDeviceToken();

		HashMap<String, Object> message = new HashMap<>();
		HashMap<String, Object> body = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "key=" + SERVER_KEY);

		HashMap<String, String> notificationContentMap = new HashMap<>();

		// notificationContentMap.put("title", String.join(",", titleSet));	// 해당 키워드 셋
		notificationContentMap.put("title", alertTitle);
		notificationContentMap.put("body", alertBody);
		notificationContentMap.put("click_action", alertUrl);
		notificationContentMap.put("icon", docId);

		// HashMap<String, String> data = new HashMap<>();
		// data.put("url", alertUrl);

		body.put("registration_ids", token);
		body.put("notification", notificationContentMap);
		// log.info("data : {}", data);

		// message.put("message", body);

		HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<FcmResponseDto> response = restTemplate.exchange(
			"https://fcm.googleapis.com/fcm/send",
			HttpMethod.POST,
			entity,
			FcmResponseDto.class
		);

		return response.getBody();
	}

}
