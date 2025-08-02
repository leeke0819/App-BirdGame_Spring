package com.example.demo.Dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long tokenExpiresIn;
    private Long refreshTokenExpiresIn;
}
