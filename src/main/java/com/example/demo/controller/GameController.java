package com.example.demo.controller;

import com.example.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/game")
public class GameController {

    @Autowired
    private GameService gameService;


    @PostMapping("/start")
    public void gameStart(){
        gameService.startGameTime();
    }

    /*
    @PostMapping("/end")
    public void gameEnd(){

    }
    */

}
