package com.jobdashboard.backend.entity;


import com.jobdashboard.backend.entity.base.BaseEntity;
import com.jobdashboard.backend.entity.enums.SourceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 기업 자료 엔티티
 * company(1) : N
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CompanyEvidence extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id",nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    private SourceType sourceType;

    @Column(nullable = false)
    private String title;

    private String url;

    @Column(name = "published_at")
    private LocalDate publishedAt; // 자료 공개일

    @Column(columnDefinition = "TEXT")
    private String content; // 원문 내용

    @Column(name = "is_official")
    private Boolean isOfficial;
}
