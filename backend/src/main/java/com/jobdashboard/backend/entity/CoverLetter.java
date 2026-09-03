package com.jobdashboard.backend.entity;

import com.jobdashboard.backend.entity.base.BaseCreatedEntity;
import com.jobdashboard.backend.entity.enums.AiStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static com.jobdashboard.backend.entity.enums.AiStatus.*;

/**
 * 자소서 엔티티
 * FK → Application · AI 확장 지점
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CoverLetter extends BaseCreatedEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Application application;

    private String title;

    //자소서 실제 내용 - 긴 문장 TEXT
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "ai_feedback", columnDefinition = "TEXT")
    private String aiFeedback;

    // AI 응답 상태 - 자소서 피드백 요청
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "ai_status")
    private AiStatus aiStatus = PENDING;

    // Java + DB 기본값 1 보장
    @Builder.Default
    @Column(columnDefinition = "INTEGER DEFAULT 1")
    private Integer version = 1;

}
