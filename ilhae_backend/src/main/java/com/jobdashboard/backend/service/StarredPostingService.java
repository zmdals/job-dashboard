package com.jobdashboard.backend.service;

import com.jobdashboard.backend.dto.starredposting.StarredPostingRes;
import com.jobdashboard.backend.entity.JobPosting;
import com.jobdashboard.backend.entity.StarredPosting;
import com.jobdashboard.backend.entity.User;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.JobPostingRepository;
import com.jobdashboard.backend.repository.StarredPostingRepository;
import com.jobdashboard.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StarredPostingService {

    private final StarredPostingRepository starredPostingRepository;
    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;

    public List<StarredPostingRes> getAll(Long userId) {
        return starredPostingRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(StarredPostingRes::from)
                .toList();
    }

    @Transactional
    public StarredPostingRes create(Long userId, Long postingId) {
        if (starredPostingRepository.existsByUserIdAndJobPostingId(userId, postingId)) {
            throw new IllegalStateException("이미 즐겨찾기한 공고입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        JobPosting jobPosting = jobPostingRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException("공고를 찾을 수 없습니다."));

        StarredPosting starredPosting = StarredPosting.builder()
                .user(user)
                .jobPosting(jobPosting)
                .build();

        return StarredPostingRes.from(starredPostingRepository.save(starredPosting));
    }

    @Transactional
    public void remove(Long userId, Long postingId) {
        StarredPosting starredPosting = starredPostingRepository
                .findByUserIdAndJobPostingId(userId, postingId)
                .orElseThrow(() -> new ResourceNotFoundException("즐겨찾기를 찾을 수 없습니다."));
        starredPostingRepository.deleteById(starredPosting.getId());
    }
}
