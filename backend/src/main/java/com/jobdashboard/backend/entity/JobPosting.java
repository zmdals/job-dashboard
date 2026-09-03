package com.jobdashboard.backend.entity;

import com.jobdashboard.backend.entity.base.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 채용 공고 엔티티
 * 채용 공고 명, 채용회사는 필수 값
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class JobPosting extends BaseCreatedEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false)
    private String title;

    private String url;

    @Column(name = "job_type")
    private String jobType;

    private String location;

    @Column(name = "annual_income")
    private String annualIncome;

    private LocalDate deadline;

    @Column(columnDefinition = "TEXT")
    private String description;


    // 엔티티 수정 메서드
    public void update(String title, String url,
                       String jobType, String location, String annualIncome,
                       LocalDate deadline, String description) {
        this.title = title;
        this.url = url;
        this.jobType = jobType;
        this.location = location;
        this.annualIncome = annualIncome;
        this.deadline = deadline;
        this.description = description;
    }
}
