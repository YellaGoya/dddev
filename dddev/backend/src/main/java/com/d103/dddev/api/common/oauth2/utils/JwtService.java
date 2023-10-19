package com.d103.dddev.api.common.oauth2.utils;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.d103.dddev.api.user.repository.UserRepository;
import com.d103.dddev.api.user.repository.dto.UserDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {
	@Value("${jwt.secretKey}")
	private String secretKey;

	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;

	@Value("${jwt.access.header}")
	private String accessHeader;

	@Value("${jwt.refresh.header}")
	private String refreshHeader;

	/**
	 * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
	 * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
	 */
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	private static final String GITHUB_CLAIM = "githubId";
	private static final String BEARER = "Bearer ";

	private final UserRepository userRepository;

	/**
	 * AccessToken 생성 메소드
	 */
	public String createAccessToken(Integer githubId) {
		Date now = new Date();
		return JWT.create()    // JWT 토큰을 생성하는 빌더 반환
			.withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 subject 지정 -> accessToken
			.withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정
			//클레임으로 uid, email 사용.
			//추가적으로 식별자나, 이름 등의 정보를 더 추가하셔도 됩니다.
			//추가하실 경우 .withClaim(클래임 이름, 클래임 값) 으로 설정해주시면 됩니다
			.withClaim(GITHUB_CLAIM, githubId)    // 깃허브 아이디 클레임
			.sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
	}

	/**
	 * RefreshToken 생성
	 * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
	 */
	public String createRefreshToken() {
		Date now = new Date();
		return JWT.create()
			.withSubject(REFRESH_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
			.sign(Algorithm.HMAC512(secretKey));
	}

	/**
	 * AccessToken 헤더에 실어서 보내기
	 */
	public void sendAccessToken(HttpServletResponse response, String accessToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		response.setHeader(accessHeader, accessToken);
		log.info("재발급된 Access Token : {}", accessToken);
	}

	/**
	 * AccessToken + RefreshToken 헤더에 실어서 보내기
	 */
	public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		setAccessTokenHeader(response, accessToken);
		setRefreshTokenHeader(response, refreshToken);
		log.info("sendAccessAndRefreshToken :: Access Token, Refresh Token 헤더 설정 완료");
	}

	/**
	 * 헤더에서 RefreshToken 추출
	 * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
	 * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
	 */
	public Optional<String> extractRefreshToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(refreshHeader))
			.filter(refreshToken -> refreshToken.startsWith(BEARER))
			.map(refreshToken -> refreshToken.replace(BEARER, ""));
	}

	/**
	 * 헤더에서 AccessToken 추출
	 * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
	 * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
	 */
	public Optional<String> extractAccessToken(HttpServletRequest request) {
		log.info("extractAccessToken");
		//System.out.println(request.getHeader(accessHeader));
		return Optional.ofNullable(request.getHeader(accessHeader))
			.filter(accessToken -> accessToken.startsWith(BEARER))
			.map(accessToken -> accessToken.replace(BEARER, ""));
	}

	/**
	 * Bearer __________형식으로 되어있는 accessToken에서 Bearer를 제거하는 함수
	 * */
	public Optional<String> extractAccessHeaderToToken(String accessToken) {
		if (accessToken.startsWith(BEARER)) {
			return Optional.ofNullable(accessToken)
				.map(token -> token.replace(BEARER, ""));
		} else {
			return Optional.of(accessToken);
		}
		// return Optional.ofNullable(accessToken)
		// 	.filter(token -> token.startsWith(BEARER))
		// 	.map(token -> token.replace(BEARER, ""));
	}

	/**
	 * AccessToken에서 Email 추출
	 * 추출 전에 JWT.require()로 검증기 생성
	 * verify로 AceessToken 검증 후
	 * 유효하다면 getClaim()으로 이메일 추출
	 * 유효하지 않다면 빈 Optional 객체 반환
	 */
	public Optional<Integer> extractGithubId(String accessToken) {
		try {
			accessToken = extractAccessHeaderToToken(accessToken).get();
			return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
				.build()
				.verify(accessToken)
				.getClaim(GITHUB_CLAIM)
				.asInt());
		} catch (Exception e) {
			log.error("extractEmail :: 액세스 토큰이 유효하지 않습니다.");
			e.printStackTrace();
			return Optional.empty();
		}
	}

	public Optional<UserDto> getUser(String accessToken) {
		try {
			Integer githubId = extractGithubId(accessToken).orElseThrow(() -> new Exception("깃허브 아이디가 없습니다."));
			return userRepository.findBygithubId(githubId);
		} catch (Exception e) {
			log.error("아이디가 없습니다.");
			return Optional.empty();
		}
	}

	/**
	 * AccessToken 헤더 설정
	 */
	public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
		response.setHeader(accessHeader, accessToken);
	}

	/**
	 * RefreshToken 헤더 설정
	 */
	public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
		response.setHeader(refreshHeader, refreshToken);
	}

	/**
	 * RefreshToken DB 저장(업데이트)
	 */
	public void updateRefreshToken(Integer id, String refreshToken) {
		userRepository.findById(id)
			.ifPresentOrElse(
				user -> {
					user.updateRefreshToken(refreshToken);
					userRepository.saveAndFlush(user);
				},
				() -> new Exception("updateRefreshToken :: 일치하는 회원이 없습니다.")
			);
	}

	public boolean isTokenValid(String token) {
		try {
			JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
			return true;
		} catch (Exception e) {
			log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
			return false;
		}
	}

}