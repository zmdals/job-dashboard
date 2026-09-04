package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.StarredPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StarredPostingRepository extends JpaRepository<StarredPosting, Long> {

    List<StarredPosting> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndJobPostingId(Long userId, Long jobPostingId);

    Optional<StarredPosting> findByUserIdAndJobPostingId(Long userId, Long jobPostingId);
}
