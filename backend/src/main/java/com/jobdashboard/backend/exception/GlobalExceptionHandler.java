package com.jobdashboard.backend.exception;

import com.jobdashboard.backend.dto.common.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 엔티티 못 찾음 예외 처리
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ErrorResponse.of(404, "NOT_FOUND", e.getMessage()));
    }

    // INVALID_INPUT 예외
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(400)
                .body(ErrorResponse.of(400, "INVALID_INPUT", e.getMessage()));
    }

    // 충돌 에러 처리
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleConflict(IllegalStateException e) {
        return ResponseEntity.status(409)
                .body(ErrorResponse.of(409, "CONFLICT", e.getMessage()));
    }

    // @Valid로 validation 실패 시 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("입력값이 올바르지 않습니다");
        return ResponseEntity.status(400)
                .body(ErrorResponse.of(400,"VALIDATION_ERROR",message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        return ResponseEntity.status(500)
                .body(ErrorResponse.of(500, "INTERNAL_ERROR", "서버 오류가 발생했습니다"));
    }

}
