package com.example.demo.controller;

import com.example.demo.Dto.LoginRequestDto;
import com.example.demo.Dto.TokenDto;
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
        return loginSuccess;
    }
}
