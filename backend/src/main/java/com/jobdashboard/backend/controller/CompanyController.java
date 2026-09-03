package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.dto.company.CompanyEvidenceRes;
import com.jobdashboard.backend.dto.company.CompanyRes;
import com.jobdashboard.backend.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "회사", description = "회사 정보 API")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "회사 정보 조회", description = "회사 기본 정보를 조회합니다 (모달용)")
    @GetMapping("/{companyId}")
    public ApiResponse<CompanyRes> getCompany(@PathVariable Long companyId) {
        return ApiResponse.ok(companyService.getCompany(companyId));
    }
}