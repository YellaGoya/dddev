package com.d103.dddev.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.d103.dddev.api.common.filter.JwtAuthenticationProcessingFilter;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtService jwtService;
	private final UserRepository userRepository;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.formLogin().disable()
			.httpBasic().disable()
			.cors()
			.configurationSource(corsConfigurationSource())
			.and()
			.csrf().disable()
			.headers().frameOptions().disable()
			.and()

			// 세션 사용하지 않음
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/swagger-ui.html").permitAll()
			.antMatchers("/oauth/**").permitAll()
			.antMatchers("/swagger-resources/**", "/v2/api-docs", "/swagger-resources",
				"/swagger-ui.html", "/webjars/**", "/swagger/**", "/swagger-ui/**").permitAll()
			.antMatchers("/", "/**", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**").permitAll()
			.antMatchers("/v3/api-docs", "//v3/api-docs", "/configuration/ui", "/configuration/security",
				"//swagger-ui.html", "/webjars/**").permitAll()
			//			.anyRequest().authenticated();
			.anyRequest().permitAll();
		//		http.addFilterAfter(jwtAuthenticationProcessingFilter(), LogoutFilter.class);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();

		corsConfiguration.setAllowCredentials(true); // 내 서버가 응답할 때 json을 JS에서 처리할 수 있게 설정
		corsConfiguration.setExposedHeaders(List.of("*")); // 헤더 값 접근

		corsConfiguration.addAllowedOriginPattern("http://localhost:3000");
		corsConfiguration.addAllowedOriginPattern("https://k9d103.p.ssafy.io");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.setMaxAge(7200L);
		corsConfiguration.addExposedHeader("Authorization");
		corsConfiguration.addExposedHeader("Authorization-refresh");
		corsConfiguration.addExposedHeader("nickname");
		corsConfiguration.addExposedHeader("role");
		corsConfiguration.addExposedHeader("githubId");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
		JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService,
			userRepository);
		return jwtAuthenticationFilter;
	}
}
