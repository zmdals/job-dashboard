package com.jobdashboard.backend.dto.certificate;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.Certificate;
import com.jobdashboard.backend.entity.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CertificateReq {

    @NotBlank
    private String certName;

    @NotBlank
    private String issuer;

    private LocalDate acquiredDate;

    private Integer languageScore;

    public Certificate toEntity(User user) {
        return Certificate.builder()
                .user(user)
                .certName(this.certName)
                .issuer(this.issuer)
                .acquiredDate(this.acquiredDate)
                .languageScore(this.languageScore)
                .build();
    }
}
