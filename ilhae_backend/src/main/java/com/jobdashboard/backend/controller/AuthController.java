package com.jobdashboard.backend.controller;

import com.jobdashboard.backend.dto.auth.LoginReq;
import com.jobdashboard.backend.dto.auth.SignupReq;
import com.jobdashboard.backend.dto.auth.TokenRes;
import com.jobdashboard.backend.dto.common.ApiResponse;
import com.jobdashboard.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "인증/인가 API", description = "")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "이메일/비밀번호로 회원가입 후 토큰 발급")
    @PostMapping("/signup")
    public ApiResponse<TokenRes> signup(@Valid @RequestBody SignupReq req) {
        return ApiResponse.ok(authService.signup(req));
    }

    @Operation(summary = "로그인", description = "이메일/비밀번호로 로그인 후 토큰 발급")
    @PostMapping("/login")
    public ApiResponse<TokenRes> login(@Valid @RequestBody LoginReq req) {
        return ApiResponse.ok(authService.login(req));
    }
}
