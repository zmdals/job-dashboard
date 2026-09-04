package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

    List<Application> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Application> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserIdAndJobPostingId(Long userId, Long postingId);

    boolean existsByIdAndUserId(Long applicationId, Long userId);
}
