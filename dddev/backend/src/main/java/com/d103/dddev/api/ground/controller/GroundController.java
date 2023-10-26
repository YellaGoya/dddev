package com.d103.dddev.api.ground.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.service.GroundService;
import com.d103.dddev.api.ground.service.GroundUserService;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.repository.service.RepositoryService;
import com.d103.dddev.api.user.repository.dto.UserDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ground")
@RequiredArgsConstructor
@Api(tags = {"그라운드 API"})
@Slf4j
public class GroundController {

	private final GroundService groundService;
	private final GroundUserService groundUserService;
	private final RepositoryService repositoryService;
	private final JwtService jwtService;

	@PostMapping("/{repoId}")
	@ApiOperation(value = "그라운드 생성", notes = "그라운드 생성하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<GroundDto> createGround(@RequestHeader String Authorization,
		@PathVariable Integer repoId, @RequestBody GroundDto groundDto) {
		try {
			log.info("createGround :: 그라운드 생성 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			// 1. 연결할 레포지토리 불러오기
			RepositoryDto repositoryDto = repositoryService.getRepository(repoId)
				.orElseThrow(() -> new NoSuchElementException("getRepository :: 존재하지 않는 레포지토리입니다."));

			repositoryDto.setIsGround(true);
			repositoryDto = repositoryService.saveRepository(repositoryDto);

			// 2. 그라운드 생성하기
			return new ResponseEntity<>(groundService.createGround(groundDto, userDto, repositoryDto), HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}")
	@ApiOperation(value = "그라운드 정보 조회", notes = "그라운드 정보를 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<GroundDto> getGroundInfo(@PathVariable Integer groundId, @RequestHeader String Authorization) {
		try {
			log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			// 1. 사용자가 해당 그라운드의 멤버인지 확인
			System.out.println(groundUserService.checkIsGroundMember(groundId, userDto.getId()));
			// 2. 그라운드 정보 조회

			return null;
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/is-owner")
	@ApiOperation(value = "그라운드 owner 확인", notes = "그라운드의 owner인지 확인하는 API")
	ResponseEntity<Boolean> checkIsGroundOwner(@PathVariable Integer groundId, @RequestHeader String Authorization) {
		try {
			log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			// 1. 사용자가 해당 그라운드의 멤버인지 확인
			System.out.println(groundUserService.checkIsGroundOwner(groundId, userDto.getId()));

			// 2. 그라운드 정보 조회


			return null;
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
