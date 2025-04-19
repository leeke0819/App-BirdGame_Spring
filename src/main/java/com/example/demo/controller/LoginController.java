package com.example.demo.controller;

import com.example.demo.Dto.request.LoginRequestDto;
import com.example.demo.Dto.response.TokenDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginRequestDto loginRequestDto) {
        TokenDto loginSuccess = userService.loginUser(loginRequestDto);
        String password = loginRequestDto.getPassword();
        if((password.length() < 8 || password.length() > 12) || !password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 ~ 12자 이하로 설정하고, 대문자,소문자,특수문자를 하나이상 포함해야합니다.");
        }
        return loginSuccess;
    }
}
