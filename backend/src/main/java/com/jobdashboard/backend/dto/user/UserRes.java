package com.jobdashboard.backend.dto.user;

import com.jobdashboard.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserRes {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;

    public static UserRes from(User user) {
        return UserRes.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}