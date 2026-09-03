package com.jobdashboard.backend.dto.education;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.Education;
import com.jobdashboard.backend.entity.User;
import com.jobdashboard.backend.entity.enums.EducationStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EducationReq {

    @NotBlank
    private String schoolName;

    @NotBlank
    private String degree;

    @NotBlank
    private String major;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private EducationStatus educationStatus;

    // toEntity() 메서드로 엔티티 변환
    public Education toEntity(User user) {
        return Education.builder()
                .user(user)
                .schoolName(this.schoolName)
                .degree(this.degree)
                .major(this.major)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .educationStatus(this.educationStatus)
                .build();

    }
}
