package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.dto.starredposting.StarredPostingRes;
import com.jobdashboard.backend.service.StarredPostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/me/starred-postings")
@RequiredArgsConstructor
@Tag(name = "즐겨찾기", description = "관심공고 즐겨찾기 API")
// TODO: 로그인 기능 구현 후 현재 로그인한 사용자의 ID로 교체
public class StarredPostingController {

    private final StarredPostingService starredPostingService;

    @Operation(summary = "관심 공고 조회", description = "현재 사용자가 저장한 모든 관심 공고를 최신순으로 조회합니다")
    @GetMapping
    public ApiResponse<List<StarredPostingRes>> getAll() {
        Long userId = 1L;
        return ApiResponse.ok(starredPostingService.getAll(userId));
    }

    @Operation(summary = "관심 공고 등록", description = "채용공고를 현재 사용자의 관심 공고로 등록합니다")
    @PostMapping("/{postingId}")
    public ApiResponse<StarredPostingRes> create(@PathVariable Long postingId) {
        Long userId = 1L;
        return ApiResponse.ok(starredPostingService.create(userId, postingId));
    }

    @Operation(summary = "관심 공고 삭제", description = "현재 사용자의 관심 공고에서 해당 채용공고를 삭제합니다")
    @DeleteMapping("/{postingId}")
    public ApiResponse<Void> remove(@PathVariable Long postingId) {
        Long userId = 1L;
        starredPostingService.remove(userId, postingId);
        return ApiResponse.ok(null, "삭제 완료");
    }
}
