package com.jobdashboard.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 성공 응답 dto
 *
 * // 예시
 * {
 *   "success": true,
 *   "data": { "id": 1, "companyName": "삼성" },
 *   "message": null
 * }
 */

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;

    public static <T> ApiResponse<T> ok(T data){
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> ok(T data, String message){
        return new ApiResponse<>(true, data, message);
    }
}

