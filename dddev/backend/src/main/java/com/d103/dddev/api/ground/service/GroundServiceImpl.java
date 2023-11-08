package com.d103.dddev.api.ground.service;

import java.util.HashMap;
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
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.repository.IssueRepository;
import com.d103.dddev.api.issue.service.IssueService;
import com.d103.dddev.api.issue.util.UndefinedUtil;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.repository.service.RepositoryService;
import com.d103.dddev.api.user.repository.dto.UserDto;
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
	public GroundDto createGround(String groundName, UserDto userDto, RepositoryDto repositoryDto) throws Exception {
		log.info("service - createGround :: 그라운드 생성 진입");
		GroundDto groundDto = GroundDto.builder()
			.name(groundName)
			.focusTime(DEFAULT_GROUND_FOCUS_TIME)
			.activeTime(DEFAULT_GROUND_ACTIVE_TIME)
			.build();

		groundDto.setRepositoryDto(repositoryDto);
		groundDto = groundRepository.saveAndFlush(groundDto);

		// 그라운드 오너 update하기
		groundUserService.updateGroundOwner(groundDto, userDto);

		// 생성자의 최근 방문 ground를 생성한 그라운드로 하기
		userService.updateLastVisitedGround(groundDto.getId(), userDto);

		/*
		 *
		 * 그라운드 생성 시 미분류 문서 생성(목표, 체크 포인트)
		 *
		 * */

		undefinedUtil.createUndefined(groundDto);

		return groundDto;
	}

	@Override
	public Optional<GroundDto> getGroundByRepoId(Integer repoId) throws Exception {
		log.info("service - 레포지토리 아이디로 ground 조회 진입");
		return groundRepository.findByRepositoryDto_Id(repoId);
	}

	@Override
	public Optional<GroundDto> getGroundInfo(Integer groundId) throws Exception {
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
	public Map<String, Long> getGroundFocusTimeCount(Integer groundId, Integer sprintId) throws Exception {
		return issueService.getGroundFocusTimeCount(groundId, sprintId);
	}

	@Override
	public Map<String, Long> getGroundActiveTimeCount(Integer groundId, Integer sprintId) throws Exception {
		return issueService.getGroundActiveTimeCount(groundId, sprintId);
	}

	@Override
	public Map<String, Long> getGroundTotalTimeCount(Integer groundId, Integer sprintId) throws Exception {
		return issueService.getGroundTotalTimeCount(groundId, sprintId);
	}

	@Override
	public GroundDto updateGroundInfo(GroundDto newGroundDto, GroundDto groundDto) throws Exception {
		log.info("service - updateGroundInfo :: 그라운드 이름 수정 진입");
		// dto 업데이트하기
		groundDto.setName(newGroundDto.getName());
		groundDto.setFocusTime(newGroundDto.getFocusTime());
		groundDto.setActiveTime(newGroundDto.getActiveTime());

		return groundRepository.saveAndFlush(groundDto);
	}

	@Override
	public GroundDto updateGroundProfile(MultipartFile file, GroundDto groundDto) throws Exception {
		log.info("service - modifyGroundProfile :: 그라운드 프로필 사진 수정 진입");

		// 기존 프로필
		ProfileDto prevProfile = groundDto.getProfileDto();

		// 새 프로필 사진 서버/db에 저장
		ProfileDto newProfile = profileService.saveGroundProfile(file);

		// 새 프로필 사진 userDto에 저장
		groundDto.setProfileDto(newProfile);
		groundDto = groundRepository.saveAndFlush(groundDto);

		// 기존 프로필 사진 서버/db에서 삭제
		if (prevProfile != null) {
			profileService.deleteProfile(prevProfile);
		}

		return groundDto;
	}

	@Override
	public GroundDto deleteGroundProfile(GroundDto groundDto) throws Exception {
		log.info("service - deleteGroundProfile :: 그라운드 프로필 사진 삭제 진입");
		// 그라운드 프로필 dto
		ProfileDto profileDto = groundDto.getProfileDto();

		groundDto.setProfileDto(null);
		groundDto = groundRepository.saveAndFlush(groundDto);

		// 프로필 사진 서버/db에서 삭제
		if (profileDto != null) {
			profileService.deleteProfile(profileDto);
		}

		return groundDto;
	}

	@Override
	public void deleteMemberFromGround(GroundDto groundDto, UserDto userDto) throws Exception {
		log.info("service - deleteMemberFromGround :: 그라운드 멤버 나가기 진입");

		GroundUserDto groundUserDto = groundUserService.getGroundMember(groundDto.getId(), userDto.getId())
			.orElseThrow(() -> new NoSuchElementException("그라운드에 해당 사용자가 존재하지 않습니다."));

		groundUserService.deleteGroundUser(groundUserDto);

	}

	// TODO :: ground에 속한 이슈, 문서, 리퀘스트 모두 삭제하기
	@Override
	public void deleteGround(GroundDto groundDto) throws Exception {
		log.info("service - deleteGround :: 그라운드 삭제 진입");

		// 프로필 사진 받아오기
		ProfileDto profileDto = groundDto.getProfileDto();

		// 레포지토리 받아오기
		RepositoryDto repositoryDto = groundDto.getRepositoryDto();

		// 이슈 리스트 받아오기

		// 문서 리스트 받아오기

		// 리퀘스트 리스트 받아오기

		// 스프린트 받아오기

		// 차트 데이터 받아오기

		// 그라운드 멤버 리스트 받아오기
		List<GroundUserDto> groundMembers = groundUserService.getGroundMembers(groundDto.getId());

		// 그라운드 멤버 리스트 db에서 삭제하기
		for (GroundUserDto g : groundMembers) {
			groundUserService.deleteGroundUser(g);
		}

		// 그라운드 삭제
		groundRepository.delete(groundDto);

		// 서버/db에서 프로필 사진 삭제
		if (profileDto != null) {
			profileService.deleteProfile(profileDto);
		}

		// 레포지토리 is_ground = false로 변경
		repositoryService.updateIsGround(repositoryDto, false);

		// 이슈 삭제

		// 문서 삭제

		// 리퀘스트 삭제

		// 스프린트 삭제

		// 차트 데이터 삭제하기

	}
}
