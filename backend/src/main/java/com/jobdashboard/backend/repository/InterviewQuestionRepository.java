package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findAllByApplicationIdOrderByCreatedAtDesc(Long applicationId);

    boolean existsByApplicationId(Long applicationId);

    void deleteAllByApplicationId(Long applicationId);
}