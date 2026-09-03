package com.jobdashboard.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobdashboard.backend.entity.Career;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {

}
