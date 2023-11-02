package com.d103.dddev.api.common.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.common.oauth2.utils.PasswordUtil;
import com.d103.dddev.api.user.repository.UserRepository;
import com.d103.dddev.api.user.repository.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Jwt 인증 필터
 * "/login" 이외의 URI 요청이 왔을 때 처리하는 필터
 *
 * 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청
 * AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AccessToken과 함께 요청
 *
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다.
 * 2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR
 * 3. RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급(RTR 방식)
 *                              인증 성공 처리는 하지 않고 실패 처리
 *
 */

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
	private static final String NO_CHECK_URL_SIGN_IN = "/oauth/sign-in"; // "/oauth/sign-in으로 들어오는 요청은 Filter 작동x
	private static final String NO_CHECK_URL_MAIN = "/"; // "/으로 들어오는 요청은 Filter 작동x
	private static final String NO_CHECK_URL_SWAGGER = "/swagger-ui";    // 스웨거 필터 작동 x 

	private static final String ACCESS_CLAIN = "access";
	private static final String REFRESH_CLAIM = "refresh";

	private final JwtService jwtService;
	private final UserRepository userRepository;

	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		if (requestURI.startsWith(NO_CHECK_URL_SIGN_IN) || requestURI.equals(NO_CHECK_URL_MAIN)
			|| requestURI.startsWith(NO_CHECK_URL_SWAGGER)) {
			filterChain.doFilter(request, response); // "/oauth/sign-in" 요청이 들어오면, 다음 필터 호출
			return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
		}

		// access token 확인하기
		// 사용자 요청 헤더에서 access token 추출
		String accessToken = jwtService.extractAccessToken(request).orElse(null);

		// access token이 있을 경우
		if (accessToken != null) {
			// accessToken 유효성 검증
			if (!checkAccessTokenAndAuthentication(accessToken) || !jwtService.extractTokenType(accessToken)
				.equals(ACCESS_CLAIN)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 accessToken입니다ㅜ");
				return;
			}
		} else {    // access token이 없을 경우
			// refresh token 있는지 확인하기
			String refreshToken = jwtService.extractRefreshToken(request).orElse(null);

			// refresh가 invalid이면 return
			if (!jwtService.isTokenValid(refreshToken) || !jwtService.extractTokenType(refreshToken)
				.equals(REFRESH_CLAIM)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 refreshToken입니다ㅜ");
				return;
			}
		}

		filterChain.doFilter(request, response);

	}

	/**
	 *  [리프레시 토큰으로 유저 정보 찾기 & 액세스 토큰/리프레시 토큰 재발급 메소드]
	 *  파라미터로 들어온 `1헤더에서 추출한 리프레시 토큰으로 DB에서 유저를 찾고, 해당 유저가 있다면
	 *  JwtService.createAccessToken()으로 AccessToken 생성,
	 *  reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출
	 *  그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
	 */
	// public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken ) throws JWTVerificationException {
	// 	try {
	// 		jwtService.getUser(refreshToken)
	// 			.ifPresent(user -> {
	// 				String reIssuedRefreshToken = reIssueRefreshToken(user);
	// 				String reIssuedAccessToken = jwtService.createAccessToken(user.getGithubId());
	// 				jwtService.sendAccessAndRefreshToken(response, reIssuedAccessToken,
	// 					reIssuedRefreshToken);
	// 				saveAuthentication(user);
	// 			});
	// 	} catch (Exception e) {
	// 		throw new JWTVerificationException("JwtAuthenticationProcessingFilter :: 존재하지 않는 사용자");
	// 	}
	//
	// }

	/**
	 * [액세스 토큰 체크 & 인증 처리 메소드]
	 * request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
	 * 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
	 * 그 유저 객체를 saveAuthentication()으로 인증 처리하여
	 * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
	 * 그 후 다음 인증 필터로 진행
	 */
	public boolean checkAccessTokenAndAuthentication(String token) throws ServletException, IOException {
		log.info("checkAccessTokenAndAuthentication() 호출");
		return Optional.of(token)
			.filter(jwtService::isTokenValid)
			.flatMap(accessToken -> jwtService.extractGithubId(accessToken))
			.flatMap(githubId -> userRepository.findByGithubId(githubId))
			.map(userDto -> {
				saveAuthentication(userDto);
				return true;
			}).orElse(false);
	}

	/**
	 * [인증 허가 메소드]
	 * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
	 *
	 * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
	 * UsernamePasswordAuthenticationToken의 파라미터
	 * 1. 위에서 만든 UserDetailsUser 객체 (유저 정보)
	 * 2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거)
	 * 3. Collection < ? extends GrantedAuthority>로,
	 * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에,
	 * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
	 *
	 * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
	 * setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
	 */
	public void saveAuthentication(UserDto myUser) {
		// 인증 시 사용할 랜덤 비밀번호
		String password = PasswordUtil.generateRandomPassword();

		UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
			.username(myUser.getNickname())
			.password(password)
			.roles(myUser.getRole().name())
			.build();

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
			authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
