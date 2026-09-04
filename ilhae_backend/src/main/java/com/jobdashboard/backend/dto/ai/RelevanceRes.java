package com.jobdashboard.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 적합도 응답 (비저장, 모달용)
 */
@Getter
@Builder
@AllArgsConstructor
public class RelevanceRes {
    private Integer score;
    private String summary;
    private List<String> strengths;
    private List<String> weaknesses;
}