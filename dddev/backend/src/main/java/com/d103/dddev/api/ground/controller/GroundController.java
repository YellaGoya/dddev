package com.d103.dddev.api.ground.controller;

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

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.vo.GroundUserVO;
import com.d103.dddev.api.ground.service.GroundService;
import com.d103.dddev.api.ground.service.GroundUserService;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.repository.service.RepositoryService;
import com.d103.dddev.api.user.repository.dto.UserDto;
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
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 409, message = "이미 존재하는 그라운드의 레포지토리"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<GroundDto>> createGround(@RequestHeader String Authorization, HttpServletRequest request,
		@ApiParam(value = "repoId") @PathVariable Integer repoId,
		@ApiParam(value = "name : \"name\"") @RequestBody Map<String, String> nameMap) {
		try {
			log.info("controller - createGround :: 그라운드 생성 진입");
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");

			// 1. 연결할 레포지토리 불러오기
			RepositoryDto repositoryDto = repositoryService.getRepository(repoId)
				.orElseThrow(() -> new NoSuchElementException("getRepository :: 존재하지 않는 레포지토리입니다."));

			if (groundService.getGroundByRepoId(repositoryDto.getId()).isPresent()) {
				throw new EntityExistsException("controller - createGround :: 이미 그라운드가 생성된 레포지토리입니다.");
			}

			repositoryDto.setIsGround(true);
			repositoryDto = repositoryService.saveRepository(repositoryDto);

			String groundName = nameMap.get("name");

			// 2. 그라운드 생성하기
			GroundDto ground = groundService.createGround(groundName, userDto, repositoryDto);
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 생성 성공!")
				.data(ground)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (EntityExistsException e) {
			log.error(e.getMessage());
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.CONFLICT.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{groundId}/invite/{githubId}")
	@ApiOperation(value = "그라운드에 멤버 추가하기", notes = "그라운드에 멤버 추가하는 API")
	@ApiResponses(value = {@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 또는 그라운드의 오너가 아님"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<List<GroundUserVO>>> inviteMemberToGround(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId, @ApiParam(value = "githubId") @PathVariable Integer githubId,
		HttpServletRequest request) {
		log.info("controller - inviteMemberToGround :: 그라운드에 멤버 추가하기 진입");
		ResponseVO<List<GroundUserVO>> responseVO;
		try {

			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");
			GroundDto groundDto = (GroundDto)mav.getModel().get("groundDto");

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			// 추가할 멤버 userDto
			UserDto newMember = userService.getUserInfo(githubId)
				.orElseThrow(() -> new NoSuchElementException("추가할 사용자가 존재하지 않습니다."));

			// 그라운드에 멤버 추가하기
			responseVO = ResponseVO.<List<GroundUserVO>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 멤버 추가 성공!")
				.data(groundUserService.inviteMemberToGround(groundDto, newMember))
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<List<GroundUserVO>>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<List<GroundUserVO>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}")
	@ApiOperation(value = "그라운드 정보 조회", notes = "그라운드 정보를 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<GroundDto>> getGroundInfo(@RequestHeader String Authorization,
		HttpServletRequest request) {
		log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
		ResponseVO<GroundDto> responseVO;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			GroundDto groundDto = (GroundDto)mav.getModel().get("groundDto");

			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 정보 조회 성공!")
				.data(groundDto)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/user/{email}")
	@ApiOperation(value = "이메일로 사용자 찾기", notes = "이메일로 사용자를 찾는 API(이미 그라운드에 추가된 사람은 제외됨)")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드의 owner가 아님"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<List<Map<String, String>>>> getUserByEmail(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@ApiParam(value = "email") @PathVariable String email, HttpServletRequest request) {
		log.info("controller - getUserByEmail :: 이메일로 멤버 찾기 진입(이미 그라운드에 추가된 사람은 제외됨)");
		ResponseVO<List<Map<String, String>>> responseVO;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			List<Map<String, String>> userList = groundUserService.findUserByEmail(groundId, email);

			responseVO = ResponseVO.<List<Map<String, String>>>builder()
				.code(HttpStatus.OK.value())
				.message("멤버 찾기 성공~!~!~!")
				.data(userList)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<List<Map<String, String>>>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<List<Map<String, String>>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/is-owner")
	@ApiOperation(value = "그라운드 owner 확인", notes = "그라운드의 owner인지 확인하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Boolean>> checkIsGroundOwner(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		HttpServletRequest request) {
		log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
		ResponseVO<Boolean> responseVO;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");

			Boolean isGroundOwner = groundUserService.checkIsGroundOwner(groundId, userDto.getId());

			responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 owner 확인 성공!")
				.data(isGroundOwner)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/is-member")
	@ApiOperation(value = "사용자가 그라운드 멤버인지 확인", notes = "사용자가 그라운드 멤버인지 확인하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Boolean>> checkIsGroundMember(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		HttpServletRequest request) {
		log.info("controller - checkIsGroundMember :: 사용자가 그라운드 멤버인지 조회 진입");
		ResponseVO<Boolean> responseVO;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");

			Boolean isMember = groundUserService.checkIsGroundMember(groundId, userDto.getId());

			responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 멤버 확인 성공!")
				.data(isMember)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/users")
	@ApiOperation(value = "그라운드 사용자 목록 조회", notes = "그라운드 사용자 목록을 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<List<GroundUserVO>>> getGroundUsers(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId) {
		log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
		ResponseVO<List<GroundUserVO>> responseVO;
		try {
			// 그라운드의 유저 목록 불러오기
			List<GroundUserVO> groundMembers = groundUserService.getGroundMembersAsVO(groundId);

			responseVO = ResponseVO.<List<GroundUserVO>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 멤버 목록 조회 성공!")
				.data(groundMembers)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<List<GroundUserVO>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/focus-time/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 집중시간 조회", notes = "그라운드 차트 - 완료/미완료 집중시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "그라운드의 멤버가 아님"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Map<String, Integer>>> getFocusTime(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization) {

		log.info("controller - getFocusTime :: 차트 집중시간 조회 진입");
		ResponseVO<Map<String, Integer>> responseVO;
		try {
			responseVO = ResponseVO.<Map<String, Integer>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 완료/미완료 집중시간 조회 완료!")
				.data(groundService.getGroundFocusTime(groundId, sprintId))
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<Map<String, Integer>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/active-time/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 집중시간 조회", notes = "그라운드 차트 - 완료/미완료 집중시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Map<String, Integer>>> getActiveTime(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization) {
		log.info("controller - getFocusTime :: 차트 연구시간 조회 진입");
		ResponseVO<Map<String, Integer>> responseVO;
		try {
			responseVO = ResponseVO.<Map<String, Integer>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 완료/미완료 연구시간 조회 성공!")
				.data(groundService.getGroundActiveTime(groundId, sprintId))
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<Map<String, Integer>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/total-time/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 전체 완료/미완료 시간 조회", notes = "그라운드 차트 - 전체 완료/미완료 시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Map<String, Integer>>> getTotalTime(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization) {
		log.info("controller - getTotalTime :: 전체 완료/미완료 시간 조회 진입");
		ResponseVO<Map<String, Integer>> responseVO;
		try {
			responseVO = ResponseVO.<Map<String, Integer>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 전체 완료/미완료 연구시간 조회 성공!")
				.data(groundService.getGroundTotalTime(groundId, sprintId))
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<Map<String, Integer>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/focus-count/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 집중시간 완료/미완료 개수 조회", notes = "그라운드 차트 - 집중시간 완료/미완료 시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Map<String, Long>>> getFocusCount(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization,
		HttpServletRequest request) {
		log.info("controller - getFocusCount :: 집중시간 완료/미완료 개수 조회 진입");
		ResponseVO<Map<String, Long>> responseVO;
		try {
			responseVO = ResponseVO.<Map<String, Long>>builder()
				.code(HttpStatus.OK.value())
				.message("집중시간 완료/미완료 개수 조회 성공!")
				.data(groundService.getGroundFocusTimeCount(groundId, sprintId))
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<Map<String, Long>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/active-count/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 연구시간 완료/미완료 개수 조회", notes = "그라운드 차트 - 연구시간 완료/미완료 시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Map<String, Long>>> getActiveCount(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization) {
		log.info("controller - getActiveCount :: 연구시간 완료/미완료 개수 조회 진입");
		ResponseVO<Map<String, Long>> responseVO;
		try {
			responseVO = ResponseVO.<Map<String, Long>>builder()
				.code(HttpStatus.OK.value())
				.message("집중시간 완료/미완료 개수 조회 성공!")
				.data(groundService.getGroundActiveTimeCount(groundId, sprintId))
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<Map<String, Long>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/chart/total-count/{sprintId}")
	@ApiOperation(value = "그라운드 차트 - 전체 완료/미완료 개수 조회", notes = "그라운드 차트 - 전체 완료/미완료 시간 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Map<String, Long>>> getTotalCount(
		@ApiParam(value = "그라운드 아이디") @PathVariable Integer groundId,
		@ApiParam("스프린트 아이디") @PathVariable Integer sprintId, @RequestHeader String Authorization,
		HttpServletRequest request) {
		log.info("controller - getTotalCount :: 전체 완료/미완료 개수 조회 진입");
		ResponseVO<Map<String, Long>> responseVO;
		try {
			responseVO = ResponseVO.<Map<String, Long>>builder()
				.code(HttpStatus.OK.value())
				.message("집중시간 완료/미완료 개수 조회 성공!")
				.data(groundService.getGroundTotalTimeCount(groundId, sprintId))
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<Map<String, Long>>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{groundId}")
	@ApiOperation(value = "그라운드 정보 수정", notes = "그라운드 정보를 수정하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자거나 해당 그라운드의 owner가 아님"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<GroundDto>> updateGroundInfo(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@ApiParam(value = "{name : ___, activeTime : ___, focusTime : ___}") @RequestBody GroundDto newGroundDto,
		HttpServletRequest request) {
		log.info("controller - updateGroundInfo :: 그라운드 정보 수정 진입");
		ResponseVO<GroundDto> responseVO;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");
			GroundDto groundDto = (GroundDto)mav.getModel().get("groundDto");

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			groundDto = groundService.updateGroundInfo(newGroundDto, groundDto);
			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 정보 수정 성공!")
				.data(groundDto)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{groundId}/profile")
	@ApiOperation(value = "그라운드 프로필 사진 수정", notes = "그라운드 프로필 사진 수정하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드 오너가 아님"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<GroundDto>> modifyGroundProfile(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId, @RequestParam MultipartFile file,
		HttpServletRequest request) {
		log.info("controller - modifyGroundName :: 그라운드 프로필 수정 진입");
		ResponseVO<GroundDto> responseVO;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");
			GroundDto groundDto = (GroundDto)mav.getModel().get("groundDto");

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			groundDto = groundService.updateGroundProfile(file, groundDto);

			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 프로필 수정 성공!")
				.data(groundDto)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{groundId}/profile")
	@ApiOperation(value = "그라운드 프로필 사진 삭제", notes = "그라운드 프로필 사진 삭제하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드 오너가 아님"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<GroundDto>> deleteGroundProfileImg(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		HttpServletRequest request) {
		log.info("controller - deleteGroundProfileImg :: 그라운드 프로필 사진 삭제 진입");
		ResponseVO<GroundDto> responseVO;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");
			GroundDto groundDto = (GroundDto)mav.getModel().get("groundDto");

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			groundDto = groundService.deleteGroundProfile(groundDto);

			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 프로필 사진 삭제 성공!")
				.data(groundDto)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{groundId}/owner/user/{githubId}")
	@ApiOperation(value = "그라운드에서 사용자 강퇴시키기", notes = "그라운드 멤버인 사용자 강퇴시키는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드의 오너가 아님"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<String>> deleteMemberFromGroundByOwner(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@ApiParam(value = "강퇴시킬 사람의 githubId") @PathVariable Integer githubId, HttpServletRequest request) {
		log.info("controller - deleteMemberFromGroundByOwner :: 멤버 강퇴 진입");
		ResponseVO<String> responseVO;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");
			GroundDto groundDto = (GroundDto)mav.getModel().get("groundDto");

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			UserDto memberToDelete = userService.getUserInfo(githubId)
				.orElseThrow(() -> new NoSuchElementException("강퇴시킬 멤버가 존재하지 않습니다."));

			groundService.deleteMemberFromGround(groundDto, memberToDelete);

			responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.OK.value())
				.message("멤버 강퇴 성공!")
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{groundId}/user")
	@ApiOperation(value = "그라운드에서 사용자 나가기", notes = "그라운드 멤버인 사용자 나가기 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<String>> deleteMemberFromGround(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId, HttpServletRequest request) {
		log.info("controller - deleteMemberFromGround :: 멤버 나가기 진입");
		ResponseVO<String> responseVO;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");
			GroundDto groundDto = (GroundDto)mav.getModel().get("groundDto");

			groundService.deleteMemberFromGround(groundDto, userDto);

			responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 나가기 성공!")
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{groundId}")
	@ApiOperation(value = "그라운드 삭제", notes = "그라운드를 삭제하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 403, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자 혹은 그라운드 오너가 아님"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<String>> deleteGround(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		HttpServletRequest request) {
		log.info("controller - modifyActiveTime :: 그라운드 연구시간 수정 진입");
		ResponseVO<String> responseVO;
		try {
			ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
			UserDto userDto = (UserDto)mav.getModel().get("userDto");
			GroundDto groundDto = (GroundDto)mav.getModel().get("groundDto");

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			groundService.deleteGround(groundDto);

			responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 삭제 성공!")
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
