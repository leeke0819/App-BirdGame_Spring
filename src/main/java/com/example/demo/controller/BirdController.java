package com.example.demo.controller;

import com.example.demo.Dto.request.BirdFeedRequestDto;
import com.example.demo.Dto.response.BirdFeedResponseDto;
import com.example.demo.Dto.response.MyPageResponseDto;
import com.example.demo.model.BirdEntity;
import com.example.demo.service.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bird")
public class BirdController {

    @Autowired
    private BirdService birdService;

//    @PostMapping("/hatch")
//    public birdHatch(@RequestBody )

    @PostMapping("/feed")
    public ResponseEntity<BirdFeedResponseDto> birdFeed(@RequestBody BirdFeedRequestDto birdFeedRequestDto) throws Exception {
        String itemCode = birdFeedRequestDto.getItemCode();
        int amount = birdFeedRequestDto.getAmount();

        return ResponseEntity.ok(birdService.birdGiveFood(itemCode, amount));
    }

    @GetMapping("/state")
    public ResponseEntity<BirdEntity> getMyBirdState() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        BirdEntity updatedBird = birdService.updateBirdState(email);
        return ResponseEntity.ok(updatedBird);
    }
}
