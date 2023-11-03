package com.d103.dddev.config;

import com.d103.dddev.api.common.interceptor.GroundIdValidationInterceptor;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.ground.repository.GroundRepository;
import com.d103.dddev.api.ground.repository.GroundUserRepository;
import com.d103.dddev.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final GroundRepository groundRepository;
    private final GroundUserRepository groundUserRepository;
    private final JwtService jwtService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // GroundIdValidationInterceptor 인스턴스를 생성하여 Interceptor로 등록합니다.
        registry.addInterceptor(new GroundIdValidationInterceptor(groundRepository, groundUserRepository, jwtService))
                .addPathPatterns("/ground/{groundId}"); // Interceptor를 적용할 경로 패턴을 지정합니다.
    }
}
