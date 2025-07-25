package com.example.demo.controller;

import com.example.demo.Dto.request.GameOverRequestDto;
import com.example.demo.Dto.request.StartGameRequestDto;
import com.example.demo.Dto.response.GameOverResponseDto;
import com.example.demo.Dto.response.StartGameResponseDto;
import com.example.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/game")
public class GameController {

    @Autowired
    private GameService gameService;


    @PostMapping("/start")
    public ResponseEntity<StartGameResponseDto> gameStart(@RequestBody StartGameRequestDto startGameRequestDto){
        String sessionId = gameService.startGameTime(startGameRequestDto);
        return ResponseEntity.ok(new StartGameResponseDto(sessionId));
    }

    @PostMapping("/over")
    public ResponseEntity<GameOverResponseDto> gameOver(@RequestBody GameOverRequestDto requestDto) {
        System.out.println("📦 sessionId = " + requestDto.getSessionId());
        GameOverResponseDto result = gameService.overGameTime(requestDto.getSessionId());
        return ResponseEntity.ok(result);
    }
}
