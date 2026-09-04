package com.jobdashboard.backend.repository;

import com.jobdashboard.backend.entity.CompanyEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyEvidenceRepository extends JpaRepository<CompanyEvidence,Long> {
    List<CompanyEvidence> findAllByCompanyId(Long companyId);
}
