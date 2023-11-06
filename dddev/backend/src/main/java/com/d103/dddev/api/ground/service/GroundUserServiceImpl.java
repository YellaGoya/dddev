package com.d103.dddev.api.ground.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.d103.dddev.api.ground.repository.GroundUserRepository;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.ground.vo.GroundUserVO;
import com.d103.dddev.api.user.repository.dto.UserDto;

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
	public List<GroundUserVO> inviteMemberToGround(GroundDto groundDto, UserDto newMember) throws Exception {
		log.info("service - inviteMemberToGround :: 그라운드에 사용자 초대하기 진입");
		GroundUserDto newGroundUserDto = GroundUserDto.builder()
			.userDto(newMember)
			.groundDto(groundDto)
			.isOwner(false)
			.build();

		groundUserRepository.saveAndFlush(newGroundUserDto);

		List<GroundUserDto> groundUserDtoList = groundUserRepository.findByGroundDto_Id(groundDto.getId());
		List<GroundUserVO> groundUserVOList = new ArrayList<>();

		for(GroundUserDto g : groundUserDtoList) {
			GroundUserVO userVO = GroundUserVO.builder()
				.isOwner(g.getIsOwner())
				.userId(g.getUserDto().getId())
				.profileDto(g.getUserDto().getProfileDto())
				.githubId(g.getUserDto().getGithubId())
				.nickname(g.getUserDto().getNickname())
				.statusMsg(g.getUserDto().getStatusMsg())
				.build();

			groundUserVOList.add(userVO);
		}
		return groundUserVOList;
	}

	/**
	 * ground owner 설정
	 * */
	@Override
	public GroundUserDto updateGroundOwner(GroundDto groundDto, UserDto userDto) throws Exception {
		log.info("service - updateGroundUser :: 그라운드 owner 업데이트 진입");
		GroundUserDto groundUserDto = GroundUserDto.builder()
			.groundDto(groundDto)
			.userDto(userDto)
			.isOwner(true)
			.build();

		return groundUserRepository.saveAndFlush(groundUserDto);
	}

	@Override
	public Boolean checkIsGroundOwner(Integer groundId, Integer userId) throws Exception {
		log.info("service - checkIsGroundOwner :: 그라운드 오너 확인");
		Optional<GroundUserDto> groundUserDtoOptional = groundUserRepository.findByGroundDto_IdAndUserDto_IdAndIsOwnerIsTrue(
			groundId, userId);
		return groundUserDtoOptional.isPresent();
	}

	@Override
	public Boolean checkIsGroundMember(Integer groundId, Integer userId) throws Exception {
		log.info("service - checkIsGroundMember :: 그라운드 멤버 확인 진입");
		Optional<GroundUserDto> groundUserDtoOptional = groundUserRepository.findByGroundDto_IdAndUserDto_Id(
			groundId, userId);

		return groundUserDtoOptional.isPresent();
	}

	@Override
	public List<GroundUserDto> getGroundMembers(Integer groundId) throws Exception {
		return groundUserRepository.findByGroundDto_Id(groundId);
	}

	@Override
	public List<GroundUserVO> getGroundMembersAsVO(Integer groundId) throws Exception {
		List<GroundUserDto> groundMemberList = groundUserRepository.findByGroundDto_Id(groundId);
		List<GroundUserVO> groundUserVOList = new ArrayList<>();

		for(GroundUserDto g : groundMemberList) {
			if(!g.getUserDto().getValid())
				continue;

			GroundUserVO groundUserVO = GroundUserVO.builder()
				.isOwner(g.getIsOwner())
				.userId(g.getUserDto().getId())
				.profileDto(g.getUserDto().getProfileDto())
				.githubId(g.getUserDto().getGithubId())
				.nickname(g.getUserDto().getNickname())
				.statusMsg(g.getUserDto().getStatusMsg())
				.build();

			groundUserVOList.add(groundUserVO);
		}

		return groundUserVOList;
	}

	@Override
	public Optional<GroundUserDto> getGroundMember(Integer groundId, Integer userId) throws Exception {
		return groundUserRepository.findByGroundDto_IdAndUserDto_Id(groundId, userId);
	}

	@Override
	public void deleteGroundUser(GroundUserDto groundUserDto) throws Exception {
		groundUserRepository.delete(groundUserDto);
	}
}
