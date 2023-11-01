package com.d103.dddev.api.ground.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.d103.dddev.api.file.service.ProfileService;
import com.d103.dddev.api.ground.repository.GroundRepository;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
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
	private final Integer DEFAULT_GROUND_IMG_ID = 2;

	/**
	 * 그라운드 생성
	 * 1. groundDto에 repository 연결
	 * 2. groundUserDto owner 설정
	 * */
	@Override
	public GroundDto createGround(String groundName, UserDto userDto, RepositoryDto repositoryDto) throws Exception {
		log.info("service - createGround :: 그라운드 생성 진입");
		GroundDto groundDto = GroundDto.builder()
			.name(groundName)
			.build();

		groundDto.setRepositoryDto(repositoryDto);
		groundDto = groundRepository.saveAndFlush(groundDto);

		// 그라운드 오너 update하기
		groundUserService.updateGroundOwner(groundDto, userDto);

		// 생성자의 최근 방문 ground를 생성한 그라운드로 하기
		userService.modifyLastVisitedGround(groundDto.getId(), userDto);
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
	public GroundDto updateGround(GroundDto groundDto, GroundDto newGroundDto) throws Exception {
		log.info("service - modifyGroundName :: 그라운드 이름 수정 진입");
		// dto 업데이트하기
		groundDto.setName(newGroundDto.getName());
		groundDto.setFocusTime(newGroundDto.getFocusTime());
		groundDto.setActiveTime(newGroundDto.getActiveTime());

		return groundRepository.saveAndFlush(groundDto);
	}

	@Override
	public GroundDto updateGroundProfile(GroundDto groundDto, MultipartFile file) throws Exception {
		log.info("service - modifyGroundProfile :: 그라운드 프로필 사진 수정 진입");

		// 기존 프로필
		ProfileDto prevProfile = groundDto.getProfileDto();

		// 새 프로필 사진 서버/db에 저장
		ProfileDto newProfile = profileService.saveGroundProfile(file);

		// 새 프로필 사진 userDto에 저장
		groundDto.setProfileDto(newProfile);

		// 기존 프로필 사진 서버/db에서 삭제
		if(prevProfile != null && prevProfile.getId() != DEFAULT_GROUND_IMG_ID) {
			profileService.deleteProfile(prevProfile);
		}

		return groundRepository.saveAndFlush(groundDto);
	}

	@Override
	public GroundDto deleteGroundProfile(GroundDto groundDto) throws Exception {
		log.info("service - deleteGroundProfile :: 그라운드 프로필 사진 삭제 진입");
		// 그라운드 프로필 dto
		ProfileDto profileDto = groundDto.getProfileDto();

		// 기본 프로필 사진 받아오기
		ProfileDto defaultProfile = profileService.getProfileDto(DEFAULT_GROUND_IMG_ID);

		groundDto.setProfileDto(defaultProfile);

		// 프로필 사진 서버/db에서 삭제
		if(profileDto != null && profileDto.getId() != DEFAULT_GROUND_IMG_ID) {
			profileService.deleteProfile(profileDto);
		}

		return groundRepository.saveAndFlush(groundDto);
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

		// 차트 데이터 받아오기

		// 그라운드 삭제
		groundRepository.delete(groundDto);

		// 서버/db에서 프로필 사진 삭제
		if(profileDto != null && profileDto.getId() != DEFAULT_GROUND_IMG_ID) {
			profileService.deleteProfile(profileDto);
		}

		// 레포지토리 is_ground = false로 변경
		repositoryService.updateIsGround(repositoryDto, false);

		// 이슈 삭제

		// 문서 삭제

		// 리퀘스트 삭제

		// 차트 데이터 삭제하기
	}
}
