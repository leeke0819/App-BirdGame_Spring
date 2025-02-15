package com.example.demo.controller;

import com.example.demo.service.GoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gold")
public class GoldController {

    @Autowired
    private GoldService goldService;

    @GetMapping
    public ResponseEntity<Integer> getGold() {
        int gold = goldService.getUserGold();
        return ResponseEntity.ok(gold);
    }
}