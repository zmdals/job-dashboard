package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.dto.jobposting.JobPostingReq;
import com.jobdashboard.backend.dto.jobposting.JobPostingRes;
import com.jobdashboard.backend.service.JobPostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postings")
@RequiredArgsConstructor
@Tag(name = "채용공고", description = "채용공고 CRUD API")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @Operation(summary = "전체 공고 조회", description = "등록된 모든 채용공고를 조회합니다")
    @GetMapping
    public ApiResponse<List<JobPostingRes>> getAll() {
        return ApiResponse.ok(jobPostingService.getAllPostings());
    }

    @Operation(summary = "공고 상세 조회", description = "postingId로 채용공고 상세 정보를 조회합니다")
    @GetMapping("/{postingId}")
    public ApiResponse<JobPostingRes> get(@PathVariable Long postingId) {
        return ApiResponse.ok(jobPostingService.getPosting(postingId));
    }

    @Operation(summary = "공고 등록", description = "새 채용공고를 등록합니다 (company, title 필수)")
    @PostMapping
    public ApiResponse<JobPostingRes> create(@Valid @RequestBody JobPostingReq req) {
        return ApiResponse.ok(jobPostingService.create(req));
    }

    @Operation(summary = "공고 수정", description = "기존 채용공고 정보를 수정합니다")
    @PutMapping("/{postingId}")
    public ApiResponse<JobPostingRes> update(@Valid @RequestBody JobPostingReq req,
                                             @PathVariable Long postingId) {
        return ApiResponse.ok(jobPostingService.update(req, postingId));
    }

    @Operation(summary = "공고 삭제", description = "채용공고를 삭제합니다")
    @DeleteMapping("/{postingId}")
    public ApiResponse<Void> remove(@PathVariable Long postingId) {
        jobPostingService.remove(postingId);
        return ApiResponse.ok(null, "삭제 완료");
    }
}
