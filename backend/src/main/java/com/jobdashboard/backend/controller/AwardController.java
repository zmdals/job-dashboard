package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.award.AwardReq;
import com.jobdashboard.backend.dto.award.AwardRes;
import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.service.AwardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me/awards")
@Tag(name = "수상 경력", description = "수상 경력 CRUD API")
public class AwardController {

    private final AwardService awardService;

    @GetMapping
    @Operation(summary = "수상 경력 전체 조회")
    public ApiResponse<List<AwardRes>> getAll() {
        Long userId = 1L;
        return ApiResponse.ok(awardService.getAll(userId));
    }

    @PostMapping
    @Operation(summary = "수상 경력 등록")
    public ApiResponse<AwardRes> create(
            @Valid @RequestBody AwardReq req) {
        Long userId = 1L;
        return ApiResponse.ok(awardService.create(req, userId));
    }

    @PutMapping("/{awardId}")
    @Operation(summary = "수상 경력 수정")
    public ApiResponse<AwardRes> update(
            @PathVariable Long awardId,
            @Valid @RequestBody AwardReq req) {
        Long userId = 1L;
        return ApiResponse.ok(
                awardService.update(req, awardId, userId));
    }

    @DeleteMapping("/{awardId}")
    @Operation(summary = "수상 경력 삭제")
    public ApiResponse<Void> remove(
            @PathVariable Long awardId) {
        Long userId = 1L;
        awardService.remove(awardId, userId);
        return ApiResponse.ok(null, "삭제 완료");
    }
}