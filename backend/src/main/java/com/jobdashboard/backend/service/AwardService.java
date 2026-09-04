package com.jobdashboard.backend.service;

import com.jobdashboard.backend.dto.award.AwardReq;
import com.jobdashboard.backend.dto.award.AwardRes;
import com.jobdashboard.backend.entity.Award;
import com.jobdashboard.backend.entity.User;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.AwardRepository;
import com.jobdashboard.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AwardService {

    private final AwardRepository awardRepository;
    private final UserRepository userRepository;

    public List<AwardRes> getAll(Long userId) {
        return awardRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(AwardRes::from)
                .toList();
    }

    @Transactional
    public AwardRes create(AwardReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        Award award = req.toEntity(user);
        return AwardRes.from(awardRepository.save(award));
    }

    @Transactional
    public AwardRes update(AwardReq req, Long awardId, Long userId) {
        Award award = awardRepository.findByIdAndUserId(awardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("수상 내역을 찾을 수 없습니다."));

        award.update(
                req.getAwardName(),
                req.getOrganizer(),
                req.getAwardDate(),
                req.getDescription());

        return AwardRes.from(award);
    }

    @Transactional
    public void remove(Long awardId, Long userId) {
        if (!awardRepository.existsByIdAndUserId(awardId, userId)) {
            throw new ResourceNotFoundException("수상 내역을 찾을 수 없습니다.");
        }

        awardRepository.deleteById(awardId);
    }
}