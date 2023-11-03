package com.d103.dddev.api.common.oauth2.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.common.oauth2.service.Oauth2Service;
import com.d103.dddev.api.common.oauth2.utils.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"GitHub 로그인 api"})
public class Oauth2Controller {

	private final Oauth2Service oauth2Service;
	private final JwtService jwtService;

	@GetMapping("/sign-in")
	@ApiOperation(value = "GitHub 로그인 api", notes = "GitHub 로그인 api")
	public ResponseEntity<String> signIn(@ApiParam(value = "redirect 코드", required = true) @RequestParam String code,
		HttpServletResponse response) {
		try {
			log.info("로그인 api 진입 :: {}", code);
			Map<String, String> login = oauth2Service.login(code);

			// access, refresh, 이름 헤더로 보내기
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("Authorization", login.get("Authorization"));
			response.setHeader("Authorization-refresh", login.get("Authorization-refresh"));
			response.setHeader("nickname", login.get("nickname"));
			response.setHeader("role", login.get("role"));
			response.setHeader("lastGroundId", login.get("lastGroundId"));

			return new ResponseEntity<>("로그인 성공!", HttpStatus.OK);
		} catch (Exception e) {
			log.info("소셜 로그인에 실패했습니다. 에러 메시지 :: {}", e.getMessage());
			return new ResponseEntity<>("로그인 실패ㅜㅜ!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/re-issue")
	@ApiOperation(value = "refresh token으로 새 token 발급", notes = "refresh token으로 새 token 발급받는 API")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "새 토큰 발급 성공"),
		@ApiResponse(code = 403, message = "새 토큰 발급 실패")})
	ResponseEntity<ResponseVO<String>> reIssueTokens(HttpServletRequest request, HttpServletResponse response) {
		ResponseVO<String> responseVO;
		try {
			jwtService.reIssueAccessAndRefreshToken(request, response);
			responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.OK.value())
				.message("새 토큰 발급 성공!")
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			responseVO = ResponseVO.<String>builder()
				.code(HttpStatus.FORBIDDEN.value())
				.message("새 토큰 발급 실패ㅜ")
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.FORBIDDEN);
		}
	}

}
