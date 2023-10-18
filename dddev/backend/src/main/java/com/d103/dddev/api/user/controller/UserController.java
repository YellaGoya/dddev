package com.d103.dddev.api.user.controller;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.d103.dddev.api.file.service.ProfileService;
import com.d103.dddev.api.user.repository.dto.UserDto;
import com.d103.dddev.api.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"사용자 API"})
public class UserController {

	private final UserService userService;
	private final ProfileService profileService;
	private final JwtService jwtService;

	@GetMapping("/user-info")
	@ApiOperation(value = "사용자 정보", notes = "사용자 정보를 받아오는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<UserDto> getUserInfo(@RequestHeader String Authorization) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));
			return new ResponseEntity<>(userDto, HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/profile")
	@ApiOperation(value = "사용자 프로필 사진", notes = "사용자 프로필 사진을 받아오는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자 혹은 프로필 사진"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<byte[]> getUserProfile(@RequestHeader String Authorization) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			ProfileDto profileDto = userDto.getProfileDto();

			// 파일 확장자에 따라 파일 헤더 세팅
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", profileDto.getContentType());

			byte[] profile = profileService.getProfileById(profileDto.getId());

			return new ResponseEntity<>(profile, headers, HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/nickname/duplicate")
	@ApiOperation(value = "닉네임 중복 조회", notes = "닉네임 중복 조회 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<Boolean> checkDupNickname(@ApiParam(value = "중복 체크할 닉네임") @RequestParam String nickname,
		@RequestHeader String Authorization) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			return new ResponseEntity<>(userService.checkDupNickname(nickname, userDto.getId()), HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/nickname")
	@ApiOperation(value = "사용자 닉네임 수정", notes = "사용자 닉네임 수정 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<UserDto> modifyNickname(@ApiParam(value = "유저 닉네임") Map<String, String> nicknameMap,
		@RequestHeader String Authorization) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));
			String newNickname = nicknameMap.get("nickname");
			return new ResponseEntity<>(userService.modifyNickname(newNickname, userDto), HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@ApiOperation(value = "사용자 프로필 사진 수정", notes = "사용자 프로필 사진 수정 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "파일 저장 에러"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<UserDto> modifyProfile(@RequestPart("file") MultipartFile file,
		@RequestHeader String Authorization) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			return new ResponseEntity<>(userService.modifyProfile(file, userDto), HttpStatus.OK);

		} catch (NoSuchFieldException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/profile")
	@ApiOperation(value = "사용자 프로필 사진 삭제", notes = "사용자 프로필 사진 삭제 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "파일 저장 에러"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<UserDto> deleteProfile(@RequestHeader String Authorization) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			return new ResponseEntity<>(userService.deleteProfile(userDto), HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping
	@ApiOperation(value = "사용자 탈퇴", notes = "사용자 탈퇴 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "파일 저장 에러"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<String> deleteUser(@RequestHeader String Authorization) {
		try {


			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			userService.deleteUser(userDto);
			return new ResponseEntity<>("사용자 탈퇴 성공!", HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("사용자 탈퇴 실패..", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
