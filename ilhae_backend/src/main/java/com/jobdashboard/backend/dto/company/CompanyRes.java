package com.jobdashboard.backend.dto.company;

import com.jobdashboard.backend.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CompanyRes {

    private Long id;
    private String name;
    private String industry;
    private String description;
    private String url;
    private String dartCorpCode;

    public static CompanyRes from(Company company) {
        return CompanyRes.builder()
                .id(company.getId())
                .name(company.getName())
                .industry(company.getIndustry())
                .description(company.getDescription())
                .url(company.getUrl())
                .dartCorpCode(company.getDartCorpCode())
                .build();
    }
}
