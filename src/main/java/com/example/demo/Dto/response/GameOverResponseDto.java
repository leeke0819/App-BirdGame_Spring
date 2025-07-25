package com.example.demo.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameOverResponseDto {
    private int reward;
    private long duration;
    private String userId;
}
