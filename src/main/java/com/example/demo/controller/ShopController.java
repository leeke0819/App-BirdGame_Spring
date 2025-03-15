package com.example.demo.controller;

import com.example.demo.Dto.MyPageResponseDto;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.service.ShopService;
import org.apache.catalina.User;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService, UserService userService) {
        this.shopService = shopService;
    }


    @GetMapping
    public List<ItemEntity> shop() {
        return shopService.getItems();
    }

    @GetMapping("/page")
    public Page<ItemEntity> shopPage(@RequestParam String pageNo) {
        try {
            int pageNumber = Integer.parseInt(pageNo);
            return shopService.getShopPageItems(pageNumber, 10);
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
    public ResponseEntity<String> buyItem(@RequestParam String itemCode){
        if(shopService.buyItem(itemCode)){
            return ResponseEntity.ok("구매 성공");
        }
        else{
            return ResponseEntity.badRequest().body("골드가 부족합니다.");
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellItem(@RequestParam String itemCode) {
        if(shopService.sellItem(itemCode)) {
            return ResponseEntity.ok("판매 성공");
        } else {
            return ResponseEntity.badRequest().body("판매할 아이템이 없습니다.");
        }
    }

}
