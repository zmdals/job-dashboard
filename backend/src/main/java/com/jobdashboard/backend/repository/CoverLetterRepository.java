package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.CoverLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverLetterRepository extends JpaRepository<CoverLetter,Long> {
    boolean existsByApplicationId(Long applicationId);
}
