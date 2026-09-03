package com.jobdashboard.backend.dto.matchanalysis;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchAnalysisReq {
    private Boolean includeCoverLetter; // 자소서 포함 여부
}
