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

import com.jobdashboard.backend.dto.career.CareerReq;
import com.jobdashboard.backend.dto.career.CareerRes;
import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.service.CareerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/me/careers")
@RequiredArgsConstructor
@Tag(name = "경력", description = "경력사항 CRUD API")
// TODO: 추후 로그인 기능 구현 시, 현재 로그인한 사용자의 ID를 가져와야 함
public class CareerController {

    private final CareerService careerService;

    @Operation(summary = "전체 경력 조회", description = "현재 사용자의 모든 경력사항을 조회합니다")
    @GetMapping
    public ApiResponse<List<CareerRes>> getAll() {
        Long userId = 1L;
        return ApiResponse.ok(careerService.getAll(userId));
    }

    @Operation(summary = "경력 등록", description = "새 경력사항을 등록합니다")
    @PostMapping
    public ApiResponse<CareerRes> create(@Valid @RequestBody CareerReq req) {
        Long userId = 1L;
        return ApiResponse.ok(careerService.create(req, userId));
    }

    @Operation(summary = "경력 수정", description = "경력사항을 수정합니다")
    @PutMapping("/{careerId}")
    public ApiResponse<CareerRes> update(@PathVariable Long careerId, @Valid @RequestBody CareerReq req) {
        Long userId = 1L;
        return ApiResponse.ok(careerService.update(req, careerId, userId));
    }

    @Operation(summary = "경력 삭제", description = "경력사항을 삭제합니다")
    @DeleteMapping("/{careerId}")
    public ApiResponse<Void> remove(@PathVariable Long careerId) {
        Long userId = 1L;
        careerService.remove(careerId, userId);
        return ApiResponse.ok(null, "삭제 완료");
    }
}