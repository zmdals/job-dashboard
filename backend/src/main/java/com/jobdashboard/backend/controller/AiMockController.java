package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.ai.*;
import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.dto.matchanalysis.MatchAnalysisReq;
import com.jobdashboard.backend.dto.matchanalysis.MatchAnalysisRes;
import com.jobdashboard.backend.service.AiMockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "AI Mock", description = "AI Mock API — 나중에 실제 AI 서비스로 교체")
public class AiMockController {

    private final AiMockService aiMockService;

    // 1. 적합도 (비저장, 모달용)
    @Operation(summary = "AI 적합도 분석", description = "유저 스펙 vs 공고 요건 비교 — 비저장, 상세 모달용")
    @PostMapping("/api/postings/{postingId}/ai/relevance")
    public ApiResponse<RelevanceRes> getRelevance(@PathVariable Long postingId) {
        return ApiResponse.ok(aiMockService.getRelevance(postingId));
    }

    // 2. 매칭 분석 생성 (저장형, 덮어쓰기)
    @Operation(summary = "AI 매칭 분석 생성", description = "스펙 기반 분석, 자소서 포함 시 점수 갱신")
    @PostMapping("/api/applications/{applicationId}/ai/analysis")
    public ApiResponse<MatchAnalysisRes> createAnalysis(
            @PathVariable Long applicationId,
            @RequestBody MatchAnalysisReq req) {
        Long userId = 1L;
        return ApiResponse.ok(aiMockService.createAnalysis(applicationId, userId, req));
    }

    // 3. 매칭 분석 조회
    @Operation(summary = "AI 매칭 분석 조회", description = "저장된 매칭 분석 결과 조회")
    @GetMapping("/api/applications/{applicationId}/ai/analysis")
    public ApiResponse<MatchAnalysisRes> getAnalysis(@PathVariable Long applicationId) {
        Long userId = 1L;
        return ApiResponse.ok(aiMockService.getAnalysis(applicationId, userId));
    }

    // 4. 자소서 AI 피드백 (비저장)
    @Operation(summary = "자소서 AI 피드백", description = "자소서 내용을 분석하여 피드백 제공 — 비저장")
    @PostMapping("/api/cover-letters/{coverLetterId}/ai/feedback")
    public ApiResponse<CoverLetterFeedbackRes> getFeedback(@PathVariable Long coverLetterId) {
        return ApiResponse.ok(aiMockService.getFeedback(coverLetterId));
    }

    // 5. 면접 질문 생성 (저장형, 덮어쓰기)
    @Operation(summary = "AI 면접 질문 생성", description = "공고 요건 기반 예상 면접 질문 생성")
    @PostMapping("/api/applications/{applicationId}/ai/interview-questions")
    public ApiResponse<List<InterviewQuestionRes>> createInterviewQuestions(
            @PathVariable Long applicationId) {
        Long userId = 1L;
        return ApiResponse.ok(aiMockService.createInterviewQuestions(applicationId, userId));
    }

    // 6. 면접 질문 조회
    @Operation(summary = "AI 면접 질문 조회", description = "생성된 면접 예상 질문 목록 조회")
    @GetMapping("/api/applications/{applicationId}/ai/interview-questions")
    public ApiResponse<List<InterviewQuestionRes>> getInterviewQuestions(
            @PathVariable Long applicationId) {
        Long userId = 1L;
        return ApiResponse.ok(aiMockService.getInterviewQuestions(applicationId, userId));
    }
}