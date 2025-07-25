package com.example.demo.service.Impl;

import com.example.demo.Dto.request.StartGameRequestDto;
import com.example.demo.Dto.response.GameOverResponseDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.GameService;
import com.example.demo.service.GameRewardPolicy;
import com.example.demo.session.GameSession;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    private static final Map<String, GameSession> sessionMap = new HashMap<>(); // 세션 정보 저장 hashMap 생성
    private final UserRepository userRepository;

    public GameServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public String startGameTime(StartGameRequestDto startGameRequestDto) {
        log.info("🚀 startGameTime() 호출됨!");
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Instant startTime = Instant.now();
        String sessionId = UUID.randomUUID().toString(); // uid 안곂치게 고유의 uid 생성

        // 게임ID, 유저ID, 게임 시작 시간을 모두 포함한 세션 생성
        GameSession session = new GameSession(startGameRequestDto.getGameId(), userId, startTime);
        sessionMap.put(sessionId, session); // 아까 위에서 만든 hashMap에 세션ID와 세션을 저장
        log.info("✅ 세션 저장됨: " + sessionId);

        log.info("게임 시작 시간 : " + String.valueOf(startTime));
        log.info("게임이 시작되었습니다. " +
                "userId = " + userId +
                ", sessionId = " + sessionId +
                ", gameId = "+ startGameRequestDto.getGameId());
        return sessionId;
    }

    @Override
    @Transactional
    public GameOverResponseDto overGameTime(String sessionId) {
        GameSession session = sessionMap.get(sessionId); // hashMap에 저장된 세션ID 가져오기

        if (!sessionMap.containsKey(sessionId)) {
            System.out.println("❌ 존재하지 않는 세션: " + sessionId);
            throw new IllegalArgumentException("유효하지 않은 sessionId입니다.");
        }

        Instant endTime = Instant.now();

        // 게임 실행 시간 계산
        long secondsPlayed = Duration.between(session.getStartTime(), endTime).getSeconds();

        // 플레이 시간에 따른 보상 계산
        int reward = GameRewardPolicy.fromGameId(session.getGameId()).calculateReward(secondsPlayed);

        // 보상 지급
        userRepository.addGoldToUser(session.getUserId(), reward);

        log.info("게임 종료 시간 : " + String.valueOf(endTime));
        log.info("게임이 종료되었습니다. " +
                "userId = " + session.getUserId() +
                ", duration = " + secondsPlayed +
                ", reward = " + reward);

        sessionMap.remove(sessionId);
        return new GameOverResponseDto(reward, secondsPlayed, session.getUserId());
    }
}
