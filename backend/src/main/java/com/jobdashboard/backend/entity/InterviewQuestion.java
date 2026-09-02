package com.jobdashboard.backend.entity;

import com.jobdashboard.backend.entity.base.BaseCreatedEntity;
import com.jobdashboard.backend.entity.enums.AiStatus;
import com.jobdashboard.backend.entity.enums.InterviewCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * 면접 질문 엔티티
 * FK → Application · AI 확장 지점
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class InterviewQuestion extends BaseCreatedEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Application application;

    // 면접 예상 질문
    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    // 모범 답변
    @Column(name = "sample_answer")
    private String sampleAnswer;

    // 질문 카테고리
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewCategory category;

    // AI 응답 상태 - 면접 질문 생성 요청
    @Enumerated(EnumType.STRING)
    @Column(name = "ai_status")
    private AiStatus aiStatus;

}
