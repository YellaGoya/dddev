package com.d103.dddev.api.ground.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.d103.dddev.api.common.oauth2.utils.AesType;
import com.d103.dddev.api.common.oauth2.utils.AesUtil;
import com.d103.dddev.api.ground.repository.GroundUserRepository;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.dto.GroundTokenDto;
import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.ground.repository.entity.GroundUser;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.user.repository.entity.User;
import com.d103.dddev.api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroundUserServiceImpl implements GroundUserService {

	private final GroundUserRepository groundUserRepository;
	private final UserService userService;
	private final AesUtil aesUtil;

	@Value("${ip.log.server}")
	private String LOG_SERVER_URI;

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
		// 이미 초대된 멤버인지 확인하기
		if(checkIsGroundMember(ground.getId(), newMember.getId())){
			throw new EntityExistsException("이미 존재하는 회원입니다.");
		}

		// 그라운드 유저 엔티티
		GroundUser newGroundUser = GroundUser.builder()
			.user(newMember)
			.ground(ground)
			.isOwner(false)
			.build();

		// 저장
		groundUserRepository.saveAndFlush(newGroundUser);

		// 만약 새로 초대된 유저의 last ground id가 null일 때 last ground id를 현재 그라운드로 바꾼다.
		if(newMember.getLastGroundId() == null) {
			userService.updateLastVisitedGround(ground.getId(), newMember);
		}

		// 그라운드 유저 리스트 조회
		List<GroundUser> groundUserList = groundUserRepository.findByGround_Id(ground.getId());
		List<GroundUserDto> groundUserDtoList = new ArrayList<>();

		// dto로 변환
		for(GroundUser g : groundUserList) {
			groundUserDtoList.add(g.convertToGroundUserDto());
		}
		return groundUserDtoList;
	}

	@Override
	public String createGroundUserToken(Integer groundId, User user) throws Exception {
		// 토큰 생성
		String token = groundId + "/" + user.getId() + "/" + UUID.randomUUID();
		// String token = groundId + user.getId() + UUID.randomUUID().toString();
		// getUserInfo(token);

		// 토큰 암호화
		String encryptToken = aesUtil.aes256Encrypt(token, AesType.LOG);

		// uri
		String uri = LOG_SERVER_URI+"/log/auth";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		// params.add("token", Base64Utils.encodeToUrlSafeString(encryptToken.getBytes()));
		GroundTokenDto param = GroundTokenDto.builder()
			.token(encryptToken)
			.build();

		HttpEntity<GroundTokenDto> entity = new HttpEntity<>(param, headers);

		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
			uri,
			HttpMethod.POST,
			entity,
			new ParameterizedTypeReference<Map<String, Object>>() {
			}
		);

		return (String)response.getBody().get("message");
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
