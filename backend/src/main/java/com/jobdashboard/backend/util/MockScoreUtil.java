package com.jobdashboard.backend.util;

/**
 * postId 기반 가짜 점수 생성 도구
 */
public class MockScoreUtil {

    private MockScoreUtil() {}

    // 같은 ID → 같은 점수 (60~95)
    public static Integer fromId(Long id) {
        int hash = id.hashCode();
        return 60 + Math.abs(hash % 36);
    }
}
