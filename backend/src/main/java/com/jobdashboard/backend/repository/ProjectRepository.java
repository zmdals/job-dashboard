package com.jobdashboard.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobdashboard.backend.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
