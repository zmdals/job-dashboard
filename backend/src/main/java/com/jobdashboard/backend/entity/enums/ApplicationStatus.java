package com.jobdashboard.backend.entity.enums;

/**
 * 지원 상태(수정 필요)
 */
public enum ApplicationStatus {
    PREPARING,    // 지원 준비
    APPLIED,      // 서류 접수
    IN_PROGRESS,  // 전형 진행 중 (면접, 코테 등)
    ACCEPTED,     // 최종 합격
    REJECTED      // 불합격
}
