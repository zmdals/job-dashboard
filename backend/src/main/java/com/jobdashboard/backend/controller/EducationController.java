package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.dto.education.EducationReq;
import com.jobdashboard.backend.dto.education.EducationRes;
import com.jobdashboard.backend.service.EducationService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/me/educations")
@RequiredArgsConstructor
@Tag(name = "학력", description = "학력사항 CRUD API")
// TODO: 추후 로그인 기능 구현 시, 현재 로그인한 사용자의 ID를 가져와야 함
public class EducationController {

    private final EducationService educationService;

    @Operation(summary = "전체 학력 조회", description = "등록된 모든 학력사항을 조회합니다")

    @GetMapping
    public ApiResponse<List<EducationRes>> getAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.ok(educationService.getAll(userId));
    }
    /* 불필요
    @Operation(summary = "학력 상세 조회", description = "educationId로 학력사항을 조회합니다")
    @GetMapping("/{educationId}")
    public ApiResponse<EducationRes> get(@PathVariable Long educationId) {
        return ApiResponse.ok(educationService.get(educationId));
    }*/

    @Operation(summary = "학력 등록", description = "새 학력사항을 등록합니다")
    @PostMapping
    public ApiResponse<EducationRes> create(@Valid @RequestBody EducationReq req,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.ok(educationService.create(req, userId));
    }

    @Operation(summary = "학력 수정", description = "학력사항을 수정합니다")
    @PutMapping("/{educationId}")
    public ApiResponse<EducationRes> update(@PathVariable Long educationId,
                                            @Valid @RequestBody EducationReq req,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.ok(educationService.update(req, educationId, userId));
    }

    @Operation(summary = "학력 삭제", description = "학력사항을 삭제합니다")
    @DeleteMapping("/{educationId}")
    public ApiResponse<Void> remove(@PathVariable Long educationId,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        educationService.remove(educationId,userId);
        return ApiResponse.ok(null, "삭제 완료");
    }
}
