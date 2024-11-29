package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.api.common.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 自訂異常處理
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getCode())
                .body(ApiResponse.fail(ex.getCode(), ex.getMessage(), ex.toString()));
    }

    // 全局異常處理
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", ex.toString()));
    }
}