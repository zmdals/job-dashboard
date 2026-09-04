package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.dto.coverletter.CoverLetterReq;
import com.jobdashboard.backend.dto.coverletter.CoverLetterRes;
import com.jobdashboard.backend.security.CustomUserDetails;
import com.jobdashboard.backend.service.CoverLetterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "자소서", description = "지원 내역별 자소서 API")
public class CoverLetterController {

    private final CoverLetterService coverLetterService;

    @Operation(summary = "자소서 조회")
    @GetMapping("/api/applications/{applicationId}/cover-letter")
    public ApiResponse<CoverLetterRes> get(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.ok(
                coverLetterService.get(applicationId, userId));
    }

    @Operation(summary = "자소서 생성")
    @PostMapping("/api/applications/{applicationId}/cover-letter")
    public ApiResponse<CoverLetterRes> create(
            @PathVariable Long applicationId,
            @Valid @RequestBody CoverLetterReq req,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.ok(
                coverLetterService.create(applicationId, userId, req));
    }

    @Operation(summary = "자소서 수정", description = "자소서를 수정합니다")
    @PutMapping("/api/applications/{applicationId}/cover-letter/{coverLetterId}")
    public ApiResponse<CoverLetterRes> update(
            @PathVariable Long applicationId,
            @PathVariable Long coverLetterId,
            @Valid @RequestBody CoverLetterReq req,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.ok(coverLetterService.update(applicationId, coverLetterId, userId, req));
    }

    @Operation(summary = "자소서 삭제", description = "자소서를 삭제합니다")
    @DeleteMapping("/api/applications/{applicationId}/cover-letter/{coverLetterId}")
    public ApiResponse<Void> remove(
            @PathVariable Long applicationId,
            @PathVariable Long coverLetterId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        coverLetterService.remove(applicationId, coverLetterId, userId);
        return ApiResponse.ok(null, "삭제 완료");
    }
}