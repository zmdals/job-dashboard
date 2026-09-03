package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.dto.jobposting.JobPostingReq;
import com.jobdashboard.backend.dto.jobposting.JobPostingRes;
import com.jobdashboard.backend.service.JobPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postings")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @GetMapping
    // GET /api/postings → 전체 조회 → ApiResponse.ok(list) - 페이징 처리, sorting 구현?
    public ApiResponse<List<JobPostingRes>> getAll() {
        return ApiResponse.ok(jobPostingService.getAllPostings());
    }

    @GetMapping("/{postingId}")
    // GET /api/postings/{postingId} → 상세 조회 → ApiResponse.ok(detail)
    public ApiResponse<JobPostingRes> get(@PathVariable Long postingId){
        return ApiResponse.ok(jobPostingService.getPosting(postingId));
    }

    @PostMapping
    // POST /api/postings → 생성 → ApiResponse.ok(detail-생성된 공고 반환)
    public ApiResponse<JobPostingRes> create(@Valid @RequestBody JobPostingReq req){
        return ApiResponse.ok(jobPostingService.create(req));
    }

    @PutMapping("/{postingId}")
    // Put /api/postings/{postingId} -> 들어온 정보로 수정
    public ApiResponse<JobPostingRes> update(@Valid @RequestBody JobPostingReq req,
                                             @PathVariable Long postingId){
        return ApiResponse.ok(jobPostingService.update(req, postingId));
    }

    @DeleteMapping("/{postingId}")
    // DELETE /api/postings/{postingId} → 삭제 → ApiResponse.ok(null, "삭제 완료")
    public ApiResponse<Void> remove(@PathVariable Long postingId){
        jobPostingService.remove(postingId);
        return ApiResponse.ok(null,"삭제 완료");
    }



}
