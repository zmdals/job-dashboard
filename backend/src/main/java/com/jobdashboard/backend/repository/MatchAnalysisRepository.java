package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.MatchAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchAnalysisRepository extends JpaRepository<MatchAnalysis,Long> {
    boolean existsByApplicationId(Long applicationId);
}
