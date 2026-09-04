package com.jobdashboard.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobdashboard.backend.entity.Career;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {

    List<Career> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Career> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}