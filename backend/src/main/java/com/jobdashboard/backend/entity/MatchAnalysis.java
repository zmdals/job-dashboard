package com.jobdashboard.backend.entity;

import com.jobdashboard.backend.entity.base.BaseCreatedEntity;
import com.jobdashboard.backend.entity.enums.AiStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

import static com.jobdashboard.backend.entity.enums.AiStatus.PENDING;

/**
 * 매칭 분석 엔티티
 * FK → Application · AI 확장 지점
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MatchAnalysis extends BaseCreatedEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Application application;

    // 종합 점수
    @Column(name = "total_score")
    private BigDecimal totalScore;

    // 예상 합격률 (%)
    @Column(name = "pass_probability")
    private BigDecimal passProbability;

    // AI 종합 코멘트
    @Column(columnDefinition = "TEXT")
    private String summary;

    // AI 응답 상태 - 매칭 분석 요청
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "ai_status")
    private AiStatus aiStatus = PENDING;

}
