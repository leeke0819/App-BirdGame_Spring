package com.example.demo.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoUserMinimalResponseDto {
    private Long kakaoUserId;
    private String email;
    private Boolean emailVerified;
    private String profileImageUrl;
}
