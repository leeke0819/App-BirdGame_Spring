package com.example.demo.service;

import com.example.demo.Dto.response.BirdFeedResponseDto;
import com.example.demo.model.BagEntity;
import com.example.demo.model.BirdEntity;
import org.springframework.stereotype.Service;

@Service
public interface BirdService {
    BirdFeedResponseDto birdGiveFood(String itemCode, int amount) throws Exception;
    void deleteUserBagItem(BagEntity bagEntity);
    BirdEntity updateBirdState(String email);
}
