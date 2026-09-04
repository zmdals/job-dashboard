package com.jobdashboard.backend.dto.award;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.Award;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AwardRes {

    private Long id;
    private String awardName;
    private String organizer;
    private LocalDate awardDate;
    private String description;

    public static AwardRes from(Award award) {
        return AwardRes.builder()
                .id(award.getId())
                .awardName(award.getAwardName())
                .organizer(award.getOrganizer())
                .awardDate(award.getAwardDate())
                .description(award.getDescription())
                .build();
    }

}
