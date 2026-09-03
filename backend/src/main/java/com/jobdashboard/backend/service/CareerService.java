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

    // 전체 경력 조회
    // findAll()로 엔티티 목록을 가져온 뒤 CareerRes로 변환한다.
    public List<CareerRes> getAll() {
        return careerRepository.findAll().stream()
                .map(CareerRes::from)
                .toList();
    }

    // 단일 경력 조회
    // ID로 조회하고, 데이터가 없으면 예외를 발생시킨다.
    public CareerRes get(Long careerId) {
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new ResourceNotFoundException("경력사항을 찾을 수 없습니다."));
        return CareerRes.from(career);
    }

    // 경력 생성
    // 요청 DTO를 Career 엔티티로 변환한 뒤 Repository를 통해 저장한다.
    @Transactional
    public CareerRes create(CareerReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Career career = req.toEntity(user);

        return CareerRes.from(careerRepository.save(career));
    }

    // 경력사항 수정
    @Transactional
    public CareerRes update(CareerReq req, Long careerId) {
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new ResourceNotFoundException("경력사항을 찾을 수 없습니다."));

        career.update(
                req.getCompanyName(),
                req.getPosition(),
                req.getStartDate(),
                req.getEndDate(),
                req.getDescription());
        return CareerRes.from(career);
    }

    // 경력사항 삭제
    @Transactional
    public void remove(Long careerId) {
        if (!careerRepository.existsById(careerId)) {
            throw new ResourceNotFoundException("경력사항을 찾을 수 없습니다.");
        }
        careerRepository.deleteById(careerId);
    }

}
