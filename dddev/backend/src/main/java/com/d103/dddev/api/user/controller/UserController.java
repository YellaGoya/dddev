package com.d103.dddev.api.user.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.ground.vo.GroundVO;
import com.d103.dddev.api.user.repository.dto.UserDto;
import com.d103.dddev.api.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"사용자 API"})
public class UserController {

	private final UserService userService;
	private final JwtService jwtService;

	@GetMapping("/user-info")
	@ApiOperation(value = "사용자 정보", notes = "사용자 정보를 받아오는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<UserDto>> getUserInfo(@RequestHeader String Authorization) {
		log.info("controller - getUserInfo :: 사용자 정보 받아오기 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));
			ResponseVO<UserDto> responseVO = ResponseVO.<UserDto>builder()
				.code(HttpStatus.OK.value())
				.message("사용자 정보 조회 성공")
				.data(userDto)
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<UserDto> responseVO = ResponseVO.<UserDto>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<UserDto> responseVO = ResponseVO.<UserDto>builder()
				.code(HttpStatus.FORBIDDEN.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<UserDto> responseVO = ResponseVO.<UserDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/profile")
	@ApiOperation(value = "사용자 프로필 사진", notes = "사용자 프로필 사진을 받아오는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자 혹은 프로필 사진"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<byte[]>> getUserProfile(@RequestHeader String Authorization) {
		log.info("controller - getUserprofile :: 사용자 프로필 받아오기 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			ProfileDto profileDto = userDto.getProfileDto();

			// 파일 확장자에 따라 파일 헤더 세팅
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", profileDto.getContentType());

			byte[] profile = userService.getProfile(userDto);

			ResponseVO<byte[]> responseVO = ResponseVO.<byte[]>builder()
				.code(HttpStatus.OK.value())
				.message("프로필 조회 성공!")
				.data(profile)
				.build();

			return new ResponseEntity<>(responseVO, headers, HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<byte[]> responseVO = ResponseVO.<byte[]>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<byte[]> responseVO = ResponseVO.<byte[]>builder()
				.code(HttpStatus.FORBIDDEN.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<byte[]> responseVO = ResponseVO.<byte[]>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/ground/list")
	@ApiOperation(value = "사용자가 가입한 그라운드 목록 조회", notes = "사용자가 가입한 그라운드 목록을 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<List<GroundVO>>> getGroundList(@RequestHeader String Authorization) {
		log.info("controller - getGroundList :: 사용자가 가입한 그라운드 목록 조회 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			List<GroundUserDto> groundUserDtoList = userService.getGroundList(userDto);
			List<GroundVO> groundVOList = new ArrayList<>();

			for(GroundUserDto g : groundUserDtoList) {
				GroundVO groundVO = GroundVO.builder()
					.isOwner(g.getIsOwner())
					.groundDto(g.getGroundDto())
					.build();

				groundVOList.add(groundVO);
			}

			ResponseVO<List<GroundVO>> responseVO = ResponseVO.<List<GroundVO>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 목록 조회 성공!")
				.data(groundVOList)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<List<GroundVO>> responseVO = ResponseVO.<List<GroundVO>>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<List<GroundVO>> responseVO = ResponseVO.<List<GroundVO>>builder()
				.code(HttpStatus.FORBIDDEN.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<List<GroundVO>> responseVO = ResponseVO.<List<GroundVO>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@GetMapping("/status-msg")
	@ApiOperation(value = "상태 메시지 조회", notes = "사용자 상태 메시지를 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<String>> getStatusMsg(@RequestHeader String Authorization) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.OK.value())
				.message("상태 메시지 조회 성공!")
				.data(userDto.getStatusMsg())
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/nickname/duplicate/{nickname}")
	@ApiOperation(value = "닉네임 중복 조회", notes = "닉네임 중복 조회 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Boolean>> checkDupNickname(@ApiParam(value = "중복 체크할 닉네임") @PathVariable String nickname,
		@RequestHeader String Authorization) {
		log.info("controller - checkDupNickname :: 사용자 닉네임 중복 조회 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.OK.value())
				.message("닉네임 중복 조회 성공!")
				.data(userService.checkDupNickname(nickname, userDto.getId()))
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.FORBIDDEN.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/personal-access-token")
	@ApiOperation(value = "회원가입 후 personal access token 저장", notes = "회원가입 후 personal access token 저장하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<String>> savePersonalAccessToken(
		@ApiParam(value = "personal access token") @RequestBody Map<String, String> personalAccessTokenMap,
		@RequestHeader String Authorization) {
		log.info("controller - savePersonalAccessToken :: personal access token 저장 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			String newPersonalAccessToken = personalAccessTokenMap.get("personalAccessToken");

			userService.savePersonalAccessToken(newPersonalAccessToken, userDto);
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.OK.value())
				.message("personal access token 저장 성공!")
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.FORBIDDEN.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/personal-access-token")
	@ApiOperation(value = "personal access token 수정", notes = "personal access token 수정하는 API")
	ResponseEntity<ResponseVO<String>> modifyPersonalAccessToken(
		@ApiParam(value = "personal-access-token") @RequestBody Map<String, String> personalAccessTokenMap,
		@RequestHeader String Authorization) {
		log.info("controller - modifyPersonalAccessToken :: personal access token 수정 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			String newPersonalAccessToken = personalAccessTokenMap.get("personal-access-token");
			userService.savePersonalAccessToken(newPersonalAccessToken, userDto);
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.OK.value())
				.message("personal access token 저장 성공!")
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.FORBIDDEN.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/nickname")
	@ApiOperation(value = "사용자 닉네임 수정", notes = "사용자 닉네임 수정 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<UserDto> modifyNickname(
		@ApiParam(value = "{nickname : __}") @RequestBody Map<String, String> nicknameMap,
		@RequestHeader String Authorization) {
		log.info("controller - modifyNickname :: nickname 수정 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));
			String newNickname = nicknameMap.get("nickname");
			return new ResponseEntity<>(userService.modifyNickname(newNickname, userDto), HttpStatus.OK);
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

	@PutMapping(path = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@ApiOperation(value = "사용자 프로필 사진 수정", notes = "사용자 프로필 사진 수정 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "파일 저장 에러"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<UserDto> modifyProfile(@RequestPart("file") MultipartFile file,
		@RequestHeader String Authorization) {
		log.info("controller - modifyProfile :: 프로필 사진 수정 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			return new ResponseEntity<>(userService.modifyProfile(file, userDto), HttpStatus.OK);

		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/status-msg")
	@ApiOperation(value = "사용자 상태메시지 수정", notes = "사용자 상태메시지 수정하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<UserDto> modifyStatusMsg(@RequestHeader String Authorization,
		@ApiParam(value = "{msg : __}") @RequestBody Map<String, String> statusMsgMap) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));
			String statusMsg = statusMsgMap.get("msg");
			return new ResponseEntity<>(userService.modifyStatusMsg(statusMsg, userDto), HttpStatus.OK);
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

	@PutMapping("/last-ground/{lastGroundId}")
	@ApiOperation(value = "사용자가 마지막으로 방문한 그라운드 수정", notes = "사용자가 마지막으로 방문한 그라운드를 수정하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<String>> modifyLastVisitedGround(@PathVariable Integer lastGroundId, @RequestHeader String Authorization) {
		log.info("controller - lastVisitedGround() :: 마지막으로 방문한 그라운드 수정 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			userDto = userService.modifyLastVisitedGround(lastGroundId, userDto);

			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.OK.value())
				.message("사용자가 마지막으로 방문한 그라운드 수정 성공!")
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
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

	@DeleteMapping("/profile")
	@ApiOperation(value = "사용자 프로필 사진 삭제", notes = "사용자 프로필 사진 삭제 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "파일 저장 에러"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<UserDto> deleteProfile(@RequestHeader String Authorization) {
		log.info("controller - deleteProfile :: 프로필 사진 삭제 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			return new ResponseEntity<>(userService.deleteProfile(userDto), HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping
	@ApiOperation(value = "사용자 탈퇴", notes = "사용자 탈퇴 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "파일 저장 에러"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<String> deleteUser(@RequestParam String code, @RequestHeader String Authorization) {
		log.info("controller - deleteUser :: 사용자 탈퇴 진입");
		try {
			// 사용자 정보 받아오기
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			// github token 받아오기
			Map<String, String> tokens = userService.githubToken(code);
			String githubAccessToken = tokens.get("access_token");

			// 깃허브 authorization 삭제하기
			userService.unlink(githubAccessToken);

			// 사용자 정보 db/서버에서 삭제하기
			userService.deleteUser(userDto);

			return new ResponseEntity<>("사용자 탈퇴 성공!", HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>("access token 오류", HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>("존재하지 않는 사용자", HttpStatus.FORBIDDEN);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>("파일 저장 에러", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>("사용자 탈퇴 실패..", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/status-msg")
	@ApiOperation(value = "사용자 상태메시지 삭제", notes = "사용자 상태메시지 삭제하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 403, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<UserDto> deleteStatusMsg(@RequestHeader String Authorization) {
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			return new ResponseEntity<>(userService.deleteStatusMsg(userDto), HttpStatus.OK);
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
