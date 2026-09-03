package com.jobdashboard.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 자소서 AI 피드백 응답 (비저장)
 */
@Getter
@Builder
@AllArgsConstructor
public class CoverLetterFeedbackRes {
    private Long coverLetterId;
    private Integer score;
    private String summary;
    private List<String> strengths;
    private List<String> improvements;
}