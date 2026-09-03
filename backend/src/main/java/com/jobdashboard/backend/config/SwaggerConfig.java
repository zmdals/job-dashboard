package com.jobdashboard.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 컨트롤러에 만든 API들을 자동으로 문서화해서 웹 UI로 보여주는 도구 클래스
 * localhost:8080/swagger-ui/index.html
 * 접속하면 api 목록 조회 가능.
 *
 */

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI-Ready 취업 대시보드 API")
                        .description("취업 지원 관리 + AI 분석 Mock API")
                        .version("v1.0.0"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local")
                ));
    }
}
