package com.jobdashboard.backend.entity;

import com.jobdashboard.backend.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 회사 정보 엔티티
 * JobPosting(N) : 1
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Company extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    //산업/업종
    @Column(nullable = false)
    private String industry;

    //회사에 대한 간단한 설명
    private String description;

    //회사 공식 홈페이지 주소
    private String url;

    //DART에서 기업을 식별하기 위한 기업 고유 코드. 없으면 NULL -무조건 8자리 숫자 (00으로 시작할수있어서 string)
    @Column(name = "dart_corp_code")
    private String dartCorpCode;




}
