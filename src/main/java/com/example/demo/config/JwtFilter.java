package com.example.demo.config;

import com.example.demo.service.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        String jwt = resolveToken(request);


        //TODO:: 헤더에 RefreshToken 이 있는 경우... refreshToken을 추출해서 validate Token에 true와 함께 전달.
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt, false)) { //토큰의 유효성 체크
            Authentication authentication = tokenProvider.getAuthentication(jwt); //토큰에서 authenticatino 객체 추출
            SecurityContextHolder.getContext().setAuthentication(authentication); //팔찌채우기: Authentication 객체를 Security Context holder에 등록
        }

        filterChain.doFilter(request, response);
    }
}
