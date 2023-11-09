package com.d103.dddev.api.ground.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.d103.dddev.api.file.service.ProfileService;
import com.d103.dddev.api.ground.repository.GroundRepository;
import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.ground.repository.entity.GroundUser;
import com.d103.dddev.api.issue.service.IssueService;
import com.d103.dddev.api.issue.util.UndefinedUtil;
import com.d103.dddev.api.repository.repository.entity.Repository;
import com.d103.dddev.api.repository.service.RepositoryService;
import com.d103.dddev.api.user.repository.entity.User;
import com.d103.dddev.api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroundServiceImpl implements GroundService {

	private final GroundRepository groundRepository;
	private final GroundUserService groundUserService;
	private final UserService userService;
	private final ProfileService profileService;
	private final RepositoryService repositoryService;
	private final IssueService issueService;
	private final UndefinedUtil undefinedUtil;
	private final Integer DEFAULT_GROUND_FOCUS_TIME = 5;
	private final Integer DEFAULT_GROUND_ACTIVE_TIME = 3;

	/**
	 * 그라운드 생성
	 * 1. groundDto에 repository 연결
	 * 2. groundUserDto owner 설정
	 * */
	@Override
	@Transactional
	public Ground createGround(String groundName, User user, Repository repository) throws Exception {
		log.info("service - createGround :: 그라운드 생성 진입");
		Ground ground = Ground.builder()
			.name(groundName)
			.focusTime(DEFAULT_GROUND_FOCUS_TIME)
			.activeTime(DEFAULT_GROUND_ACTIVE_TIME)
			.build();

		ground.setRepository(repository);
		ground = groundRepository.saveAndFlush(ground);

		// 그라운드 오너 update하기
		groundUserService.updateGroundOwner(ground, user);

		// 생성자의 최근 방문 ground를 생성한 그라운드로 하기
		userService.updateLastVisitedGround(ground.getId(), user);

		/*
		 *
		 * 그라운드 생성 시 미분류 문서 생성(목표, 체크 포인트)
		 *
		 * */

		undefinedUtil.createUndefined(ground);

		return ground;
	}

	@Override
	public Optional<Ground> getGroundByRepoId(Integer repoId) throws Exception {
		log.info("service - 레포지토리 아이디로 ground 조회 진입");
		return groundRepository.findByRepository_Id(repoId);
	}

	@Override
	public Optional<Ground> getGroundInfo(Integer groundId) throws Exception {
		log.info("service - getGroundInfo :: 그라운드 조회 진입");
		return groundRepository.findById(groundId);
	}

	@Override
	public Map<String, Integer> getGroundFocusTime(Integer groundId, Integer sprintId) throws
		Exception {
		return issueService.getGroundFocusTime(groundId, sprintId);
	}

	@Override
	public Map<String, Integer> getGroundActiveTime(Integer groundId, Integer sprintId) throws
		Exception {
		return issueService.getGroundActiveTime(groundId, sprintId);
	}

	@Override
	public Map<String, Integer> getGroundTotalTime(Integer groundId, Integer sprintId) throws Exception {
		return issueService.getGroundTotalTime(groundId, sprintId);
	}

	@Override
	public Map<String, Integer> getGroundFocusTimeCount(Integer groundId, Integer sprintId) throws Exception {
		return issueService.getGroundFocusTimeCount(groundId, sprintId);
	}

	@Override
	public Map<String, Integer> getGroundActiveTimeCount(Integer groundId, Integer sprintId) throws Exception {
		return issueService.getGroundActiveTimeCount(groundId, sprintId);
	}

	@Override
	public Map<String, Integer> getGroundTotalTimeCount(Integer groundId, Integer sprintId) throws Exception {
		return issueService.getGroundTotalTimeCount(groundId, sprintId);
	}

	@Override
	public Ground updateGroundInfo(Ground newGround, Ground ground) throws Exception {
		log.info("service - updateGroundInfo :: 그라운드 이름 수정 진입");
		// dto 업데이트하기
		ground.setName(newGround.getName());
		ground.setFocusTime(newGround.getFocusTime());
		ground.setActiveTime(newGround.getActiveTime());

		return groundRepository.saveAndFlush(ground);
	}

	@Override
	public Ground updateGroundProfile(MultipartFile file, Ground ground) throws Exception {
		log.info("service - modifyGroundProfile :: 그라운드 프로필 사진 수정 진입");

		// 기존 프로필
		ProfileDto prevProfile = ground.getProfileDto();

		// 새 프로필 사진 서버/db에 저장
		ProfileDto newProfile = profileService.saveGroundProfile(file);

		// 새 프로필 사진 userDto에 저장
		ground.setProfileDto(newProfile);
		ground = groundRepository.saveAndFlush(ground);

		// 기존 프로필 사진 서버/db에서 삭제
		if (prevProfile != null) {
			profileService.deleteProfile(prevProfile);
		}

		return ground;
	}

	@Override
	public Ground deleteGroundProfile(Ground ground) throws Exception {
		log.info("service - deleteGroundProfile :: 그라운드 프로필 사진 삭제 진입");
		// 그라운드 프로필 dto
		ProfileDto profileDto = ground.getProfileDto();

		ground.setProfileDto(null);
		ground = groundRepository.saveAndFlush(ground);

		// 프로필 사진 서버/db에서 삭제
		if (profileDto != null) {
			profileService.deleteProfile(profileDto);
		}

		return ground;
	}

	@Override
	public void deleteMemberFromGround(Ground ground, User user) throws Exception {
		log.info("service - deleteMemberFromGround :: 그라운드 멤버 나가기 진입");

		GroundUser groundUser = groundUserService.getGroundMember(ground.getId(), user.getId())
			.orElseThrow(() -> new NoSuchElementException("그라운드에 해당 사용자가 존재하지 않습니다."));

		groundUserService.deleteGroundUser(groundUser);

	}

	// TODO :: ground에 속한 이슈, 문서, 리퀘스트 모두 삭제하기
	@Override
	public void deleteGround(Ground ground) throws Exception {
		log.info("service - deleteGround :: 그라운드 삭제 진입");

		// 프로필 사진 받아오기
		ProfileDto profileDto = ground.getProfileDto();

		// 레포지토리 받아오기
		Repository repository = ground.getRepository();

		// 이슈 리스트 받아오기

		// 문서 리스트 받아오기

		// 리퀘스트 리스트 받아오기

		// 스프린트 받아오기

		// 차트 데이터 받아오기

		// 그라운드 멤버 리스트 받아오기
		List<GroundUser> groundMembers = groundUserService.getGroundMembers(ground.getId());

		// 그라운드 멤버 리스트 db에서 삭제하기
		for (GroundUser g : groundMembers) {
			groundUserService.deleteGroundUser(g);
		}

		// 그라운드 삭제
		groundRepository.delete(ground);

		// 서버/db에서 프로필 사진 삭제
		if (profileDto != null) {
			profileService.deleteProfile(profileDto);
		}

		// 레포지토리 is_ground = false로 변경
		repositoryService.updateIsGround(repository, false);

		// 이슈 삭제

		// 문서 삭제

		// 리퀘스트 삭제

		// 스프린트 삭제

		// 차트 데이터 삭제하기

	}
}
