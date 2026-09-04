package com.jobdashboard.backend.service;

import com.jobdashboard.backend.dto.jobposting.JobPostingReq;
import com.jobdashboard.backend.dto.jobposting.JobPostingRes;
import com.jobdashboard.backend.entity.Company;
import com.jobdashboard.backend.entity.JobPosting;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.CompanyRepository;
import com.jobdashboard.backend.repository.JobPostingRepository;
import com.jobdashboard.backend.util.MockScoreUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.jobdashboard.backend.util.MockScoreUtil.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;

    // 전체 조회 → findAll → stream으로 Response 변환
    public Page<JobPostingRes> getAllPostings(Pageable pageable) {
        return jobPostingRepository.findAll(pageable)
                // 가상 랜덤 점수 넘김.
                .map(posting -> JobPostingRes.from(posting, fromId(posting.getId())));
    }

    // 단일 상세 조회 → findById → 없으면 ResourceNotFoundException
    public JobPostingRes getPosting(Long postingId) {
        JobPosting jobPosting = jobPostingRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException("공고를 찾을 수 없습니다."));
        return JobPostingRes.from(jobPosting, fromId(postingId));
    }

    // 공고 생성 → @Transactional 붙이고 save
    @Transactional
    public JobPostingRes create(JobPostingReq req) {
        Company company = companyRepository.findById(req.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("회사를 찾을 수 없습니다."));
        JobPosting jobPosting = req.toEntity(company);
        JobPosting savedPosting = jobPostingRepository.save(jobPosting);
        return JobPostingRes.from(savedPosting, fromId(savedPosting.getId()));
    }

    // 공고 수정
    @Transactional
    public JobPostingRes update(JobPostingReq req, Long postingId) {
        JobPosting jobPosting = jobPostingRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException("공고를 찾을 수 없습니다."));

        // company는 안 바꿈 — 공고의 회사가 바뀌는 건 삭제 후 재등록이 맞음
        jobPosting.update(req.getTitle(), req.getUrl(), req.getJobType(),
                req.getLocation(), req.getAnnualIncome(),
                req.getDeadline(), req.getDescription());
        return JobPostingRes.from(jobPosting, fromId(postingId));
    }

    // 공고 삭제 → @Transactional 붙이고 deleteById
    @Transactional
    public void remove(Long postingId) {
        if(!jobPostingRepository.existsById(postingId)){
            throw new ResourceNotFoundException("공고를 찾을 수 없습니다.");
        }
        jobPostingRepository.deleteById(postingId);
    }

}
