package com.example.demo.controller;

import com.example.demo.Dto.response.MyPageResponseDto;
import com.example.demo.Dto.request.UpdateUserInfoRequestDto;
import com.example.demo.Dto.request.UserInfoRequestDto;
import com.example.demo.model.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;



    //Create
    @PostMapping
    public UserEntity signUp(@RequestBody UserInfoRequestDto userInfoRequestDto) {
        System.out.println(userInfoRequestDto.getEmail());
        System.out.println(userInfoRequestDto.getPassword());
        return userService.saveUser(userInfoRequestDto.getEmail(), userInfoRequestDto.getPassword(), userInfoRequestDto.getNickname());
    }

    //Read
    @GetMapping
    public ResponseEntity<MyPageResponseDto> myPage() {
        return ResponseEntity.ok(userService.myPage());
    }

    //Update
    @PutMapping //회원정보 수정 API (예를들어 닉네임 수정등에 용이하게..), 로그인 필요.
    public String updateUserInfo(@RequestBody UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        return userService.updateUserInfo(updateUserInfoRequestDto);
    }

    //Delete, 로그인 필요
    @DeleteMapping // 회원탈퇴 API
    public ResponseEntity<String> deleteUser() {
        if(userService.deleteUser()){
            return ResponseEntity.ok("성공적으로 탈퇴되었습니다.");
        }else{
            return ResponseEntity.status(500).body("서버에 문제가 있습니다.");
        }
    }

    @GetMapping("/gold")
    public ResponseEntity<Map<String, Integer>> getUserGold() {
        Integer gold = userService.getGoldByEmail();
        Map<String, Integer> response = new HashMap<>();
        response.put("gold", gold);
        return ResponseEntity.ok(response);
    }

}