package com.jobdashboard.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobdashboard.backend.dto.project.ProjectReq;
import com.jobdashboard.backend.dto.project.ProjectRes;
import com.jobdashboard.backend.entity.Project;
import com.jobdashboard.backend.entity.User;
import com.jobdashboard.backend.exception.ResourceNotFoundException;
import com.jobdashboard.backend.repository.ProjectRepository;
import com.jobdashboard.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // 현재 유저의 전체 프로젝트 조회
    public List<ProjectRes> getAll(Long userId) {
        return projectRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ProjectRes::from)
                .toList();
    }

    // 프로젝트 생성
    @Transactional
    public ProjectRes create(ProjectReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Project project = req.toEntity(user);
        return ProjectRes.from(projectRepository.save(project));
    }

    // 프로젝트 수정 — userId 검증 포함
    @Transactional
    public ProjectRes update(ProjectReq req, Long projectId, Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        project.update(
                req.getProjectName(),
                req.getRole(),
                req.getTechStack(),
                req.getDescription());
        return ProjectRes.from(project);
    }

    // 프로젝트 삭제 — userId 검증 포함
    @Transactional
    public void remove(Long projectId, Long userId) {
        if (!projectRepository.existsByIdAndUserId(projectId, userId)) {
            throw new ResourceNotFoundException("프로젝트를 찾을 수 없습니다.");
        }
        projectRepository.deleteById(projectId);
    }
}