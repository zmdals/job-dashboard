package com.jobdashboard.backend.dto.matchanalysis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobdashboard.backend.entity.MatchAnalysis;
import com.jobdashboard.backend.entity.base.BaseCreatedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MatchAnalysisRes {
    private Long id;
    private Long applicationId;
    private Integer score;
    private String summary;
    private List<String> strengths;
    private List<String> weaknesses;
    private String recommendation;
    private Boolean includedCoverLetter;

    public static MatchAnalysisRes from(MatchAnalysis analysis) {
        return MatchAnalysisRes.builder()
                .id(analysis.getId())
                .applicationId(analysis.getApplication().getId())
                .score(analysis.getTotalScore().intValue())
                .summary(analysis.getSummary())
                .strengths(parseJson(analysis.getStrengths()))
                .weaknesses(parseJson(analysis.getWeaknesses()))
                .recommendation(analysis.getRecommendation())
                .includedCoverLetter(analysis.getIncludedCoverLetter())
                .build();
    }

    private static List<String> parseJson(String json) {
        if (json == null) return List.of();
        try {
            return new ObjectMapper().readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return List.of();
        }
    }
}
