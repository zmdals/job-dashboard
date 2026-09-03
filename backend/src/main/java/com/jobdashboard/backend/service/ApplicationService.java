package com.jobdashboard.backend.service;

import com.jobdashboard.backend.dto.application.ApplicationRes;
import com.jobdashboard.backend.dto.application.StatusUpdateReq;
import com.jobdashboard.backend.entity.Application;
import com.jobdashboard.backend.entity.JobPosting;
import com.jobdashboard.backend.entity.User;
import com.jobdashboard.backend.entity.enums.ApplicationStatus;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.ApplicationRepository;
import com.jobdashboard.backend.repository.JobPostingRepository;
import com.jobdashboard.backend.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;

    private static final Map<ApplicationStatus, Set<ApplicationStatus>> VALID_TRANSITIONS = Map.of(
            ApplicationStatus.PREPARING, Set.of(ApplicationStatus.APPLIED, ApplicationStatus.REJECTED),
            ApplicationStatus.APPLIED, Set.of(ApplicationStatus.IN_PROGRESS, ApplicationStatus.REJECTED),
            ApplicationStatus.IN_PROGRESS, Set.of(ApplicationStatus.ACCEPTED, ApplicationStatus.REJECTED)
    );

    // 유저로 지원내역 전체 조회
    public List<ApplicationRes> getAll(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("유저를 찾을 수 없습니다."));
        return applicationRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ApplicationRes::from)
                .toList();
    }

    // 공고에서 지원하기 버튼 -> 지원 내역에 추가
    @Transactional
    public ApplicationRes create(Long userId, Long postingId) {
        if(applicationRepository.existsByUserIdAndJobPostingId(userId, postingId)) {
            throw new IllegalStateException("이미 해당 공고에 지원 이력이 있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        JobPosting jobPosting = jobPostingRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 채용공고입니다. id=" + postingId));

        Application application = Application.builder()
                .user(user)
                .jobPosting(jobPosting)
                .build();

        Application saved = applicationRepository.save(application);
        return ApplicationRes.from(saved);

    }

    // 지원 상태 수정
    @Transactional
    public ApplicationRes updateStatus(Long applicationId, Long userId, StatusUpdateReq req) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("지원 내역을 찾을 수 없습니다. id=" + applicationId));

        validateTransition(application.getApplicationStatus(), req.getStatus());

        application.updateStatus(req.getStatus());
        if(req.getMemo() != null) {
            application.updateMemo(req.getMemo());
        }

        return ApplicationRes.from(application);
    }

    // 지원 내역 삭제
    @Transactional
    public void remove(Long applicationId, Long userId) {
        if(!applicationRepository.existsByIdAndUserId(applicationId,userId)){
            throw new ResourceNotFoundException("지원 내역을 찾을 수 없습니다. id=" + applicationId);
        }
        applicationRepository.deleteById(applicationId);
    }

    /**
     * 상태 전이 규칙 검증
     *
     * VALID_TRANSITIONS 맵에 "현재 상태 → 갈 수 있는 상태 목록"이 정의돼 있음
     *   PREPARING   → APPLIED, REJECTED
     *   APPLIED     → IN_PROGRESS, REJECTED
     *   IN_PROGRESS → ACCEPTED, REJECTED
     *
     * 맵에 키가 없는 상태(ACCEPTED, REJECTED)는 최종 상태라 더 이상 변경 불가.
     * 예: PREPARING에서 바로 ACCEPTED로 바꾸려 하면 → 허용 목록에 없으니까 예외 터짐.
     */
    private void validateTransition(ApplicationStatus current, ApplicationStatus target) {
        Set<ApplicationStatus> allowed = VALID_TRANSITIONS.get(current);
        // allowed == null → 현재 상태가 최종 상태(ACCEPTED/REJECTED)라 전이 자체가 불가
        // !allowed.contains(target) → 갈 수 있는 상태 목록에 없는 곳으로 가려는 시도
        if (allowed == null || !allowed.contains(target)) {
            throw new IllegalStateException(
                    String.format("'%s' → '%s' 상태 변경이 불가합니다.", current, target));
        }
    }
}
