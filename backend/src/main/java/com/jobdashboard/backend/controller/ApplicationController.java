package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.application.ApplicationRes;
import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    /*
    // GET /api/me/applications - 내 지원현황 전체 조회 - 메인페이지
    @GetMapping("/api/me/applications")
    public ApiResponse<List<ApplicationRes>> getMyApplications(){

    }

    // POST /api/postings/{postingId}/applications → PREPARING(기본값)으로 생성
    @PostMapping("/api/postings/{postingId}/applications")
    public ApiResponse<ApplicationRes> create(@PathVariable Long postingId){
        //applicationService.create(postingId);
    }
    //상태 하나만 바꾸는 건 리소스의 일부 필드만 건드리는 거니까 PATCH
    //@PatchMapping("/api/")*/
}
