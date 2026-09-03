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

    // 전체 프로젝트 조회
    // findAll()로 엔티티 목록을 가져온 뒤 ProjectRes로 변환한다.
    public List<ProjectRes> getAll() {
        return projectRepository.findAll().stream()
                .map(ProjectRes::from)
                .toList();
    }

    // 단일 프로젝트 조회
    // ID로 조회하고, 데이터가 없으면 예외를 발생시킨다.
    public ProjectRes get(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));
        return ProjectRes.from(project);
    }

    // 프로젝트 생성
    // 사용자 정보를 연결한 뒤 요청 DTO를 Project 엔티티로 변환하고 저장한다.
    @Transactional
    public ProjectRes create(ProjectReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Project project = req.toEntity(user);

        return ProjectRes.from(projectRepository.save(project));
    }

    // 프로젝트 삭제
    @Transactional
    public void remove(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("프로젝트를 찾을 수 없습니다.");
        }
        projectRepository.deleteById(projectId);
    }

    // 프로젝트 수정
    @Transactional
    public ProjectRes update(ProjectReq req, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        project.update(
                req.getProjectName(),
                req.getRole(),
                req.getTechStack(),
                req.getDescription());

        return ProjectRes.from(project);
    }
}
