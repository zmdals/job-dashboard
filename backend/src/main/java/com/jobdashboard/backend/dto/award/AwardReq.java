package com.jobdashboard.backend.dto.award;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.Award;
import com.jobdashboard.backend.entity.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AwardReq {

    @NotBlank(message = "수상명은 필수입니다.")
    private String awardName;

    private String organizer;
    private LocalDate awardDate;
    private String description;

    public Award toEntity(User user) {
        return Award.builder()
                .user(user)
                .awardName(awardName)
                .organizer(organizer)
                .awardDate(awardDate)
                .description(description)
                .build();
    }
}
