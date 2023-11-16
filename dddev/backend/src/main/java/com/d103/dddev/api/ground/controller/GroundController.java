package com.d103.dddev.api.ground.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.d103.dddev.api.common.ResponseDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.ground.repository.dto.LogEnvDto;
import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.ground.service.GroundService;
import com.d103.dddev.api.ground.service.GroundUserService;
import com.d103.dddev.api.repository.repository.entity.Repository;
import com.d103.dddev.api.repository.service.RepositoryService;
import com.d103.dddev.api.user.repository.entity.User;
import com.d103.dddev.api.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// TODO :: api 테스트 해보기

@RestController
@RequestMapping("/ground")
@RequiredArgsConstructor
@Api(tags = {"그라운드 API"})
@Slf4j
public class GroundController {

	private final GroundService groundService;
	private final GroundUserService groundUserService;
	private final RepositoryService repositoryService;
	private final UserService userService;

	@PostMapping("/repo/{repoId}")
	@ApiOperation(value = "그라운드 생성", notes = "{\"name\" : {name}}")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "존재하지 않는 레포지토리"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 409, message = "이미 존재하는 그라운드의 레포지토리"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Ground>> createGround(@RequestHeader String Authorization, HttpServletRequest request,
		@ApiParam(value = "repoId") @PathVariable Integer repoId,
		@ApiParam(value = "name : \"name\"") @RequestBody Map<String, String> nameMap) {
		try {
			log.info("controller - createGround :: 그라운드 생성 진입");
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			// 1. 연결할 레포지토리 불러오기
			Repository repository = repositoryService.getRepository(repoId)
				.orElseThrow(() -> new NoSuchElementException("getRepository :: 존재하지 않는 레포지토리입니다."));

			if (groundService.getGroundByRepoId(repository.getId()).isPresent()) {
				throw new EntityExistsException("controller - createGround :: 이미 그라운드가 생성된 레포지토리입니다.");
			}

			repository.setIsGround(true);
			repository = repositoryService.saveRepository(repository);

			String groundName = nameMap.get("name");

			// 2. 그라운드 생성하기
			Ground ground = groundService.createGround(groundName, user, repository);
			ResponseDto<Ground> responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 생성 성공!")
				.data(ground)
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseDto<Ground> responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
		} catch (EntityExistsException e) {
			log.error(e.getMessage());
			ResponseDto<Ground> responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.CONFLICT.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseDto<Ground> responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{groundId}/invite/{githubId}")
	@ApiOperation(value = "그라운드에 멤버 추가하기", notes = "그라운드에 멤버 추가하는 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 또는 그라운드의 오너가 아님"),
		@ApiResponse(code = 409, message = "이미 그라운드에 존재하는 회원"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<List<GroundUserDto>>> inviteMemberToGround(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@ApiParam(value = "githubId") @PathVariable Integer githubId, HttpServletRequest request) {
		log.info("controller - inviteMemberToGround :: 그라운드에 멤버 추가하기 진입");
		ResponseDto<List<GroundUserDto>> responseDto;
		try {

			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");
			Ground ground = (Ground)mav.getModel().get("ground");

			if (!groundUserService.checkIsGroundOwner(groundId, user.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			// 추가할 멤버 userEntity
			User newMember = userService.getUserEntity(githubId)
				.orElseThrow(() -> new NoSuchElementException("추가할 사용자가 존재하지 않습니다."));

			// 그라운드에 멤버 추가하기
			responseDto = ResponseDto.<List<GroundUserDto>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 멤버 추가 성공!")
				.data(groundUserService.inviteMemberToGround(ground, newMember))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<List<GroundUserDto>>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
		} catch (EntityExistsException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<List<GroundUserDto>>builder()
				.code(HttpStatus.CONFLICT.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<List<GroundUserDto>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{groundId}/token")
	@ApiOperation(value = "그라운드 로그 토큰 생성", notes = "그라운드 로그 토큰 생성하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<String>> createGroundToken(@ApiParam("groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - createGroundToken :: 그라운드 로그 토큰 생성 진입");
		ResponseDto<String> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 토크 생성 성공!")
				.data(groundUserService.createGroundUserToken(groundId, user))
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

	@PostMapping("/{groundId}/log-env")
	@ApiOperation(value = "로그 환경 설정 생성 API", notes = "로그 환경 설정 생성하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<List<LogEnvDto>>> createLogEnv(@ApiParam("groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization,
		@ApiParam(name = "logEnv", value = "type과 value만 넣기") @RequestBody LogEnvDto newLogEnv,
		HttpServletRequest request) {
		log.info("controller - createLogEnv :: 로그 환경 설정 생성 진입");
		ResponseDto<List<LogEnvDto>> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			Ground ground = (Ground)mav.getModel().get("ground");

			responseDto = ResponseDto.<List<LogEnvDto>>builder()
				.code(HttpStatus.OK.value())
				.message("로그 환경 설정 생성 성공!")
				.data(groundService.createLogEnv(ground, newLogEnv))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<List<LogEnvDto>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}")
	@ApiOperation(value = "그라운드 정보 조회", notes = "그라운드 정보를 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Ground>> getGroundInfo(@ApiParam("groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
		ResponseDto<Ground> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			Ground ground = (Ground)mav.getModel().get("ground");

			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 정보 조회 성공!")
				.data(ground)
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// TODO :: 테스트 다시해보기
	@GetMapping("/{groundId}/user/{email}")
	@ApiOperation(value = "이메일로 사용자 찾기", notes = "이메일로 사용자를 찾는 API(이미 그라운드에 추가된 사람은 제외됨)")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드의 owner가 아님"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<List<Map<String, String>>>> getUserByEmail(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@ApiParam(value = "email") @PathVariable String email, HttpServletRequest request) {
		log.info("controller - getUserByEmail :: 이메일로 멤버 찾기 진입(이미 그라운드에 추가된 사람은 제외됨)");
		ResponseDto<List<Map<String, String>>> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			if (!groundUserService.checkIsGroundOwner(groundId, user.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			List<Map<String, String>> userList = groundUserService.findUserByEmail(groundId, email);

			responseDto = ResponseDto.<List<Map<String, String>>>builder()
				.code(HttpStatus.OK.value())
				.message("멤버 찾기 성공~!~!~!")
				.data(userList)
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<List<Map<String, String>>>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<List<Map<String, String>>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/is-owner")
	@ApiOperation(value = "그라운드 owner 확인", notes = "그라운드의 owner인지 확인하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Boolean>> checkIsGroundOwner(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId, HttpServletRequest request) {
		log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
		ResponseDto<Boolean> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			Boolean isGroundOwner = groundUserService.checkIsGroundOwner(groundId, user.getId());

			responseDto = ResponseDto.<Boolean>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 owner 확인 성공!")
				.data(isGroundOwner)
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

	@GetMapping("/{groundId}/is-member")
	@ApiOperation(value = "사용자가 그라운드 멤버인지 확인", notes = "사용자가 그라운드 멤버인지 확인하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Boolean>> checkIsGroundMember(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId, HttpServletRequest request) {
		log.info("controller - checkIsGroundMember :: 사용자가 그라운드 멤버인지 조회 진입");
		ResponseDto<Boolean> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");

			Boolean isMember = groundUserService.checkIsGroundMember(groundId, user.getId());

			responseDto = ResponseDto.<Boolean>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 멤버 확인 성공!")
				.data(isMember)
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

	@GetMapping("/{groundId}/users")
	@ApiOperation(value = "그라운드 사용자 목록 조회", notes = "그라운드 사용자 목록을 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<List<GroundUserDto>>> getGroundUsers(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId) {
		log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
		ResponseDto<List<GroundUserDto>> responseDto;
		try {
			// 그라운드의 유저 목록 불러오기
			List<GroundUserDto> groundMembers = groundUserService.getGroundMembersAsDto(groundId);

			responseDto = ResponseDto.<List<GroundUserDto>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 멤버 목록 조회 성공!")
				.data(groundMembers)
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<List<GroundUserDto>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/log-env")
	@ApiOperation(value = "로그 환경 설정 조회 API", notes = "로그 환경 설정 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<List<LogEnvDto>>> getLogEnv(@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization, HttpServletRequest request) {
		log.info("controller - getLogEnv :: 로그 환경 설정 조회 진입");
		ResponseDto<List<LogEnvDto>> responseDto;
		try {
			responseDto = ResponseDto.<List<LogEnvDto>>builder()
				.code(HttpStatus.OK.value())
				.message("로그 환경 설정 조회 성공!")
				.data(groundService.getLogEnv(groundId))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<List<LogEnvDto>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/focus-time/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 집중시간 조회", notes = "그라운드 차트 - 완료/미완료 집중시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "그라운드의 멤버가 아님"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Map<String, Integer>>> getFocusTime(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization) {

		log.info("controller - getFocusTime :: 차트 집중시간 조회 진입");
		ResponseDto<Map<String, Integer>> responseDto;
		try {
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 완료/미완료 집중시간 조회 완료!")
				.data(groundService.getSprintFocusTime(sprintId))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/active-time/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 연구시간 조회", notes = "그라운드 차트 - 완료/미완료 연구시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Map<String, Integer>>> getActiveTime(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization) {
		log.info("controller - getFocusTime :: 차트 연구시간 조회 진입");
		ResponseDto<Map<String, Integer>> responseDto;
		try {
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 완료/미완료 연구시간 조회 성공!")
				.data(groundService.getSprintActiveTime(sprintId))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/total-time/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 전체 완료/미완료 시간 조회", notes = "그라운드 차트 - 전체 완료/미완료 시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Map<String, Integer>>> getTotalTime(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization) {
		log.info("controller - getTotalTime :: 전체 완료/미완료 시간 조회 진입");
		ResponseDto<Map<String, Integer>> responseDto;
		try {
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.OK.value())
				.message("전체 완료/미완료 시간 조회 성공!")
				.data(groundService.getSprintTotalTime(sprintId))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/focus-count/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 집중시간 완료/미완료 개수 조회", notes = "그라운드 차트 - 집중시간 완료/미완료 시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Map<String, Integer>>> getFocusCount(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization,
		HttpServletRequest request) {
		log.info("controller - getFocusCount :: 집중시간 완료/미완료 개수 조회 진입");
		ResponseDto<Map<String, Integer>> responseDto;
		try {
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.OK.value())
				.message("집중시간 완료/미완료 개수 조회 성공!")
				.data(groundService.getSprintFocusTimeCount(sprintId))
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/active-count/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 연구시간 완료/미완료 개수 조회", notes = "그라운드 차트 - 연구시간 완료/미완료 시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Map<String, Integer>>> getActiveCount(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization) {
		log.info("controller - getActiveCount :: 연구시간 완료/미완료 개수 조회 진입");
		ResponseDto<Map<String, Integer>> responseDto;
		try {
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.OK.value())
				.message("연구시간 완료/미완료 개수 조회 성공!")
				.data(groundService.getSprintActiveTimeCount(sprintId))
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/total-count/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 전체 완료/미완료 개수 조회", notes = "그라운드 차트 - 전체 완료/미완료 시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Map<String, Integer>>> getTotalCount(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization,
		HttpServletRequest request) {
		log.info("controller - getTotalCount :: 전체 완료/미완료 개수 조회 진입");
		ResponseDto<Map<String, Integer>> responseDto;
		try {
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.OK.value())
				.message("전체 완료/미완료 개수 조회 성공!")
				.data(groundService.getSprintTotalTimeCount(sprintId))
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Map<String, Integer>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/burn-down/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 번다운차트 조회", notes = "그라운드 차트 - 번다운차트를 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Map<LocalDate, Integer>>> getBurnDownChart(
		@ApiParam("그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization,
		HttpServletRequest request) {
		log.info("controller - getBurnDownChart :: 번다운 차트 데이터 조회 진입");
		ResponseDto<Map<LocalDate, Integer>> responseDto;
		try {
			responseDto = ResponseDto.<Map<LocalDate, Integer>>builder()
				.code(HttpStatus.OK.value())
				.message("번다운 차트 데이터 조회 성공!")
				.data(groundService.getSprintBurnDownChart(sprintId))
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Map<LocalDate, Integer>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/avg/focus")
	@ApiOperation(value = "그라운드 차트 - 완료 집중시간 평균 조회", notes = "그라운드 차트 - 완료 집중시간 평균을 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Double>> getFocusDoneAvg(@ApiParam("groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization) {
		log.info("controller - getFocusDoneAvg :: 완료 집중시간 평균 조회 진입");
		ResponseDto<Double> responseDto;
		try {
			responseDto = ResponseDto.<Double>builder()
				.code(HttpStatus.OK.value())
				.message("완료 집중시간 평균 조회 성공!")
				.data(groundService.getGroundFocusTimeDoneAvg(groundId))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Double>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/avg/active")
	@ApiOperation(value = "그라운드 차트 - 완료 연구시간 평균 조회", notes = "그라운드 차트 - 완료 연구시간 평균을 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Double>> getActiveDoneAvg(@ApiParam("groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization) {
		log.info("controller - getFocusDoneAvg :: 완료 연구시간 평균 조회 진입");
		ResponseDto<Double> responseDto;
		try {
			responseDto = ResponseDto.<Double>builder()
				.code(HttpStatus.OK.value())
				.message("완료 연구시간 평균 조회 성공!")
				.data(groundService.getGroundActiveTimeDoneAvg(groundId))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Double>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/avg/total")
	@ApiOperation(value = "그라운드 차트 - 전체 완료 시간 평균 조회", notes = "그라운드 차트 - 전체 완료 시간 평균을 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Double>> getTotalDoneAvg(@ApiParam("groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization) {
		log.info("controller - getFocusDoneAvg :: 전체 완료 시간 평균 조회 진입");
		ResponseDto<Double> responseDto;
		try {
			responseDto = ResponseDto.<Double>builder()
				.code(HttpStatus.OK.value())
				.message("전체 완료 시간 평균 조회 성공!")
				.data(groundService.getGroundTotalTimeDoneAvg(groundId))
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Double>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{groundId}")
	@ApiOperation(value = "그라운드 정보 수정", notes = "그라운드 정보를 수정하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자거나 해당 그라운드의 owner가 아님"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Ground>> updateGroundInfo(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@ApiParam(value = "{name : ___, activeTime : ___, focusTime : ___}") @RequestBody Ground newGround,
		HttpServletRequest request) {
		log.info("controller - updateGroundInfo :: 그라운드 정보 수정 진입");
		ResponseDto<Ground> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");
			Ground ground = (Ground)mav.getModel().get("ground");

			if (!groundUserService.checkIsGroundOwner(groundId, user.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			ground = groundService.updateGroundInfo(newGround, ground);
			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 정보 수정 성공!")
				.data(ground)
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{groundId}/profile")
	@ApiOperation(value = "그라운드 프로필 사진 수정", notes = "그라운드 프로필 사진 수정하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드 오너가 아님"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Ground>> modifyGroundProfile(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId, @RequestParam MultipartFile file,
		HttpServletRequest request) {
		log.info("controller - modifyGroundName :: 그라운드 프로필 수정 진입");
		ResponseDto<Ground> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");
			Ground ground = (Ground)mav.getModel().get("ground");

			if (!groundUserService.checkIsGroundOwner(groundId, user.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			ground = groundService.updateGroundProfile(file, ground);

			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 프로필 수정 성공!")
				.data(ground)
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{groundId}/profile")
	@ApiOperation(value = "그라운드 프로필 사진 삭제", notes = "그라운드 프로필 사진 삭제하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드 오너가 아님"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Ground>> deleteGroundProfileImg(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId, HttpServletRequest request) {
		log.info("controller - deleteGroundProfileImg :: 그라운드 프로필 사진 삭제 진입");
		ResponseDto<Ground> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");
			Ground ground = (Ground)mav.getModel().get("ground");

			if (!groundUserService.checkIsGroundOwner(groundId, user.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			ground = groundService.deleteGroundProfile(ground);

			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 프로필 사진 삭제 성공!")
				.data(ground)
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Ground>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{groundId}/log-env/{logEnvId}")
	@ApiOperation(value = "로그 환경 설정 삭제 API", notes = "로그 환경 설정 삭제하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드의 오너가 아님"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<Void>> deleteLogEnv(@ApiParam("groundId") @PathVariable Integer groundId,
		@ApiParam("logEnvId") @PathVariable Integer logEnvId, @RequestHeader String Authorization) {
		log.info("controller - deleteLogEnv :: 로그 환경 설정 삭제 API 진입");
		ResponseDto<Void> responseDto;
		try {
			groundService.deleteLogEnv(logEnvId);
			responseDto = ResponseDto.<Void>builder()
				.code(HttpStatus.OK.value())
				.message("로그 환경 설정 삭제 성공!")
				.build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Void>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<Void>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{groundId}/owner/user/{githubId}")
	@ApiOperation(value = "그라운드에서 사용자 강퇴시키기", notes = "그라운드 멤버인 사용자 강퇴시키는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드의 오너가 아님"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<String>> deleteMemberFromGroundByOwner(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@ApiParam(value = "강퇴시킬 사람의 githubId") @PathVariable Integer githubId, HttpServletRequest request) {
		log.info("controller - deleteMemberFromGroundByOwner :: 멤버 강퇴 진입");
		ResponseDto<String> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");
			Ground ground = (Ground)mav.getModel().get("ground");

			if (!groundUserService.checkIsGroundOwner(groundId, user.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			User memberToDelete = userService.getUserEntity(githubId)
				.orElseThrow(() -> new NoSuchElementException("강퇴시킬 멤버가 존재하지 않습니다."));

			groundService.deleteMemberFromGround(ground, memberToDelete);

			responseDto = ResponseDto.<String>builder().code(HttpStatus.OK.value()).message("멤버 강퇴 성공!").build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{groundId}/user")
	@ApiOperation(value = "그라운드에서 사용자 나가기", notes = "그라운드 멤버인 사용자 나가기 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"), @ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<String>> deleteMemberFromGround(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId, HttpServletRequest request) {
		log.info("controller - deleteMemberFromGround :: 멤버 나가기 진입");
		ResponseDto<String> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");
			Ground ground = (Ground)mav.getModel().get("ground");

			groundService.deleteMemberFromGround(ground, user);

			responseDto = ResponseDto.<String>builder().code(HttpStatus.OK.value()).message("그라운드 나가기 성공!").build();

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

	@DeleteMapping("/{groundId}")
	@ApiOperation(value = "그라운드 삭제", notes = "그라운드를 삭제하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드 오너가 아님"), @ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseDto<String>> deleteGround(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId, HttpServletRequest request) {
		log.info("controller - modifyActiveTime :: 그라운드 연구시간 수정 진입");
		ResponseDto<String> responseDto;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			User user = (User)mav.getModel().get("user");
			Ground ground = (Ground)mav.getModel().get("ground");

			if (!groundUserService.checkIsGroundOwner(groundId, user.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			groundService.deleteGround(user, ground);

			responseDto = ResponseDto.<String>builder().code(HttpStatus.OK.value()).message("그라운드 삭제 성공!").build();

			return new ResponseEntity<>(responseDto, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseDto = ResponseDto.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
