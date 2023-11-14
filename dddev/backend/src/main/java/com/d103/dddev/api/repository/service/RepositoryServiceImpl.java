package com.d103.dddev.api.repository.service;

import com.d103.dddev.api.repository.repository.RepositoryRepository;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.repository.repository.entity.Repository;
import com.d103.dddev.api.user.repository.entity.User;
import com.d103.dddev.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RepositoryServiceImpl implements RepositoryService {
	private String API_URL = "https://api.github.com";
	private String REPO_REQUEST_URL = "/user/repos?affiliation=owner";

	private final RepositoryRepository repositoryRepository;
	private final UserService userService;

	// TODO : 없어진 레포지토리 지우고 새로 생긴 거 추가하는 작업 하기
	/**
	 * github api로 repository list 불러오는 함수
	 * */
	@Override
	public List<RepositoryDto> getRepositoryListFromGithub(User user) throws Exception {
		log.info("getRepositoryListFromGithub 진입");
		// 기존 레포지토리 리스트
		// List<Repository> origRepoList = repositoryRepository.findByUser_IdOrderByRepoIdASC(user.getId());

		RestTemplate restTemplate = new RestTemplate();

		// 사용자 pat 가져오기
		String personalAccessToken = userService.getPersonalAccessToken(user);

		// repository list 불러올 url
		String url = API_URL + REPO_REQUEST_URL;

		// 헤더
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + personalAccessToken);

		// entity
		HttpEntity<String> entity = new HttpEntity<>(headers);

		// get 요청
		ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
			url,
			HttpMethod.GET,
			entity,
			new ParameterizedTypeReference<List<Map<String, Object>>>() {
			}
		);

		// api response
		List<Map<String, Object>> repoMap = response.getBody();
		List<RepositoryDto> newRepoList = new ArrayList<>();

		log.info("repository db 저장 진입");
		for (Map<String, Object> repo : repoMap) {
			Repository repository = Repository.builder()
				.name((String)repo.get("name"))
				.user(user)
				.fork((Boolean)repo.get("fork"))
				.repoId((Integer)repo.get("id"))
				.defaultBranch((String)repo.get("default_branch"))
				.isPrivate((Boolean)repo.get("private"))
				.build();

			// repository 조회해서 이름이 바뀌었으면 업데이트하기
			Repository repositoryEntity = getAndUpdateRepository(repository.getRepoId(),
					repository.getName()).orElseGet(() -> saveRepository(repository));

			newRepoList.add(repositoryEntity.convertToDto());
		}

		// TODO : github에서 삭제된 레포지토리가 있을 때 그거 구현하기. 근데 레포 지우기 전에 그라운드 싹다 날려야함 ㅇㅇ

		// // 정렬 완료!
		// Collections.sort(newRepoList);
		//
		// // flag
		// Boolean[] flag = new Boolean[newRepoList.size()];
		//
		// // 기존거에서 수정있는지 확인!
		// for(Repository r : origRepoList) {
		// 	int index = Collections.binarySearch(newRepoList, r);
		//
		// 	if(index >= 0){
		// 		Repository tmp = newRepoList.get(index);	// 새로 받아온거
		// 		flag[index] = true;
		// 		// 이름이 다를 경우
		// 		if (!tmp.getName().equals(r.getName())) {
		// 			r.setName(tmp.getName());
		// 		}
		//
		// 		// 디폴트 브랜치가 다를 경우
		// 		if(!tmp.getDefaultBranch().equals(r.getDefaultBranch())) {
		// 			r.setDefaultBranch(tmp.getDefaultBranch());
		// 		}
		//
		// 		// db에 저장
		// 		repositoryRepository.saveAndFlush(r);
		// 	} else {	// 존재하지 않으면 db에서 삭제하기
		// 		repositoryRepository.delete(r);
		// 	}
		// }
		//
		// // 새로 생긴 repository가 있으면 db에 추가하기~!~!
		// for()


		return newRepoList;
	}

	/**
	 * repoId로 repository 불러오기
	 * */
	@Override
	public Optional<Repository> getRepository(Integer repoId) {
		return repositoryRepository.findByRepoId(repoId);
	}


	/**
	 * repository를 가져와서 이름 변경이 있으면 db에 저장
	 * */
	@Override
	public Optional<Repository> getAndUpdateRepository(Integer repoId, String repoName) {
		Optional<Repository> byRepoId = repositoryRepository.findByRepoId(repoId);
		if (byRepoId.isPresent()) {
			Repository repository = byRepoId.get();
			if (!repository.getName().equals(repoName)) {
				repository.setName(repoName);
				return Optional.of(repositoryRepository.saveAndFlush(repository));
			}
		}
		return byRepoId;
	}

	/**
	 * db에 repository 저장
	 * */
	@Override
	public Repository saveRepository(Repository repository) {
		return repositoryRepository.saveAndFlush(repository);
	}

	@Override
	public RepositoryDto updateIsGround(Repository repository, Boolean isGround) throws Exception {
		repository.setIsGround(isGround);
		return repositoryRepository.saveAndFlush(repository).convertToDto();
	}

}
