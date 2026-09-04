package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.dto.user.UserRes;
import com.jobdashboard.backend.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저", description = "내 정보 API")
public class UserController {

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 유저 정보 반환")
    @GetMapping("/api/me")
    public ApiResponse<UserRes> getMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(UserRes.from(userDetails.getUser()));
    }
}