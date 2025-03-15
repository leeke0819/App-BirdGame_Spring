package com.example.demo.controller;

import com.example.demo.Dto.MyPageResponseDto;
import com.example.demo.Dto.SignUpRequestDto;
import com.example.demo.model.UserEntity;
import com.example.demo.service.BagService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    private final BagService bagService;

    public UserController(BagService bagService) {
        this.bagService = bagService;
    }

    @PostMapping
    public UserEntity signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        System.out.println(signUpRequestDto.getEmail());
        System.out.println(signUpRequestDto.getPassword());
        return userService.saveUser(signUpRequestDto.getEmail(),signUpRequestDto.getPassword(),signUpRequestDto.getNickname());
    }

    @GetMapping
    public ResponseEntity<MyPageResponseDto> myPage() {
        return ResponseEntity.ok(userService.myPage());
    }

    @GetMapping("/money")
    public List<UserEntity> getMoney(@RequestParam int money) {
        return bagService.getMoney(money);
    }

}