package com.jobdashboard.backend.dto.education;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.Education;
import com.jobdashboard.backend.entity.enums.EducationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EducationRes {

    private Long id;

    private String schoolName;

    private String degree;

    private String major;

    private LocalDate startDate;

    private LocalDate endDate;

    private EducationStatus educationStatus;

    public static EducationRes from(Education education) {
        return EducationRes.builder()
                .id(education.getId())
                .schoolName(education.getSchoolName())
                .degree(education.getDegree())
                .major(education.getMajor())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .educationStatus(education.getEducationStatus())
                .build();
    }
}
