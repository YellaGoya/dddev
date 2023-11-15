package com.dddev.log.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 허용할 origin
                .allowedOrigins("https://k9d103.p.ssafy.io") // 허용할 origin
                .allowedOrigins("https://k9d103.p.ssafy.io:8001") // 허용할 origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true) // 인증정보 허용
                .maxAge(3600); // 최대 캐시 시간
    }
}