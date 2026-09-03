package com.jobdashboard.backend.service;

import com.jobdashboard.backend.dto.auth.LoginReq;
import com.jobdashboard.backend.dto.auth.SignupReq;
import com.jobdashboard.backend.dto.auth.TokenRes;
import com.jobdashboard.backend.entity.User;
import com.jobdashboard.backend.repository.UserRepository;
import com.jobdashboard.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Transactional
    public TokenRes signup(SignupReq req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .phoneNumber(req.getPhoneNumber())
                .build();

        User saved = userRepository.save(user);
        String token = jwtTokenProvider.createToken(saved.getId(), saved.getEmail());
        return TokenRes.of(token, saved.getId(), saved.getName());
    }

    // 로그인
    public TokenRes login(LoginReq req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getId(), user.getEmail());
        return TokenRes.of(token, user.getId(), user.getName());
    }
}