package com.jobdashboard.backend.dto.coverletter;

import com.jobdashboard.backend.entity.Application;
import com.jobdashboard.backend.entity.CoverLetter;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CoverLetterReq {

    private String title;

    @NotBlank(message = "자기소개서 입력은 필수입니다.")
    private String content;

    public CoverLetter toEntity(Application application) {
        return CoverLetter.builder()
                .application(application)
                .title(title)
                .content(content)
                .build();
    }

}
