package com.jobdashboard.backend.service;

import com.jobdashboard.backend.dto.coverletter.CoverLetterReq;
import com.jobdashboard.backend.dto.coverletter.CoverLetterRes;
import com.jobdashboard.backend.entity.Application;
import com.jobdashboard.backend.entity.CoverLetter;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.ApplicationRepository;
import com.jobdashboard.backend.repository.CoverLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoverLetterService {

    private final CoverLetterRepository coverLetterRepository;
    private final ApplicationRepository applicationRepository;

    public CoverLetterRes get(Long applicationId, Long userId) {
        validateApplicationOwner(applicationId, userId);

        CoverLetter coverLetter = coverLetterRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("자소서를 찾을 수 없습니다."));

        return CoverLetterRes.from(coverLetter);
    }

    @Transactional
    public CoverLetterRes create(
            Long applicationId,
            Long userId,
            CoverLetterReq req) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("지원 내역을 찾을 수 없습니다."));

        if (coverLetterRepository.existsByApplicationId(applicationId)) {
            throw new IllegalStateException("이미 해당 지원 내역의 자소서가 존재합니다.");
        }

        CoverLetter coverLetter = req.toEntity(application);
        return CoverLetterRes.from(coverLetterRepository.save(coverLetter));
    }

    private void validateApplicationOwner(Long applicationId, Long userId) {
        if (!applicationRepository.existsByIdAndUserId(applicationId, userId)) {
            throw new ResourceNotFoundException("지원 내역을 찾을 수 없습니다.");
        }
    }
}