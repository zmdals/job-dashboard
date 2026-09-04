package com.jobdashboard.backend.dto.coverletter;

import java.time.LocalDateTime;

import com.jobdashboard.backend.entity.CoverLetter;
import com.jobdashboard.backend.entity.enums.AiStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoverLetterRes {

    private Long id;
    private Long applicationId;
    private String title;
    private String content;
    private String aiFeedback;
    private AiStatus aiStatus;
    private Integer version;
    private LocalDateTime createdAt;

    public static CoverLetterRes from(CoverLetter coverLetter) {
        return CoverLetterRes.builder()
                .id(coverLetter.getId())
                .applicationId(coverLetter.getApplication().getId())
                .title(coverLetter.getTitle())
                .content(coverLetter.getContent())
                .aiFeedback(coverLetter.getAiFeedback())
                .aiStatus(coverLetter.getAiStatus())
                .version(coverLetter.getVersion())
                .createdAt(coverLetter.getCreatedAt())
                .build();
    }
}
