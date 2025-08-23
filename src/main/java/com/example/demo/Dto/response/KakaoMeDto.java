package com.example.demo.Dto.response;

public class KakaoMeDto {
    public record KakaoProfile(
            String profile_image_url,
            Boolean is_default_image,
            String nickname
    ) {}

    public record KakaoAccount(
            Boolean has_email,
            Boolean is_email_valid,
            Boolean is_email_verified,
            String email,
            KakaoProfile profile

    ) {}

    public record KakaoMe(
            Long id,
            KakaoAccount kakao_account
    ) {}
}
