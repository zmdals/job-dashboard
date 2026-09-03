package com.jobdashboard.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * (비저장, 모달용)
 */
@Getter
@Builder
@AllArgsConstructor
public class RelevanceRes {

    private Integer score; // 적합도 0~100

    private String summary; // AI 분석 요약

    private List<String> strengths; // 강점

    private List<String> weaknesses; // 약점

}
