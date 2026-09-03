package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

}
