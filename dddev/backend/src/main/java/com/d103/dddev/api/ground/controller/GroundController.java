package com.d103.dddev.api.ground.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.persistence.EntityExistsException;

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

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.service.GroundService;
import com.d103.dddev.api.ground.service.GroundUserService;
import com.d103.dddev.api.ground.vo.GroundUserVO;
import com.d103.dddev.api.repository.repository.dto.RepositoryDto;
import com.d103.dddev.api.repository.service.RepositoryService;
import com.d103.dddev.api.user.repository.dto.UserDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
	@ApiOperation(value = "그라운드 생성", notes = "{\"name\" : {name}}")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 409, message = "이미 존재하는 그라운드의 레포지토리"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<GroundDto>> createGround(@RequestHeader String Authorization,
		@ApiParam(value = "repoId") @PathVariable Integer repoId,
		@ApiParam(value = "name : \"name\"") @RequestBody Map<String, String> nameMap) {
		try {
			log.info("controller - createGround :: 그라운드 생성 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

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

		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch(EntityExistsException e) {
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

	@GetMapping("/{groundId}")
	@ApiOperation(value = "그라운드 정보 조회", notes = "그라운드 정보를 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<GroundDto>> getGroundInfo(@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization) {
		try {
			log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			// 1. 사용자가 해당 그라운드의 멤버인지 확인
			if (!groundUserService.checkIsGroundMember(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 멤버가 아닙니다.");
			}
			// 2. 그라운드 정보 조회
			GroundDto groundDto = groundService.getGroundInfo(groundId)
				.orElseThrow(() -> new NoSuchElementException("존재하지 않는 그라운드"));

			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 정보 조회 성공!")
				.data(groundDto)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/is-owner")
	@ApiOperation(value = "그라운드 owner 확인", notes = "그라운드의 owner인지 확인하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Boolean>> checkIsGroundOwner(@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization) {
		try {
			log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			Boolean isGroundOwner = groundUserService.checkIsGroundOwner(groundId, userDto.getId());

			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 owner 확인 성공!")
				.data(isGroundOwner)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/is-member")
	@ApiOperation(value = "사용자가 그라운드 멤버인지 확인", notes = "사용자가 그라운드 멤버인지 확인하는 API")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<Boolean>> checkIsGroundMember(@PathVariable Integer groundId, @RequestHeader String Authorization) {
		log.info("controller - checkIsGroundMember :: 사용자가 그라운드 멤버인지 조회 진입");
		try {
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			Boolean isMember = groundUserService.checkIsGroundMember(groundId, userDto.getId());

			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 owner 확인 성공!")
				.data(isMember)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{groundId}/users")
	@ApiOperation(value = "그라운드 사용자 목록 조회", notes = "그라운드 사용자 목록을 조회하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<List<GroundUserVO>>> getGroundUsers(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId) {
		try {
			log.info("controller - getGroundInfo :: 그라운드 정보 조회 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			// 1. 사용자가 해당 그라운드의 멤버인지 확인
			if (!groundUserService.checkIsGroundMember(groundId, userDto.getId())) {
				log.error("그라운드의 멤버가 아닙니다.");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			// 2. 그라운드의 유저 목록 불러오기
			List<GroundUserVO> groundMembers = groundUserService.getGroundMembers(groundId);

			ResponseVO<List<GroundUserVO>> responseVO = ResponseVO.<List<GroundUserVO>>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 멤버 목록 조회 성공!")
				.data(groundMembers)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchFieldException e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseVO<Boolean> responseVO = ResponseVO.<Boolean>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{groundId}")
	@ApiOperation(value = "", notes = "")
	@ApiResponses(value = {@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<GroundDto>> updateGroundInfo(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@ApiParam(value = "{name : ___, activeTime : ___, focusTime : ___}") @RequestBody GroundDto newGroundDto) {
		try {
			log.info("controller - updateGroundInfo :: 그라운드 정보 수정 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			GroundDto groundDto = groundService.getGroundInfo(groundId)
				.orElseThrow(() -> new NoSuchElementException("getGroundInfo :: 존재하지 않는 그라운드입니다."));

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			groundDto = groundService.updateGroundInfo(newGroundDto, groundDto);
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 정보 수정 성공!")
				.data(groundDto)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchFieldException e) {
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{groundId}/profile")
	@ApiOperation(value = "", notes = "")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<GroundDto>> modifyGroundProfile(@RequestHeader String Authorization,
		@ApiParam(value = "groundId") @PathVariable Integer groundId, @RequestParam MultipartFile file) {
		try {
			log.info("controller - modifyGroundName :: 그라운드 이름 수정 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			GroundDto groundDto = groundService.getGroundInfo(groundId)
				.orElseThrow(() -> new NoSuchElementException("getGroundInfo :: 존재하지 않는 그라운드입니다."));

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			groundDto = groundService.updateGroundProfile(file, groundDto);

			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 이름 수정 성공!")
				.data(groundDto)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchFieldException e) {
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{groundId}/profile-img")
	@ApiOperation(value = "그라운드 프로필 사진 삭제", notes = "그라운드 프로필 사진 삭제하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<GroundDto>> deleteGroundProfileImg(
		@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization) {
		try {
			log.info("controller - modifyActiveTime :: 그라운드 연구시간 수정 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			GroundDto groundDto = groundService.getGroundInfo(groundId)
				.orElseThrow(() -> new NoSuchElementException("getGroundInfo :: 존재하지 않는 그라운드입니다."));

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			groundDto = groundService.deleteGroundProfile(groundDto);

			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 프로필 사진 삭제 성공!")
				.data(groundDto)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchFieldException e) {
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			ResponseVO<GroundDto> responseVO = ResponseVO.<GroundDto>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{groundId}")
	@ApiOperation(value = "그라운드 삭제", notes = "그라운드를 삭제하는 API")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "해당 그라운드의 멤버가 아닌 사용자"),
		@ApiResponse(code = 401, message = "access token 오류"),
		@ApiResponse(code = 406, message = "존재하지 않는 사용자"),
		@ApiResponse(code = 500, message = "내부 오류")})
	ResponseEntity<ResponseVO<String>> deleteGround(@ApiParam(value = "groundId") @PathVariable Integer groundId,
		@RequestHeader String Authorization) {
		try {
			log.info("controller - modifyActiveTime :: 그라운드 연구시간 수정 진입");
			UserDto userDto = jwtService.getUser(Authorization)
				.orElseThrow(() -> new NoSuchElementException("getUserInfo :: 존재하지 않는 사용자입니다."));

			GroundDto groundDto = groundService.getGroundInfo(groundId)
				.orElseThrow(() -> new NoSuchElementException("getGroundInfo :: 존재하지 않는 그라운드입니다."));

			if (!groundUserService.checkIsGroundOwner(groundId, userDto.getId())) {
				throw new NoSuchElementException("해당 그라운드의 owner가 아닙니다");
			}

			groundService.deleteGround(groundDto);

			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.OK.value())
				.message("그라운드 삭제 성공!")
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);

		} catch (NoSuchFieldException e) {
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
		} catch (NoSuchElementException e) {
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			ResponseVO<String> responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
			return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
