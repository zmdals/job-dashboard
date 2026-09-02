package com.jobdashboard.backend.entity;

import com.jobdashboard.backend.entity.base.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

/**
 * 지원자격 매칭 엔티티
 * FK → MatchAnalysis, JobRequirement · AI 확장 지점
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RequirementMatch extends BaseCreatedEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MatchAnalysis matchAnalysis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_id", nullable = false)
    private JobRequirement jobRequirement;

    // 항목별 점수
    private BigDecimal score;

    // 충족 여부
    @Column(name = "is_met")
    private Boolean isMet;

    // 판단 근거
    private String evidence;

}
