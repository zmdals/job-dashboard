package com.jobdashboard.backend.dto.company;

import com.jobdashboard.backend.entity.CompanyEvidence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class CompanyEvidenceRes {

    // 엔티티에서 가져오는 값
    private Long id;
    private String sourceType;
    private String title;
    private String sourceUrl;
    private LocalDate publishedAt;
    private Boolean isOfficial;

    // AI Mock 값 (엔티티에 없음, 유저마다 다름)
    private Integer relevanceScore;
    private String aiDescription;

    public static CompanyEvidenceRes from(CompanyEvidence evidence,
                                          Integer relevanceScore,
                                          String aiDescription) {
        return CompanyEvidenceRes.builder()
                .id(evidence.getId())
                .sourceType(evidence.getSourceType().name())
                .title(evidence.getTitle())
                .sourceUrl(evidence.getUrl())
                .publishedAt(evidence.getPublishedAt())
                .isOfficial(evidence.getIsOfficial())
                .relevanceScore(relevanceScore)
                .aiDescription(aiDescription)
                .build();
    }
}