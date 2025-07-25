package com.example.demo.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class GameSession {
    private int gameId;
    private String userId;
    private Instant startTime;
}
