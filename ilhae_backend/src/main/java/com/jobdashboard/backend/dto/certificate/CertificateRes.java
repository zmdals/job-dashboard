package com.jobdashboard.backend.dto.certificate;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.Certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CertificateRes {

    private Long id;

    private String certName;

    private String issuer;

    private LocalDate acquiredDate;

    private Integer languageScore;

    public static CertificateRes from(Certificate certificate) {
        return CertificateRes.builder()
                .id(certificate.getId())
                .certName(certificate.getCertName())
                .issuer(certificate.getIssuer())
                .acquiredDate(certificate.getAcquiredDate())
                .languageScore(certificate.getLanguageScore())
                .build();
    }
}
