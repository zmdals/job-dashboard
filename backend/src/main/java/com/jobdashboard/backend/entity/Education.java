package com.jobdashboard.backend.entity;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.base.BaseCreatedEntity;

import com.jobdashboard.backend.entity.enums.EducationStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 * 학력사항 엔티티
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Education extends BaseCreatedEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(nullable = false, length = 20)
    private String degree;

    @Column(nullable = false)
    private String major;

    // 입학일도 모를 수도 있으니 nullable 고려 - 월까지만 입력받기?
    // LocalDate 쓰고 프론트에서만 월까지 받기
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    //졸업, 수료 상태가 아니면 없을 수 있음 = null.
    // LocalDate 쓰고 프론트에서만 월까지 받기
    @Column(name = "end_date")
    private LocalDate endDate;

    // 재학 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EducationStatus educationStatus;

}
