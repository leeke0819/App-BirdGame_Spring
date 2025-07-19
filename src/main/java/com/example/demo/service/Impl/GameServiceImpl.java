package com.example.demo.service.Impl;

import com.example.demo.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    public void startGameTime() {
        Instant now = Instant.now();
        log.info(String.valueOf(now));
    }
}
