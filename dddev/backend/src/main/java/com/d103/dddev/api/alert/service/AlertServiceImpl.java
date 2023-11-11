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

import com.d103.dddev.api.alert.dto.AlertListDto;
import com.d103.dddev.api.alert.dto.AlertResponseDto;
import com.d103.dddev.api.ground.repository.GroundRepository;
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
import com.d103.dddev.api.alert.dto.CreateWebhookResponseDto;
import com.d103.dddev.api.alert.dto.FcmResponseDto;
import com.d103.dddev.api.alert.dto.PushWebhookDto;
import com.d103.dddev.api.alert.dto.UpdateAlertDto;
import com.d103.dddev.api.alert.dto.PullRequestWebhookDto;
import com.d103.dddev.api.alert.entity.AlertDataDocument;
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
	public void initAlert(User user, Repository repository, String type) throws Exception {

		String url = null;

		if (type.equals("push")) {
			url = PUSH_WEBHOOK_URL;
		} else if (type.equals("pull_request")) {
			url = PULL_REQUEST_WEBHOOK_URL;
		} else {
			throw new InvalidTypeException("createWebhook :: 존재하지 않는 알림 타입입니다.");
		}

		CreateWebhookResponseDto createWebhookResponseDto = createWebhook(user, repository, type, url);

		AlertEntity alertEntity = AlertEntity.builder()
			.webhookId(createWebhookResponseDto.getId())
			.createdDate(createWebhookResponseDto.getCreatedAt())
			.repositoryId(repository.getId())
			.keyword(null)
			.type(type)
			.user(null)
			.build();

		alertRepository.save(alertEntity);

	}

	@Override
	public void createAlert(String header, Repository repository, List<String> keyword, String type) throws Exception {

		User user = jwtService.getUser(header).orElseThrow(
			() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

		if (user.getDeviceToken().size() == 0) {
			throw new NoSuchElementException("createWebhook :: 사용자 알림 허용이 필요합니다.");
		}

		Integer repoId = repository.getId();

		// 이미 alertdto가 있는 경우 - repo id, type 비교
		List<AlertEntity> alertEntityOptional = alertRepository.findAllByRepositoryIdAndType(repoId, type);

		Optional<AlertEntity> userAlertDto = alertRepository.findByUser_IdAndRepositoryIdAndType(user.getId(),
			repoId, type);
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

	}

	@Override
	public CreateWebhookResponseDto createWebhook(User user, Repository repository,
		String type, String url) throws Exception {

		String token = userService.decryptPersonalAccessToken(user.getPersonalAccessToken());

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

		ResponseEntity<CreateWebhookResponseDto> response = null;

		// try {
		response = restTemplate.exchange(
			"https://api.github.com/repos/" + user.getGithubName() + "/" + repository.getName() + "/hooks",
			HttpMethod.POST,
			entity,
			CreateWebhookResponseDto.class
		);

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

		Repository repository = repositoryService.getRepository(pushWebhookDto.getRepository().getId()).orElse(null);

		// log.info("sender id :: {}", Integer.valueOf(pushWebhookDto.getSender().get("id")));
		// 트리거 발생자
		UserDto sender = userService.getUserDto(Integer.valueOf(pushWebhookDto.getSender().get("id"))).orElse(null);

		for (AlertUserKeyword alertUserKeyword : userKeyowrdList) {
			FcmResponseDto fcmResponseDto = null;
			String title = null;
			String content = null;
			// 변경될 파일 알려주기 위한 리스트
			List<String> changedFileList = new ArrayList<>();
			// 커밋 메시지 리스트
			List<String> commitMessageList = new ArrayList<>();

			Set<String> keywordSet = new HashSet<>();

			// 커밋 파일 리스트에서 키워드 찾기
			for (CommitDataDto commitDataDto : pushWebhookDto.getCommits()) {

				// 키워드 지정이 없다면
				if (alertUserKeyword.getKeyword().isEmpty()) {
					for (String filename : commitDataDto.getAdded()) {
						changedFileList.add(filename);
					}
					for (String filename : commitDataDto.getRemoved()) {
						changedFileList.add(filename);
					}
					for (String filename : commitDataDto.getModified()) {
						changedFileList.add(filename);
					}

					String commitMsg = commitDataDto.getMessage();

					// 커밋 메시지 검색

					commitMessageList.add(commitMsg);

				} else {    // 키워드 지정 시 검색
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

						// 커밋 메시지 검색
						String commitMsg = commitDataDto.getMessage();

						// 커밋 메시지 검색
						if (commitMsg.contains(keyword)) {
							commitMessageList.add(commitMsg);
							keywordSet.add(keyword);
						}

					} // 키워드
				}
			}    // 커밋 리스트

			if (!changedFileList.isEmpty() && !commitMessageList.isEmpty()) {
				title = "❗ 커밋 메시지가 발생했습니다 ❗\n❗ 파일이 변경되었습니다 ❗";
				content = "키워드 " + String.join(",", keywordSet) + " 발생!!";
				// 응답, 보낸 알림 제목, 보낸 알림 내용, 수신자, 발신자, 발생 브랜치, 비교 url, 알림 타입
				String docId = saveAlertData(title, content, alertUserKeyword.getUser(), sender,
					pushWebhookDto.getRef(), pushWebhookDto.getCompare(), "push", keywordSet, commitMessageList,
					changedFileList, repository);

				fcmResponseDto = sendAlert(alertUserKeyword.getUser(), title, content, pushWebhookDto.getCompare(),
					docId);
			} else if (!changedFileList.isEmpty()) {
				title = "❗ 파일이 변경되었습니다 ❗";
				content = "키워드 " + String.join(",", keywordSet) + " 발생!!";
				// 응답, 보낸 알림 제목, 보낸 알림 내용, 수신자, 발신자, 발생 브랜치, 비교 url, 알림 타입
				String docId = saveAlertData(title, content, alertUserKeyword.getUser(), sender,
					pushWebhookDto.getRef(), pushWebhookDto.getCompare(), "push", keywordSet, null, changedFileList,
					repository);

				fcmResponseDto = sendAlert(alertUserKeyword.getUser(), title, content, pushWebhookDto.getCompare(),
					docId);
			} else if (!commitMessageList.isEmpty()) {
				title = "❗ 커밋 메시지가 발생했습니다 ❗";
				content = "키워드 " + String.join(",", keywordSet) + " 발생!!";

				// 응답, 보낸 알림 제목, 보낸 알림 내용, 수신자, 발신자, 발생 브랜치, 비교 url, 알림 타입
				String docId = saveAlertData(title, content, alertUserKeyword.getUser(), sender,
					pushWebhookDto.getRef(), pushWebhookDto.getCompare(), "push", keywordSet, commitMessageList, null,
					repository);

				fcmResponseDto = sendAlert(alertUserKeyword.getUser(), title, content, pushWebhookDto.getCompare(),
					docId);
			}
		}    // 사용자+키워드

		// 커밋 정보 저장 (키워드 상관 없이 모든 커밋 리스트 저장)
		for (CommitDataDto commitDataDto : pushWebhookDto.getCommits()) {

		}

	}

	private String saveAlertData(String title, String content, User receiver,
		UserDto sender, String branch, String url, String type, Set<String> keywordSet, List<String> commitMessageList,
		List<String> changedFileList, Repository repository) throws Exception {
		// 전송한 알림 내역을 db에 저장
		AlertDataDocument alertDataDocument = AlertDataDocument.builder()
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
			.gitRepoId(repository.getRepoId())
			.gitRepoName(repository.getName())
			.build();

		// log.info("데이터 저장하기 전 로그");

		return alertDataRepo.addAlertData(alertDataDocument);

		// log.info("firestore에 데이터가 정상 저장되었습니다.");
	}

	@Override
	public void updateAlert(User user, UpdateAlertDto updateAlertDto, Integer alertId) throws Exception {

		// 알림 타입도 변경할 수 있게 변경
		AlertEntity alertEntity = alertRepository.findByIdAndUser_Id(alertId, user.getId()).orElseThrow(
			() -> new NoSuchElementException("updateAlert :: 알림이 존재하지 않습니다."));

		// String type = updateAlertDto.getType();
		// Integer repositoryId = alertEntity.getRepositoryId();
		// List<String> keyword = updateAlertDto.getKeyword();
		/*
		 * 알림 타입을 변경할 경우
		 * 삭제하려는 타입 알림이 하나밖에 안 남은 경우 - 웹훅 삭제 필요
		 * 생성하려는 타입 알림이 없는 경우 - 새로운 웹훅 생성 필요
		 * */
		// if (!type.equals(alertEntity.getType())) {
		// 	User user = alertEntity.getUser();
		// 	// 삭제하려는 타입 알림 조회 - 하나뿐이면 웹훅 삭제 -> 삭제 메소드에서 조건 수행
		// 	// List<AlertEntity> existAlertEntityList = alertRepository.findAllByRepositoryIdAndType(repoId, alertEntity.getType());
		// 	// if(existAlertEntityList.size() == 1) {
		// 	// 	deleteAlert(userDto, alertId);
		// 	// }
		//
		// 	deleteAlertCondition(user, alertEntity);
		//
		// 	// 생성하려는 타입 알림 조회
		// 	List<AlertEntity> alertEntityList = alertRepository.findAllByRepositoryIdAndType(repositoryId, type);
		//
		// 	// 생성하려는 타입 알림 웹훅이 이미 존재하는 경우
		// 	// 해당 웹훅 정보 저장하고 수정 저장하고 끝
		// 	if (!alertEntityList.isEmpty()) {
		// 		AlertEntity existAlertEntity = alertEntityList.get(0);
		// 		alertEntity.setWebhookId(existAlertEntity.getWebhookId());
		// 		alertEntity.setType(type);
		// 		alertEntity.setKeyword(keyword);
		// 		//save
		// 		return;
		// 	}
		//
		// 	// 생성하려는 타입 알림 웹훅이 없을 경우 - 새로 생성해야 함
		//
		// 	String url = null;
		//
		// 	if (type.equals("push")) {
		// 		url = PUSH_WEBHOOK_URL;
		// 	} else if (type.equals("pull_request")) {
		// 		url = PULL_REQUEST_WEBHOOK_URL;
		// 	} else {
		// 		throw new InvalidTypeException("updateAlert :: 존재하지 않는 알림 타입입니다.");
		// 	}
		//
		// 	Repository repository = repositoryService.getRepository(repositoryId).orElseThrow(
		// 		() -> new NoSuchElementException("updateAlert :: 존재하지 않는 레포지터리입니다.")
		// 	);
		//
		// 	CreateWebhookResponseDto createWebhookResponseDto = createWebhook(user, repository, keyword, type,
		// 		url);
		//
		// 	alertEntity.setType(type);
		// 	alertEntity.setKeyword(keyword);
		// 	alertEntity.setWebhookId(createWebhookResponseDto.getId());
		// 	return;
		// }

		// 타입 바꾸는게 아니라면 키워드 수정하고 끝
		alertEntity.setKeyword(updateAlertDto.getKeyword());
	}

	@Override
	public List<AlertResponseDto> alertList(User user) throws Exception {
		List<AlertResponseDto> alertListResponseDto = new ArrayList<>();

		List<AlertListDto> list = alertRepository.findAlertEntityAndGroundName(user.getId());

		for (AlertListDto alert : list) {
			List<String> keyword = alertRepository.findById(alert.getId())
				.orElseThrow(() -> new Exception("get keyword :: 알림이 존재하지 않습니다."))
				.getKeyword();
			AlertResponseDto alertResponse = AlertResponseDto.builder()
				.id(alert.getId())
				.keyword(keyword)
				.type(alert.getType())
				.groundName(alert.getGroundName())
				.userId(alert.getUserId())
				.build();
			alertListResponseDto.add(alertResponse);
		}

		return alertListResponseDto;
	}

	@Override
	public void deleteAlert(User user, Integer alertId) throws Exception {

		log.info("alertId, user id :: {} {}", alertId, user.getId());
		// alertId로 웹훅 아이디 찾고, (삭제 - alert 테이블에 해당 웹훅 아이디로 만들어진 다른 알람이 없을 때)
		// 알림 소유자만 삭제할 수 있도록
		AlertEntity alertEntity = alertRepository.findByIdAndUser_Id(alertId, user.getId()).orElseThrow(
			() -> new Exception("deleteAlert :: 해당 알림 정보를 찾을 수 없습니다."));

		// deleteAlertCondition(user, alertEntity);

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

		Repository repository = repositoryService.getRepository(pullRequestWebhookDto.getRepository().getId())
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
					headBranch + "->" + baseBranch, url, "push", keywordSet, null, null, repository);

				fcmResponseDto = sendAlert(alertUserKeyword.getUser(), alertTitle, alertContent,
					pullRequestWebhookDto.getPullRequest().getHtmlUrl(), docId);
			}
		}
	}    // 사용자+키워드

	@Override
	public void deleteWebhook(User user, Repository repository) throws Exception {

		String token = userService.decryptPersonalAccessToken(user.getPersonalAccessToken());
		List<AlertEntity> alertEntityList = alertRepository.findByRepositoryId(repository.getId());

		if (alertEntityList.isEmpty()) {
			throw new NoSuchElementException("deleteWebhook :: 해당 레포에 알림이 존재하지 않습니다.");
		}

		HttpHeaders headers = new HttpHeaders();

		headers.add("Accept", "application/vnd.github+json");
		headers.add("Authorization", "Bearer " + token);
		headers.add("X-GitHub-Api-Version", "2022-11-28");

		HashMap<String, Object> body = new HashMap<>();

		HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();

		try {
			for (AlertEntity alertEntity : alertEntityList) {

				ResponseEntity<Object> response = restTemplate.exchange(
					"https://api.github.com/repos/" + user.getGithubName() + "/" + repository.getName() + "/hooks/"
						+ alertEntity.getWebhookId(),
					HttpMethod.DELETE,
					entity,
					Object.class
				);
			}
		} catch (Exception e) {
			log.error("deleteAlert :: 깃허브에서 해당 웹훅을 찾을 수 없습니다. 삭제 실패");
		}

		log.info("delete webhook finished!");
		log.info("start delete alert list");
		alertRepository.deleteByRepositoryId(repository.getId());
	}

	public FcmResponseDto sendAlert(User user, String alertTitle, String alertBody, String alertUrl,
		String docId) throws
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
