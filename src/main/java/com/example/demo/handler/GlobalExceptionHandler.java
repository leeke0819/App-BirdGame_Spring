package com.example.demo.handler;

import jakarta.persistence.EntityExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.IllegalFormatException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler
    public ResponseEntity<String> handleArithmeticException(ArithmeticException e) {
        return ResponseEntity.status(400).body("Error : 0으로 나눌 수 없습니다.");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleEntityExistException(EntityExistsException e) {
        return ResponseEntity.status(400).body("이미 존재하는 이메일 주소입니다.");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.status(400).body("비밀번호는 8자 이상, 12자 이하로 설정하고 대문자, 소문자, 특수문자를 하나이상 포함해야합니다.");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNumberFormatException(NumberFormatException e){
        return ResponseEntity.status(401).body("사용자가 잘못된 포멧으로 접근했습니다.");
    }
    @ExceptionHandler
    public ResponseEntity<String> NoSuchElementException(NoSuchElementException e){
        return ResponseEntity.status(501).body("서버에 문제가 있습니다.");
    }

}