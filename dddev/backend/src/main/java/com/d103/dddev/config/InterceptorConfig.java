package com.d103.dddev.config;

import com.d103.dddev.api.common.interceptor.GroundIdValidationInterceptor;
import com.d103.dddev.api.ground.repository.GroundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final GroundRepository groundRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // GroundIdValidationInterceptor 인스턴스를 생성하여 Interceptor로 등록합니다.
        registry.addInterceptor(new GroundIdValidationInterceptor(groundRepository))
                .addPathPatterns("/ground/{groundId}/sprint/**")
                .addPathPatterns("/ground/{groundId}/general/**"); // Interceptor를 적용할 경로 패턴을 지정합니다.
    }
}
