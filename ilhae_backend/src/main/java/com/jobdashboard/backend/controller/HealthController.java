package com.jobdashboard.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버 상태 확인용 헬스체크 엔드포인트
 * UptimeRobot 등 외부 모니터링 서비스가 주기적으로 호출하여
 * Render 무료 플랜의 Cold Start(15분 무요청 시 서버 종료)를 방지.
 */
@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }
}