package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.dto.coverletter.CoverLetterReq;
import com.jobdashboard.backend.dto.coverletter.CoverLetterRes;
import com.jobdashboard.backend.service.CoverLetterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "자소서", description = "지원 내역별 자소서 API")
public class CoverLetterController {

    private final CoverLetterService coverLetterService;

    @Operation(summary = "자소서 조회")
    @GetMapping("/api/applications/{applicationId}/cover-letter")
    public ApiResponse<CoverLetterRes> get(
            @PathVariable Long applicationId) {
        Long userId = 1L;
        return ApiResponse.ok(
                coverLetterService.get(applicationId, userId));
    }

    @Operation(summary = "자소서 생성")
    @PostMapping("/api/applications/{applicationId}/cover-letter")
    public ApiResponse<CoverLetterRes> create(
            @PathVariable Long applicationId,
            @Valid @RequestBody CoverLetterReq req) {
        Long userId = 1L;
        return ApiResponse.ok(
                coverLetterService.create(applicationId, userId, req));
    }
}