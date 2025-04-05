package com.example.demo.service;

import com.example.demo.model.BagEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BirdService {
    // 알 추가하는 로직 ..
    // ShopService의 buyItem처럼..
    final private UserRepository userRepository;
    final private BagRepository bagRepository;

    public BirdService(UserRepository userRepository, BagRepository bagRepository) {
        this.userRepository = userRepository;
        this.bagRepository = bagRepository;
    }

    // 미완성 코드 ..
    public boolean addEgg(String itemCode, String birdCode, int amount) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(email);


        Optional<BagEntity> bagEntity = bagRepository.findByUserEmailAndItemCode(email,itemCode);
        if(bagEntity.isPresent()) {
            BagEntity bag = bagEntity.get();
            bagRepository.save(bag);
        }
        userRepository.save(userEntity);
        return true;
    }
}
