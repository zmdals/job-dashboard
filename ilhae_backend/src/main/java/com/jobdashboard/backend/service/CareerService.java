package com.jobdashboard.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobdashboard.backend.dto.career.CareerReq;
import com.jobdashboard.backend.dto.career.CareerRes;
import com.jobdashboard.backend.entity.Career;
import com.jobdashboard.backend.entity.User;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.CareerRepository;
import com.jobdashboard.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CareerService {

    private final CareerRepository careerRepository;
    private final UserRepository userRepository;

    // 현재 유저의 전체 경력 조회
    public List<CareerRes> getAll(Long userId) {
        return careerRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(CareerRes::from)
                .toList();
    }

    // 경력 생성
    @Transactional
    public CareerRes create(CareerReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Career career = req.toEntity(user);
        return CareerRes.from(careerRepository.save(career));
    }

    // 경력사항 수정 — userId 검증 포함
    @Transactional
    public CareerRes update(CareerReq req, Long careerId, Long userId) {
        Career career = careerRepository.findByIdAndUserId(careerId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("경력사항을 찾을 수 없습니다."));

        career.update(
                req.getCompanyName(),
                req.getPosition(),
                req.getStartDate(),
                req.getEndDate(),
                req.getDescription());
        return CareerRes.from(career);
    }

    // 경력사항 삭제 — userId 검증 포함
    @Transactional
    public void remove(Long careerId, Long userId) {
        if (!careerRepository.existsByIdAndUserId(careerId, userId)) {
            throw new ResourceNotFoundException("경력사항을 찾을 수 없습니다.");
        }
        careerRepository.deleteById(careerId);
    }
}