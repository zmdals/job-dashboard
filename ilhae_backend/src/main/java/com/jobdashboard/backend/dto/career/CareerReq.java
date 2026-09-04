package com.jobdashboard.backend.dto.career;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.Career;
import com.jobdashboard.backend.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CareerReq {

    @NotBlank
    private String companyName;

    @NotBlank
    private String position;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    public Career toEntity(User user) {
        return Career.builder()
                .user(user)
                .companyName(this.companyName)
                .position(this.position)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .description(this.description)
                .build();
    }
}
