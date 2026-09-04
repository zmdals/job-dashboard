package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    List<Award> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Award> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}