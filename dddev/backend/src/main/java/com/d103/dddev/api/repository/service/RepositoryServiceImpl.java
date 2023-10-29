package com.d103.dddev.api.repository.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.d103.dddev.api.repository.repository.RepositoryRepository;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.user.repository.dto.UserDto;
import com.d103.dddev.api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepositoryServiceImpl implements RepositoryService {
	private String API_URL = "https://api.github.com";
	private String REPO_REQUEST_URL = "/user/repos?affiliation=owner";

	private final RepositoryRepository repositoryRepository;
	private final UserService userService;

	/**
	 * github api로 repository list 불러오는 함수
	 * */
	@Override
	public List<RepositoryDto> getRepositoryListFromGithub(UserDto userDto) throws Exception {
		log.info("getRepositoryListFromGithub 진입");
		RestTemplate restTemplate = new RestTemplate();

		String personalAccessToken = userService.getPersonalAccessToken(userDto);

		String url = API_URL + REPO_REQUEST_URL;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + personalAccessToken);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
			url,
			HttpMethod.GET,
			entity,
			new ParameterizedTypeReference<List<Map<String, Object>>>() {
			}
		);

		List<Map<String, Object>> repoMap = response.getBody();
		List<RepositoryDto> repoList = new ArrayList<>();

		log.info("repository db 저장 진입");
		for (Map<String, Object> repo : repoMap) {
			RepositoryDto repositoryDto = RepositoryDto.builder()
				.name((String)repo.get("name"))
				.userDto(userDto)
				.fork((Boolean)repo.get("fork"))
				.repoId((Integer)repo.get("id"))
				.defaultBranch((String)repo.get("default_branch"))
				.isPrivate((Boolean)repo.get("private"))
				.build();

			// repository 조회해서 이름이 바뀌었으면 업데이트하기
			RepositoryDto repository = getAndUpdateRepository(repositoryDto.getRepoId(),
				repositoryDto.getName()).orElseGet(() -> saveRepository(repositoryDto));
			repoList.add(repository);
		}
		return repoList;
	}

	/**
	 * repoId로 repository 불러오기
	 * */
	@Override
	public Optional<RepositoryDto> getRepository(Integer repoId) {
		return repositoryRepository.findByRepoId(repoId);
	}

	/**
	 * repository를 가져와서 이름 변경이 있으면 db에 저장
	 * */
	@Override
	public Optional<RepositoryDto> getAndUpdateRepository(Integer repoId, String repoName) {
		Optional<RepositoryDto> byRepoId = repositoryRepository.findByRepoId(repoId);
		if (byRepoId.isPresent()) {
			RepositoryDto repositoryDto = byRepoId.get();
			if (!repositoryDto.getName().equals(repoName)) {
				repositoryDto.setName(repoName);
				return Optional.of(repositoryRepository.saveAndFlush(repositoryDto));
			}
		}
		return byRepoId;
	}

	/**
	 * db에 repository 저장
	 * */
	@Override
	public RepositoryDto saveRepository(RepositoryDto repositoryDto) {
		return repositoryRepository.saveAndFlush(repositoryDto);
	}

}
