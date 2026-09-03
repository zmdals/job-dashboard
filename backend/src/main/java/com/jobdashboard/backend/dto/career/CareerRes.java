package com.jobdashboard.backend.dto.career;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.Career;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CareerRes {

    private Long id;

    private String companyName;

    private String position;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    public static CareerRes from(Career career) {
        return CareerRes.builder()
                .id(career.getId())
                .companyName(career.getCompanyName())
                .position(career.getPosition())
                .startDate(career.getStartDate())
                .endDate(career.getEndDate())
                .description(career.getDescription())
                .build();
    }
}
