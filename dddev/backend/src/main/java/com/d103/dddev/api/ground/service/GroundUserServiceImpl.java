package com.d103.dddev.api.ground.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.d103.dddev.api.ground.repository.GroundUserRepository;
import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.ground.repository.entity.GroundUser;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.user.repository.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroundUserServiceImpl implements GroundUserService {

	private final GroundUserRepository groundUserRepository;

	/**
	 * 그라운드 유저 초대를 위해 email로 검색하기
	 * 이미 추가된 멤버는 제외시킴
	 * */
	@Override
	public List<Map<String, String>> findUserByEmail(Integer groundId, String email) throws Exception {
		log.info("service - findUserByEmail :: email로 사용자 찾기 진입");
		return groundUserRepository.findUserByGroundIdNotAndEmail(
			groundId, email);
	}

	@Override
	public List<GroundUserDto> inviteMemberToGround(Ground ground, User newMember) throws Exception {
		log.info("service - inviteMemberToGround :: 그라운드에 사용자 초대하기 진입");
		// 그라운드 유저 엔티티
		GroundUser newGroundUser = GroundUser.builder()
			.user(newMember)
			.ground(ground)
			.isOwner(false)
			.build();

		// 저장
		groundUserRepository.saveAndFlush(newGroundUser);

		// 그라운드 유저 리스트 조회
		List<GroundUser> groundUserList = groundUserRepository.findByGround_Id(ground.getId());
		List<GroundUserDto> groundUserDtoList = new ArrayList<>();

		// dto로 변환
		for(GroundUser g : groundUserList) {
			groundUserDtoList.add(g.convertToGroundUserDto());
		}
		return groundUserDtoList;
	}

	/**
	 * ground owner 설정
	 * */
	@Override
	public GroundUser updateGroundOwner(Ground ground, User user) throws Exception {
		log.info("service - updateGroundUser :: 그라운드 owner 업데이트 진입");
		GroundUser groundUser = GroundUser.builder()
			.ground(ground)
			.user(user)
			.isOwner(true)
			.build();

		return groundUserRepository.saveAndFlush(groundUser);
	}

	@Override
	public Boolean checkIsGroundOwner(Integer groundId, Integer userId) throws Exception {
		log.info("service - checkIsGroundOwner :: 그라운드 오너 확인");
		Optional<GroundUser> groundUserDtoOptional = groundUserRepository.findByGround_IdAndUser_IdAndIsOwnerIsTrue(
			groundId, userId);
		return groundUserDtoOptional.isPresent();
	}

	@Override
	public Boolean checkIsGroundMember(Integer groundId, Integer userId) throws Exception {
		log.info("service - checkIsGroundMember :: 그라운드 멤버 확인 진입");
		Optional<GroundUser> groundUserDtoOptional = groundUserRepository.findByGround_IdAndUser_Id(
			groundId, userId);

		return groundUserDtoOptional.isPresent();
	}

	@Override
	public List<GroundUser> getGroundMembers(Integer groundId) throws Exception {
		return groundUserRepository.findByGround_Id(groundId);
	}

	@Override
	public List<GroundUserDto> getGroundMembersAsDto(Integer groundId) throws Exception {
		List<GroundUser> groundMemberList = groundUserRepository.findByGround_Id(groundId);
		List<GroundUserDto> groundUserDtoList = new ArrayList<>();

		for(GroundUser g : groundMemberList) {
			if(!g.getUser().getValid())
				continue;

			groundUserDtoList.add(g.convertToGroundUserDto());
		}

		return groundUserDtoList;
	}

	@Override
	public Optional<GroundUser> getGroundMember(Integer groundId, Integer userId) throws Exception {
		return groundUserRepository.findByGround_IdAndUser_Id(groundId, userId);
	}

	@Override
	public void deleteGroundUser(GroundUser groundUser) throws Exception {
		groundUserRepository.delete(groundUser);
	}
}
