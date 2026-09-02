package com.jobdashboard.backend.entity;

import jakarta.persistence.Table;
import lombok.Getter;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users")
@Getter

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String name;

    private String phone;

    protected User() {
        // Default constructor for JPA
    }
}
