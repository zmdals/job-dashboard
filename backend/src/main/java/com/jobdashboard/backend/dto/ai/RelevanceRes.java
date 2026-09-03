package com.jobdashboard.backend.dto.ai;

import java.util.List;

/**
 *
 */
public class RelevanceRes {

    private Integer score; // 적합도 0~100

    private String summary; // AI 분석 요약

    private List<String> strengths; // 강점

    private List<String> weaknesses;

}
