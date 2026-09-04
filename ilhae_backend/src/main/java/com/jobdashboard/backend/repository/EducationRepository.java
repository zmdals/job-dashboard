package com.jobdashboard.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jobdashboard.backend.entity.Education;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    List<Education> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Education> findByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}
