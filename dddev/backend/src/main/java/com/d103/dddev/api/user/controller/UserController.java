package com.d103.dddev.api.user.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.d103.dddev.api.user.repository.dto.UserDto;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.d103.dddev.api.common.ResponseDto;
import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.d103.dddev.api.ground.repository.entity.GroundUser;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.user.repository.entity.User;
import com.d103.dddev.api.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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

	@GetMapping
	@ApiOperation(value = "사용자 정보", notes = "사용자 정보를 받아오는 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<UserDto>> getUserInfo(@RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - getUserInfo :: 사용자 정보 받아오기 진입");
		ResponseDto<UserDto> responseDto;
		try {
			ModelAndView max = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)max.getModel().get("user");
			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.OK.value())
				.message("사용자 정보 조회 성공")
				.data(user.convertToDto())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{githubId}")
	@ApiOperation(value = "github Id로 사용자 조회", notes = "github Id로 사용자를 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
			@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
			@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<UserDto>> getUserByGithubId(@PathVariable Integer githubId, @RequestHeader String Authorization) {
		log.info("controller - getUserByGithubId :: github id로 사용자 조회 진입");
		ResponseDto<UserDto> responseDto;
		try {
			UserDto userDto = userService.getUserDto(githubId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다"));
			responseDto = ResponseDto.<UserDto>builder()
					.code(HttpStatus.OK.value())
					.message("사용자 조회 성공!")
					.data(userDto)
					.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<UserDto>builder()
					.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.message(e.getMessage())
					.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@GetMapping("/ground/list")
	@ApiOperation(value = "사용자가 가입한 그라운드 목록 조회", notes = "사용자가 가입한 그라운드 목록을 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<List<GroundDto>>> getGroundList(@RequestHeader String Authorization,
                                                               HttpServletRequest request) {
		log.info("controller - getGroundList :: 사용자가 가입한 그라운드 목록 조회 진입");
		ResponseDto<List<GroundDto>> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			List<GroundUser> groundUserList = userService.getGroundList(user);
			List<GroundDto> groundDtoList = new ArrayList<>();

			for (GroundUser g : groundUserList) {
				GroundDto groundDto = GroundDto.builder()
					.isOwner(g.getIsOwner())
					.ground(g.getGround())
					.build();

				groundDtoList.add(groundDto);
			}

			responseDto = ResponseDto.<List<GroundDto>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 목록 조회 성공!")
				.data(groundDtoList)
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<List<GroundDto>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/status-msg")
	@ApiOperation(value = "상태 메시지 조회", notes = "사용자 상태 메시지를 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<String>> getStatusMsg(@RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - getStatusMsg :: 상태 메시지 조회 진입");
		ResponseDto<String> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.OK.value())
				.message("상태 메시지 조회 성공!")
				.data(user.getStatusMsg())
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/nickname/duplicate/{nickname}")
	@ApiOperation(value = "닉네임 중복 조회", notes = "닉네임 중복 조회 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Boolean>> checkDupNickname(@ApiParam(value = "중복 체크할 닉네임") @PathVariable String nickname,
                                                          @RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - checkDupNickname :: 사용자 닉네임 중복 조회 진입");
		ResponseDto<Boolean> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			responseDto = ResponseDto.<Boolean>builder()
				.code(HttpStatus.OK.value())
				.message("닉네임 중복 조회 성공!")
				.data(userService.checkDupNickname(nickname, user.getId()))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Boolean>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/personal-access-token")
	@ApiOperation(value = "회원가입 후 personal access token 저장", notes = "회원가입 후 personal access token 저장하는 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<String>> savePersonalAccessToken(
		@ApiParam(value = "personal access token") @RequestBody Map<String, String> personalAccessTokenMap,
		@RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - savePersonalAccessToken :: personal access token 저장 진입");
		ResponseDto<String> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			String newPersonalAccessToken = personalAccessTokenMap.get("personalAccessToken");

			userService.savePersonalAccessToken(newPersonalAccessToken, user);
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.OK.value())
				.message("personal access token 저장 성공!")
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/personal-access-token")
	@ApiOperation(value = "personal access token 수정", notes = "personal access token 수정하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "pat 오류"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<String>> updatePersonalAccessToken(
		@ApiParam(value = "personal-access-token") @RequestBody Map<String, String> personalAccessTokenMap,
		@RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - modifyPersonalAccessToken :: personal access token 수정 진입");
		ResponseDto<String> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			String newPersonalAccessToken = personalAccessTokenMap.get("personal-access-token");
			userService.savePersonalAccessToken(newPersonalAccessToken, user);
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.OK.value())
				.message("personal access token 저장 성공!")
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (HttpClientErrorException e) {
			log.error("personal access token 에러 :: pat를 확인하세요");
			responseDto = ResponseDto.<String>builder()
					.code(HttpStatus.UNAUTHORIZED.value())
					.message("personal access token 에러 :: pat를 확인하세요")
					.build();
			return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping
	@ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보 수정 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<UserDto>> updateUserInfo(
		@ApiParam(value = "{nickname : __, statusMsg : __}") @RequestBody UserDto newUserDto,
		@RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - updateUserInfo :: 그라운드 정보 수정 진입");
		ResponseDto<UserDto> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			UserDto userDto = userService.updateUserInfo(newUserDto, user);

			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.OK.value())
				.message("사용자 정보 수정 성공!")
				.data(userDto)
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@ApiOperation(value = "사용자 프로필 사진 수정", notes = "사용자 프로필 사진 수정 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "파일 저장 오류"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<UserDto>> updateProfile(@RequestPart("file") MultipartFile file,
                                                       @RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - updateProfile :: 프로필 사진 수정 진입");
		ResponseDto<UserDto> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.OK.value())
				.message("사용자 프로필 사진 수정 성공!")
				.data(userService.updateProfile(file, user))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (IOException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/last-ground/{lastGroundId}")
	@ApiOperation(value = "사용자가 마지막으로 방문한 그라운드 수정", notes = "사용자가 마지막으로 방문한 그라운드를 수정하는 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<String>> updateLastVisitedGround(@PathVariable Integer lastGroundId,
                                                                @RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - lastVisitedGround() :: 마지막으로 방문한 그라운드 수정 진입");
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			userService.updateLastVisitedGround(lastGroundId, user);

			ResponseDto<String> responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.OK.value())
				.message("사용자가 마지막으로 방문한 그라운드 수정 성공!")
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseDto<String> responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/profile")
	@ApiOperation(value = "사용자 프로필 사진 삭제", notes = "사용자 프로필 사진 삭제 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "파일 삭제 오류"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<UserDto>> deleteProfile(@RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - deleteProfile :: 프로필 사진 삭제 진입");
		ResponseDto<UserDto> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.OK.value())
				.message("프로필 사진 삭제 성공!")
				.data(userService.deleteProfile(user))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (IOException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping
	@ApiOperation(value = "사용자 탈퇴", notes = "사용자 탈퇴 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "파일 삭제 에러"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<String>> deleteUser(@RequestParam String code, @RequestHeader String Authorization,
                                                   HttpServletRequest request) {
		log.info("controller - deleteUser :: 사용자 탈퇴 진입");
		ResponseDto<String> responseDto;
		try {
			// 사용자 정보 받아오기
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			// github token 받아오기
			Map<String, String> tokens = userService.githubToken(code);
			String githubAccessToken = tokens.get("access_token");

			// 깃허브 authorization 삭제하기
			userService.unlink(githubAccessToken);

			// 사용자 정보 db/서버에서 삭제하기
			userService.deleteUser(user);

			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.OK.value())
				.message("사용자 탈퇴 성공!")
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (IOException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/status-msg")
	@ApiOperation(value = "사용자 상태메시지 삭제", notes = "사용자 상태메시지 삭제하는 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<UserDto>> deleteStatusMsg(@RequestHeader String Authorization,
                                                         HttpServletRequest request) {
		log.info("controller - deleteStatusMsg :: 사용자 상태메시지 삭제 진입");
		ResponseDto<UserDto> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.OK.value())
				.message("사용자 상태메시지 삭제 성공!")
				.data(userService.deleteStatusMsg(user))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<UserDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/device-token")
	@ApiOperation(value = "사용자 기기 토큰 등록", notes = "사용자가 알림을 허용한 기기 토큰을 등록하는 API")
	@ApiImplicitParam(name = "map", paramType = "body", example = "{\n"
		+ "  \"deviceToken\": \"string\"\n"
		+ "}")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<?> saveDeviceToken(@RequestHeader String Authorization, @RequestBody Map<String, String> map,
		HttpServletRequest request) {
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");
			userService.saveDeviceToken(user, map.get("deviceToken"));
			ResponseDto<String> responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.OK.value())
				.message("기기 토큰 등록에 성공했습니다.")
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseDto<String> responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@DeleteMapping("/device-token")
	@ApiOperation(value = "사용자 기기 토큰 삭제", notes = "사용자가 알림을 허용한 기기 토큰을 삭제하는 API")
	@ApiImplicitParam(name = "map", paramType = "body", example = "{\n"
		+ "  \"deviceToken\": \"string\"\n"
		+ "}")
	@ApiResponses(value = {
		@ApiResponse(code = 403, message = "존재하지 않는 사용자")
	})
	ResponseEntity<?> deleteDeviceToken(@RequestHeader String Authorization, @RequestBody Map<String, String> map,
		HttpServletRequest request) {
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");
			userService.deleteDeviceToken(user, map.get("deviceToken"));
			ResponseDto<String> responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.OK.value())
				.message("기기 토큰 삭제에 성공했습니다.")
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseDto<String> responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseDto<String> responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
