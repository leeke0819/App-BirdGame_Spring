package com.example.demo.controller;

import com.example.demo.Dto.request.BirdFeedRequestDto;
import com.example.demo.Dto.response.MyPageResponseDto;
import com.example.demo.model.BirdEntity;
import com.example.demo.service.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bird")
public class BirdController {

    @Autowired
    private BirdService birdService;

    @PostMapping("/feed")
    public MyPageResponseDto birdFeed(@RequestBody BirdFeedRequestDto birdFeedRequestDto) {

    }
}
