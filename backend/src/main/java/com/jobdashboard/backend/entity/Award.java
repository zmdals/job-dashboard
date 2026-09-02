package com.jobdashboard.backend.entity;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.base.BaseCreatedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

/**
 * 수상 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Award extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "award_name", nullable = false)
    // 수상이름
    private String awardName;

    // 주최기관
    private String organizer;

    // 수상일자 - 모를 수 있으니, nullable or 연도만 받기?
    // 프론트에서 연도만 받도록 처리.
    @Column(name = "award_date")
    private LocalDate awardDate;

    // 수상 내용
    private String description;

}
