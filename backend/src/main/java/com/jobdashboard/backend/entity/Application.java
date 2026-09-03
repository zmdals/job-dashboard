package com.jobdashboard.backend.entity;

import java.time.LocalDate;

import com.jobdashboard.backend.entity.base.BaseEntity;
import com.jobdashboard.backend.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import static com.jobdashboard.backend.entity.enums.ApplicationStatus.*;

/**
 * 지원 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    /**
     * 지원 상태 흐름:
     * PREPARING → APPLIED → IN_PROGRESS → ACCEPTED / REJECTED
     */
    @Builder.Default
    @Column(name = "application_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus = PREPARING;

    //nullable이 아니면, 지원 전 상태일때 필수값으로 뭔가 들어가야 함, 모순.
    @Column(name = "applied_date")
    private LocalDate appliedDate;

    // 대시보드에서 사용자의 메모
    @Column(columnDefinition = "TEXT")
    private String memo;

    public void updateStatus(ApplicationStatus status){
        this.applicationStatus = status;
        if (status == ApplicationStatus.APPLIED) {
            this.appliedDate = LocalDate.now();
        }
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }
}
