package com.d103.dddev.api.repository.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d103.dddev.api.common.oauth2.utils.JwtService;
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
@RequestMapping("/repository")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"레포지토리 API"})
public class RepositoryController {

	private final RepositoryService repositoryService;
	private final JwtService jwtService;

	@GetMapping("/list")
	@ApiOperation(value = "사용자 레포지토리 목록 조회", notes = "사용자 레포지토리 목록 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<List<RepositoryDto>> getRepositoryList(@RequestHeader String Authorization) {
		try {
			log.info("controller - getRepositoryList :: 사용자 레포지토리 목록 조회 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));
			return new ResponseEntity<>(repositoryService.getRepositoryListFromGithub(userDto), HttpStatus.OK);
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

	@GetMapping("/{repoId}/files")
	@ApiOperation(value = "레포지토리 파일 목록 조회", notes = "레포지토리 파일 목록 조회 API")
	ResponseEntity<?> getRepositoryFiles(@PathVariable Integer repoId, @RequestHeader String Authorization) {
		return null;
	}
}
