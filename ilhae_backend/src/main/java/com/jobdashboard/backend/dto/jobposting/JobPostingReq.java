package com.jobdashboard.backend.dto.jobposting;

import com.jobdashboard.backend.entity.Company;
import com.jobdashboard.backend.entity.JobPosting;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 *  클라이언트가 보내는 필드만
 *  id, createdAt 같은 건 없음 — 서버가 생성하는 값이니까
 */

@Getter
@NoArgsConstructor
public class JobPostingReq {

    @NotNull
    private Long companyId;

    @NotBlank
    private String title;

    private String url;

    private String jobType;

    private String location;

    private String annualIncome;

    private LocalDate deadline;

    private String description;

    // toEntity() 메서드로 엔티티 변환
    public JobPosting toEntity(Company company){
        return JobPosting.builder()
                .company(company)
                .title(this.title)
                .url(this.url)
                .jobType(this.jobType)
                .location(this.location)
                .annualIncome(this.annualIncome)
                .deadline(this.deadline)
                .description(this.description)
                .build();
    }

}
