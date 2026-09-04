package com.jobdashboard.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenRes {
    private String accessToken;
    private String tokenType;
    private Long userId;
    private String name;

    public static TokenRes of(String token, Long userId, String name) {
        return TokenRes.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(userId)
                .name(name)
                .build();
    }
}
