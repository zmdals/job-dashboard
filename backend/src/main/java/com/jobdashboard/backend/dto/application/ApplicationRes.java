package com.jobdashboard.backend.dto.application;

import com.jobdashboard.backend.entity.Application;
import com.jobdashboard.backend.entity.JobPosting;
import com.jobdashboard.backend.entity.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationRes {

    private Long id;
    private Long jobPostingId;
    private String companyName;
    private String jobTitle;
    private ApplicationStatus status;
    private String memo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ApplicationRes from(Application application){
        return ApplicationRes.builder()
                .id(application.getId())
                .jobPostingId(application.getJobPosting().getId())
                .companyName(application.getJobPosting().getCompany().getName())
                .jobTitle(application.getJobPosting().getTitle())
                .status(application.getApplicationStatus())
                .memo(application.getMemo())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
