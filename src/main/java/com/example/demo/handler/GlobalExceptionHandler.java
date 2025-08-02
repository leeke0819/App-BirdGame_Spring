package com.example.demo.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler{



    @ExceptionHandler
    public ResponseEntity<String> ArithmeticException(ArithmeticException e) {
        return ResponseEntity.status(400).body("Error : 0으로 나눌 수 없습니다.");
    }

    @ExceptionHandler
    public ResponseEntity<String> EntityExistException(EntityExistsException e) {
        return ResponseEntity.status(400).body("이미 존재하는 이메일 주소입니다.");
    }

    @ExceptionHandler
    public ResponseEntity<String> IllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> NumberFormatException(NumberFormatException e){
        return ResponseEntity.status(401).body("사용자가 잘못된 포멧으로 접근했습니다.");
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException e) {
        return ResponseEntity.status(401).body("잘못된 JWT 서명입니다.");
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<String> handleUnsupportedJwtException(UnsupportedJwtException e) {
        return ResponseEntity.status(401).body("지원되지 않는 JWT 토큰입니다.");
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> handleExpiredJwtException(ExpiredJwtException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 401);
        body.put("error", "ExpiredJwtException");
        body.put("message", "만료된 토큰입니다.");
        body.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(401).body(body);
    }
    
    @ExceptionHandler
    public ResponseEntity<String> EntityNotFoundException(EntityNotFoundException e){
        return ResponseEntity.status(404).body(String.valueOf(e));
    }
    @ExceptionHandler
    public ResponseEntity<String> NoSuchElementException(NoSuchElementException e){
        return ResponseEntity.status(501).body("서버에 문제가 있습니다.");
    }

    //TODO:: RunTimeException 예외처리 해주기.
    @ExceptionHandler
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(500).body(String.valueOf(e));
    }


}