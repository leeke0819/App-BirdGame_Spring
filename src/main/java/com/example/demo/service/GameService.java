package com.example.demo.service;

import com.example.demo.Dto.request.StartGameRequestDto;
import com.example.demo.Dto.response.GameOverResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface GameService {
    String startGameTime(StartGameRequestDto dto);
    GameOverResponseDto overGameTime(String sessionId);
}
