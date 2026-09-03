package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.application.ApplicationRes;
import com.jobdashboard.backend.dto.application.StatusUpdateReq;
import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 //인증 추가하면 userId 하드코딩 부분 수정
 *
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Application", description = "지원 관리 API")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "내 지원현황 전체 조회", description = "현재 사용자의 모든 지원 내역을 최신순으로 반환")
    @GetMapping("/api/me/applications")
    public ApiResponse<List<ApplicationRes>> getAll(){
        Long userId = 1L;
        return ApiResponse.ok(applicationService.getAll(userId));
    }

    @Operation(summary = "지원 내역 생성", description = "해당 공고에 PREPARING(기본값) 상태로 지원 내역 생성")
    @PostMapping("/api/postings/{postingId}/applications")
    public ApiResponse<ApplicationRes> create(@PathVariable Long postingId){
        Long userId = 1L;
        return ApiResponse.ok(applicationService.create(userId,postingId));
    }

    //상태 하나만 바꾸는 건 리소스의 일부 필드만 건드리는 거니까 PATCH
    @Operation(summary = "지원 상태 변경", description = "PREPARING→APPLIED→IN_PROGRESS→ACCEPTED/REJECTED 순서로 상태 변경")
    @PatchMapping("/api/applications/{applicationId}/status")
    public ApiResponse<ApplicationRes> updateStatus(@PathVariable Long applicationId,
                                              @Valid @RequestBody StatusUpdateReq req){
        Long userId = 1L;
        return ApiResponse.ok(applicationService.updateStatus(applicationId,userId, req));
    }

    // 지원내역 삭제
    @Operation(summary = "지원 내역 삭제", description = "해당 지원 내역을 삭제")
    @DeleteMapping("/api/applications/{applicationId}")
    public ApiResponse<Void> remove(@PathVariable Long applicationId){
        Long userId = 1L;
        applicationService.remove(applicationId, userId);
        return ApiResponse.ok(null,"삭제 완료");
    }

}
