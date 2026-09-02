package com.jobdashboard.backend.entity;

import com.jobdashboard.backend.entity.base.BaseEntity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    //로그인 식별자
    @Column(nullable = false, unique = true)
    private String email;

    //암호화 저장
    @Column(name = "password", nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

}
