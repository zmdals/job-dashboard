package com.jobdashboard.backend.dto.application;

import com.jobdashboard.backend.entity.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StatusUpdateReq {

    @NotNull(message = "변경할 상태를 지정해주세요.")
    private ApplicationStatus status;

    private String memo;
}
