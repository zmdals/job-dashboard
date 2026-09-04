package com.jobdashboard.backend.dto.starredposting;

import com.jobdashboard.backend.entity.StarredPosting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class StarredPostingRes {

    private Long id;
    private Long jobPostingId;
    private String companyName;
    private String jobTitle;
    private LocalDateTime createdAt;

    public static StarredPostingRes from(StarredPosting starredPosting) {
        return StarredPostingRes.builder()
                .id(starredPosting.getId())
                .jobPostingId(starredPosting.getJobPosting().getId())
                .companyName(starredPosting.getJobPosting().getCompany().getName())
                .jobTitle(starredPosting.getJobPosting().getTitle())
                .createdAt(starredPosting.getCreatedAt())
                .build();
    }
}
