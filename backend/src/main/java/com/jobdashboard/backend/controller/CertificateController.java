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

import com.jobdashboard.backend.dto.certificate.CertificateReq;
import com.jobdashboard.backend.dto.certificate.CertificateRes;
import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.service.CertificateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/me/certificates")
@RequiredArgsConstructor
@Tag(name = "자격증", description = "자격증 CRUD API")
// TODO: 추후 로그인 기능 구현 시, 현재 로그인한 사용자의 ID를 가져와야 함
public class CertificateController {

    private final CertificateService certificateService;

    @Operation(summary = "전체 자격증 조회", description = "등록된 모든 자격증을 조회합니다")

    @GetMapping
    public ApiResponse<List<CertificateRes>> getAll() {
        Long userId = 1L;
        return ApiResponse.ok(certificateService.getAll(userId));
    }

    /* 불필요
    @Operation(summary = "자격증 상세 조회", description = "certificateId로 자격증을 조회합니다")
    @GetMapping("/{certificateId}")
    public ApiResponse<CertificateRes> get(@PathVariable Long certificateId) {
        return ApiResponse.ok(certificateService.get(certificateId));
    } */

    @Operation(summary = "자격증 등록", description = "새 자격증을 등록합니다")
    @PostMapping
    public ApiResponse<CertificateRes> create(@Valid @RequestBody CertificateReq req) {
        Long userId = 1L;
        return ApiResponse.ok(certificateService.create(req, userId));
    }

    @Operation(summary = "자격증 수정", description = "자격증을 수정합니다")
    @PutMapping("/{certificateId}")
    public ApiResponse<CertificateRes> update(@PathVariable Long certificateId,
            @Valid @RequestBody CertificateReq req) {
        Long userId = 1L;
        return ApiResponse.ok(certificateService.update(req, certificateId, userId));
    }

    @Operation(summary = "자격증 삭제", description = "자격증을 삭제합니다")
    @DeleteMapping("/{certificateId}")
    public ApiResponse<Void> remove(@PathVariable Long certificateId) {
        Long userId = 1L;
        certificateService.remove(certificateId, userId);
        return ApiResponse.ok(null, "삭제 완료");
    }
}
