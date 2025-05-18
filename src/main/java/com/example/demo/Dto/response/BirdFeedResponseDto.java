package com.example.demo.Dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class BirdFeedResponseDto {
    private String birdName;
    private Integer birdLevel;
    private Integer birdHungry;
    private Integer birdThirst;
    private String status;
}
