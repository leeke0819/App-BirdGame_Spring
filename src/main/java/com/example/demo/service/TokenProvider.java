package com.example.demo.service;

import com.example.demo.Dto.response.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 360000000;    //1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 604800000; // 7일
    private final Key accessKey;
    private final Key refreshKey;

    public TokenProvider(@Value("${access.jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.accessKey = Keys.hmacShaKeyFor(keyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date tokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);


        String subject = authentication.getName();
        System.out.println(authorities);
        String accessToken = Jwts.builder()
                .setSubject(subject) //1. subject(email)식별자, 권한(Lore)
                .claim(AUTHORITIES_KEY, authorities) //2. authorities(권한(유저/어드민/게스트))
                .setExpiration(tokenExpiresIn) //3. 만료 시간
                .signWith(accessKey, SignatureAlgorithm.HS512) //4. 암호화 알고리즘
                .compact(); //암호화

        String refreshToken = Jwts.builder()
                .setSubject(subject)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(refreshKey, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenExpiresIn(tokenExpiresIn.getTime())
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())//UNIX Time반환
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken, false);
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token, boolean isRefreshToken) {
        System.out.println("전달된 JWT 토큰: " + token);
        Key key = isRefreshToken ? refreshKey : accessKey;
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
            throw new SecurityException("잘못된 JWT 서명입니다.");
            //예외던지기
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰");
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰");
            throw new UnsupportedJwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못됨");
            throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
        }
        //TODO: 토큰 상태에 따른 GlobalException Handler와의 연동 필요
    }

    private Claims parseClaims(String accessToken, boolean isRefreshToken) {
        Key key = isRefreshToken ? refreshKey : accessKey;
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
