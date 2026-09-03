package com.jobdashboard.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobdashboard.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // User 엔티티에 대한 CRUD 메서드 제공

}
