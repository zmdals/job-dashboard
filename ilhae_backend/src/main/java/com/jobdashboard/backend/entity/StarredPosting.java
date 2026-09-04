package com.jobdashboard.backend.entity;

import com.jobdashboard.backend.entity.base.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_posting_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StarredPosting extends BaseCreatedEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="job_posting_id", nullable = false)
    private JobPosting jobPosting;
}
