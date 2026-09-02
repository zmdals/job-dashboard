package com.jobdashboard.backend.entity;

import com.jobdashboard.backend.entity.base.BaseCreatedEntity;
import com.jobdashboard.backend.entity.enums.RequirementCategory;
import com.jobdashboard.backend.entity.enums.RequirementType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * 채용 자격 엔티티
 * 채용 공고, 상세 내용은 필수 값
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class JobRequirement extends BaseCreatedEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private JobPosting jobPosting;

    // 필수/우대 구분
    @Column(name = "requirement_type")
    @Enumerated(EnumType.STRING)
    private RequirementType requirementType;

    // 요건 분류
    @Column(name = "requirement_category")
    @Enumerated(EnumType.STRING)
    private RequirementCategory requirementCategory;

    // 요건 상세 내용
    @Column(nullable = false)
    private String content;

}
