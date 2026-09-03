package com.jobdashboard.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.dto.project.ProjectReq;
import com.jobdashboard.backend.dto.project.ProjectRes;
import com.jobdashboard.backend.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/me/projects")
@RequiredArgsConstructor
@Tag(name = "프로젝트", description = "프로젝트 CRUD API")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "전체 프로젝트 조회", description = "현재 사용자의 모든 프로젝트를 조회합니다")
    @GetMapping
    public ApiResponse<List<ProjectRes>> getAll() {
        Long userId = 1L;
        return ApiResponse.ok(projectService.getAll(userId));
    }

    @Operation(summary = "프로젝트 등록", description = "새 프로젝트를 등록합니다")
    @PostMapping
    public ApiResponse<ProjectRes> create(@Valid @RequestBody ProjectReq req) {
        Long userId = 1L;
        return ApiResponse.ok(projectService.create(req, userId));
    }

    @Operation(summary = "프로젝트 수정", description = "프로젝트를 수정합니다")
    @PutMapping("/{projectId}")
    public ApiResponse<ProjectRes> update(@PathVariable Long projectId, @Valid @RequestBody ProjectReq req) {
        Long userId = 1L;
        return ApiResponse.ok(projectService.update(req, projectId, userId));
    }

    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다")
    @DeleteMapping("/{projectId}")
    public ApiResponse<Void> remove(@PathVariable Long projectId) {
        Long userId = 1L;
        projectService.remove(projectId, userId);
        return ApiResponse.ok(null, "삭제 완료");
    }
}