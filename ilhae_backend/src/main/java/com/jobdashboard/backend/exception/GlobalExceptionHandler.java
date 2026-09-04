package com.jobdashboard.backend.exception;

import com.jobdashboard.backend.dto.common.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404 — 리소스를 찾을 수 없을 때
     * ex) findById 결과가 없을 때, 존재하지 않는 공고/지원내역/유저 조회
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ErrorResponse.of(404, "NOT_FOUND", e.getMessage()));
    }

    /**
     * 400 — 요청 파라미터/데이터가 논리적으로 잘못됐을 때
     * ex) 음수 ID 전달, 잘못된 날짜 범위, 존재하지 않는 enum 값 파싱 실패
     * 주의: "리소스가 없다"는 여기가 아니라 ResourceNotFoundException으로
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(400)
                .body(ErrorResponse.of(400, "INVALID_INPUT", e.getMessage()));
    }

    /**
     * 409 — 비즈니스 규칙 위반으로 요청을 처리할 수 없을 때
     * ex) 이미 해당 공고에 지원 이력 존재 (중복 지원),
     *     허용되지 않는 상태 전이 (PREPARING → ACCEPTED 직행 시도)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleConflict(IllegalStateException e) {
        return ResponseEntity.status(409)
                .body(ErrorResponse.of(409, "CONFLICT", e.getMessage()));
    }

    /**
     * 400 — @Valid 검증 실패 시 자동 발생
     * ex) @NotNull 필드에 null 전달, @NotBlank 필드에 빈 문자열 전달
     *     직접 throw하는 게 아니라 Spring이 자동으로 던짐
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("입력값이 올바르지 않습니다");
        return ResponseEntity.status(400)
                .body(ErrorResponse.of(400, "VALIDATION_ERROR", message));
    }

    /**
     * 500 — 위에서 안 잡힌 모든 예외 (최후의 안전망)
     * ex) NullPointerException, DB 연결 실패 등 예상 못한 서버 에러
     *     실제 에러 메시지는 클라이언트에 노출하지 않음
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        return ResponseEntity.status(500)
                .body(ErrorResponse.of(500, "INTERNAL_ERROR", "서버 오류가 발생했습니다"));
    }
}
