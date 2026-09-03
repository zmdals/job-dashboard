package com.jobdashboard.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobdashboard.backend.entity.Certificate;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

}
