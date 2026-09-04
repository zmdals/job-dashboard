package com.jobdashboard.backend.service;

import com.jobdashboard.backend.dto.company.CompanyEvidenceRes;
import com.jobdashboard.backend.dto.company.CompanyRes;
import com.jobdashboard.backend.entity.Company;
import com.jobdashboard.backend.entity.CompanyEvidence;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.CompanyEvidenceRepository;
import com.jobdashboard.backend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyEvidenceRepository companyEvidenceRepository;

    // 회사 정보 조회 (모달용)
    public CompanyRes getCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("회사를 찾을 수 없습니다."));
        return CompanyRes.from(company);
    }

    // 맞춤 기업 리포트 — 기업 자료 + AI Mock 관련도
    public List<CompanyEvidenceRes> getEvidenceReport(Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("회사를 찾을 수 없습니다.");
        }

        List<CompanyEvidence> evidences = companyEvidenceRepository.findAllByCompanyId(companyId);
        return evidences.stream()
                .map(e -> CompanyEvidenceRes.from(
                        e,
                        generateMockRelevance(),
                        generateMockDescription(e)
                ))
                .toList();
    }

    // Mock: 70~95 사이 관련도 — AI 붙이면 유저 프로필 기반 계산으로 교체
    private Integer generateMockRelevance() {
        return 70 + new Random().nextInt(26);
    }

    // Mock: sourceType별 맞춤 설명 — AI 붙이면 유저 프로필 vs 자료 비교 분석으로 교체
    private static final Map<String, String> DESCRIPTION_TEMPLATES = Map.of(
            "PATENT",        "내 프로필의 기술 경험과 연결되는 최근 특허입니다.",
            "PAPER",         "지원 직무와 관련된 연구 주제를 AI가 선별했습니다.",
            "IR",            "지원 직무와 기업의 사업 방향을 이해하는 데 도움이 되는 자료입니다.",
            "TECH_BLOG",     "내 기술 스택과 관련성이 높은 기술 블로그 글입니다.",
            "PRESS_RELEASE", "최근 기업 동향을 파악할 수 있는 공식 보도자료입니다.",
            "NEWS",          "기업의 최신 뉴스를 AI가 선별했습니다."
    );

    private String generateMockDescription(CompanyEvidence evidence) {
        String type = evidence.getSourceType().name();
        return DESCRIPTION_TEMPLATES.getOrDefault(type,
                "내 프로필과 관련성이 높은 기업 자료입니다.");
    }
}