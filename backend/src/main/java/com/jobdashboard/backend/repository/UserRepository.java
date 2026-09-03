package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
