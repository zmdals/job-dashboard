package com.jobdashboard.backend.entity.enums;

/**
 * 지원 상태(수정 필요)
 */
public enum ApplicationStatus {
    PREPARING,         // 서류준비
    APPLIED,           // 지원완료
    CODING_TEST,       // 코딩테스트 (필기시험)
    FIRST_INTERVIEW,   // 1차면접
    SECOND_INTERVIEW,  // 2차면접
    FINAL_INTERVIEW,   // 최종면접
    ACCEPTED,          // 최종합격
    REJECTED           // 탈락
}
