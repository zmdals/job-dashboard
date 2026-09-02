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
 * 자격증 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Certificate extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 자격증 이름
    @Column(name = "cert_name", nullable = false)
    private String certName;

    //발급기관
    @Column(nullable = false)
    private String issuer;

    //취득일
    @Column(name = "acquired_date")
    private LocalDate acquiredDate;

}