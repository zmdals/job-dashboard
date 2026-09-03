package com.jobdashboard.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 에러 응답 dto
 *
 * 예시
 * {
 *   "success": false,
 *   "status": 404,
 *   "code": "POSTING_NOT_FOUND",
 *   "message": "해당 공고를 찾을 수 없습니다"
 * }
 */
@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private boolean success;     // 항상 false
    private int status;          // HTTP 상태 코드
    private String code;         // 에러 코드 (프론트가 분기용으로 사용)
    private String message;      // 사람이 읽는 메시지

    public static ErrorResponse of(int status, String code, String message){
        return ErrorResponse.builder()
                .success(false)
                .status(status)
                .code(code)
                .message(message)
                .build();
    }
}
