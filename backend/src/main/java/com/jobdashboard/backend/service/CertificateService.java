package com.jobdashboard.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobdashboard.backend.dto.certificate.CertificateReq;
import com.jobdashboard.backend.dto.certificate.CertificateRes;
import com.jobdashboard.backend.entity.Certificate;
import com.jobdashboard.backend.entity.User;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.CertificateRepository;
import com.jobdashboard.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;

    // 전체 자격증 조회
    // findAll()로 엔티티 목록을 가져온 뒤 CertificateRes로 변환한다.
    public List<CertificateRes> getAll(Long userId) {
        return certificateRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(CertificateRes::from)
                .toList();
    }
    /* 불필요
    // 단일 자격증 조회
    // ID로 조회하고, 데이터가 없으면 예외를 발생시킨다.
    public CertificateRes get(Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("자격증을 찾을 수 없습니다."));
        return CertificateRes.from(certificate);
    } */

    // 자격증 생성
    // 사용자 정보를 연결한 뒤 요청 DTO를 Certificate 엔티티로 변환하고 저장한다.
    @Transactional
    public CertificateRes create(CertificateReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Certificate certificate = req.toEntity(user);

        return CertificateRes.from(certificateRepository.save(certificate));
    }

    // 자격증 삭제
    @Transactional
    public void remove(Long certificateId, Long userId) {
        if (!certificateRepository.existsByIdAndUserId(certificateId, userId)) {
            throw new ResourceNotFoundException("자격증을 찾을 수 없습니다.");
        }
        certificateRepository.deleteById(certificateId);
    }

    // 자격증 수정
    @Transactional
    public CertificateRes update(CertificateReq req, Long certificateId, Long userId) {
        Certificate certificate = certificateRepository.findByIdAndUserId(certificateId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("자격증을 찾을 수 없습니다."));

        certificate.update(
                req.getCertName(),
                req.getIssuer(),
                req.getAcquiredDate(),
                req.getLanguageScore());

        return CertificateRes.from(certificate);
    }
}
