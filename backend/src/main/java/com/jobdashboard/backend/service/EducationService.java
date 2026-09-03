package com.jobdashboard.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobdashboard.backend.dto.education.EducationReq;
import com.jobdashboard.backend.dto.education.EducationRes;
import com.jobdashboard.backend.entity.Education;
import com.jobdashboard.backend.entity.User;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.EducationRepository;
import com.jobdashboard.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationService {

    private final EducationRepository educationRepository;
    private final UserRepository userRepository;

    // 전체 학력 조회
    // findAll()로 엔티티 목록을 가져온 뒤 EducationRes로 변환한다.
    // 기존코드 : getAll()이 전체 유저 학력을 다 가져옴 -> 해당 유저꺼만.
    public List<EducationRes> getAll(Long userId) {
        return educationRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(EducationRes::from)
                .toList();
    }
    /* 불필요
    // 단일 학력 조회
    // ID로 조회하고, 데이터가 없으면 예외를 발생시킨다.
    public EducationRes get(Long educationId) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("학력사항을 찾을 수 없습니다."));
        return EducationRes.from(education);
    } */

    // 학력 생성
    // 요청 DTO를 Education 엔티티로 변환한 뒤 Repository를 통해 저장한다.
    @Transactional
    public EducationRes create(EducationReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Education education = req.toEntity(user);

        return EducationRes.from(educationRepository.save(education));
    }

    // 학력사항 삭제
    @Transactional
    public void remove(Long educationId, Long userId) {
        if (!educationRepository.existsByIdAndUserId(educationId, userId)) {
            throw new ResourceNotFoundException("학력사항을 찾을 수 없습니다.");
        }
        educationRepository.deleteById(educationId);
    }

    // 학력사항 수정
    @Transactional
    public EducationRes update(EducationReq req, Long educationId, Long userId) {
        Education education = educationRepository.findByIdAndUserId(educationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("학력사항을 찾을 수 없습니다."));

        education.update(
                req.getSchoolName(),
                req.getDegree(),
                req.getMajor(),
                req.getStartDate(),
                req.getEndDate(),
                req.getEducationStatus());

        return EducationRes.from(education);
    }
}
