package com.jobdashboard.backend.dto.ai;

import com.jobdashboard.backend.entity.InterviewQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 면접 예상 질문 응답
 */
@Getter
@Builder
@AllArgsConstructor
public class InterviewQuestionRes {
    private Long id;
    private String question;
    private String sampleAnswer;
    private String category;

    public static InterviewQuestionRes from(InterviewQuestion iq) {
        return InterviewQuestionRes.builder()
                .id(iq.getId())
                .question(iq.getQuestion())
                .sampleAnswer(iq.getSampleAnswer())
                .category(iq.getCategory().name())
                .build();
    }
}