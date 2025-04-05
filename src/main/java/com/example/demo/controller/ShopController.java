package com.example.demo.controller;

import com.example.demo.Dto.MyPageResponseDto;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ShopService;
import org.apache.catalina.User;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {

    final private ShopService shopService;
    final private UserRepository userRepository;

    public ShopController(ShopService shopService, UserRepository userRepository) {
        this.shopService = shopService;
        this.userRepository = userRepository;
    }


    @GetMapping
    public List<ItemEntity> shop() {
        return shopService.getItems();
    }

    @GetMapping("/page")
    public Page<ItemEntity> shopPage(@RequestParam String pageNo, @RequestParam int category) {
        try {
            int pageNumber = Integer.parseInt(pageNo);
            return shopService.getShopPageItems(pageNumber, 10,category);
        }
        catch(NumberFormatException e){
            System.out.println("사용자가 잘못된 포맷으로 접근했습니다.");
        }
        return null;
    }

    @GetMapping("/item")
    public Optional<ItemEntity> getItemName(@PathVariable String itemName) {
        return shopService.getItemName(itemName);
    }

    @GetMapping("/money")
    public List<UserEntity> getMoney(@RequestParam int money) {
        return shopService.getMoney(money);
    }

    @PostMapping("/buy")
    public ResponseEntity<Map<String, Object>> buyItem(@RequestParam String itemCode){
        Map<String, Object> response = new HashMap<>(); // 응답을 담을 맵 생성

        if(shopService.buyItem(itemCode, 1)){
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity = userRepository.findByEmail(email);

            response.put("buy_status", "성공");
            response.put("user_money", userEntity.getMoney());
            //가방에 아이템 넣기.
            return ResponseEntity.ok(response);
        }
        else{
            response.put("error_message", "골드가 부족합니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<Map<String, Object>> sellItem(@RequestParam String itemCode) {
        Map<String, Object> response = new HashMap<>();

        if(shopService.sellItem(itemCode, 1)) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity = userRepository.findByEmail(email);

            response.put("sell_status", "성공");
            response.put("user_money", userEntity.getMoney());
            return ResponseEntity.ok(response);
        } else {
            response.put("error_message", "판매할 아이템이 없습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

}
