package com.example.demo.service;

import com.example.demo.Dto.request.LoginRequestDto;
import com.example.demo.Dto.request.UpdateUserInfoRequestDto;
import com.example.demo.Dto.response.*;
import com.example.demo.model.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserEntity saveUser(String email, String password, String nickname);
    TokenDto loginUser(LoginRequestDto loginRequestDto);
    MyPageResponseDto myPage();
    boolean deleteUser();
    String updateUserInfo(UpdateUserInfoRequestDto updateUserInfoRequestDto);
    Integer getGoldByEmail();
    String getNicknameByEmail();
    TokenDto kakaoLogin(String authCode);
    KakaoMeDto.KakaoMe getKakaoUserInfo(String accessToken);
    KakaoUserMinimalResponseDto fetchKakaoMinimal(String accessToken);
}
