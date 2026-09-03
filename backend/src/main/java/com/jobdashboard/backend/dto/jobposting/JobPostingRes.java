package com.jobdashboard.backend.dto.jobposting;

import com.jobdashboard.backend.entity.JobPosting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 *  클라이언트에게 보여줄 필드만
 *  password 같은 민감정보 제외
 */

@Getter
@Builder
@AllArgsConstructor
public class JobPostingRes {

    private Long id;

    private Long companyId;

    private String companyName;

    private String title;

    private String url;

    private String jobType;

    private String location;

    private String annualIncome;

    private LocalDate deadline;

    private String description;

    private Integer relevanceScore; // AI 적합도 (0~100) - mock 데이터

    // static from(Entity) 메서드로 엔티티 → DTO 변환
    public static JobPostingRes from(JobPosting jobPosting,Integer relevanceScore){
        return JobPostingRes.builder()
                .id(jobPosting.getId())
                .companyId(jobPosting.getCompany().getId())
                .companyName(jobPosting.getCompany().getName())
                .title(jobPosting.getTitle())
                .url(jobPosting.getUrl())
                .jobType(jobPosting.getJobType())
                .location(jobPosting.getLocation())
                .annualIncome(jobPosting.getAnnualIncome())
                .deadline(jobPosting.getDeadline())
                .description(jobPosting.getDescription())
                .relevanceScore(relevanceScore)
                .build();
    }
}
