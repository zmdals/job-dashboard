package com.jobdashboard.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * 프론트(Vue, localhost:5173)에서 백엔드(Spring, localhost:8080)로
 * API를 호출하면 브라우저가 "출처가 다르다"면서 요청을 막음
 * "이 출처에서 오는 요청은 허용해라"고 Spring한테 알려주는 설정 클래스
 * 없으면 프론트에서 axios 쳤을 때 CORS 에러 뜨면서 아무것도 안 됨
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Vue dev 서버 (Vite 기본 포트)
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:5174"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);  // 세션/쿠키 기반 인증 시 필요

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}