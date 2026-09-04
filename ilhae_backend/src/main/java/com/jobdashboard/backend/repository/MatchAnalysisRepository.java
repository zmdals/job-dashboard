package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.MatchAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchAnalysisRepository extends JpaRepository<MatchAnalysis, Long> {

    Optional<MatchAnalysis> findByApplicationId(Long applicationId);

    boolean existsByApplicationId(Long applicationId);

    void deleteByApplicationId(Long applicationId);
}