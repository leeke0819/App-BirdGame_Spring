package com.example.demo.service;

import com.example.demo.Dto.MyPageResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoldService {

    @Autowired
    private UserService userService;

    public int getUserGold() {
        MyPageResponseDto myPageInfo = userService.myPage(); // 현재 로그인한 유저의 정보 가져오기
        return myPageInfo.getMoney(); // DTO에서 money 값 가져오기
    }
}
