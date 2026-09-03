package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.CoverLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CoverLetterRepository extends JpaRepository<CoverLetter, Long> {
    boolean existsByApplicationId(Long applicationId);

    Optional<CoverLetter> findByApplicationId(Long applicationId);
}
