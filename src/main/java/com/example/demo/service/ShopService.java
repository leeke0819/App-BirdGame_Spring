package com.example.demo.service;

import com.example.demo.Dto.MyPageResponseDto;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    final private ItemRepository itemRepository;
    final private UserRepository userRepository;

    public ShopService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }


    public List<ItemEntity> getItems() {
        List<ItemEntity> itemEntities = itemRepository.findAll();
        return itemEntities;
    }

    public Optional<ItemEntity> getItemName(String itemName) {
        return itemRepository.findByItemName(itemName);
    }

    public Page<ItemEntity> getShopPageItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAll(pageable);
    }

    public List<UserEntity> getMoney(int money) {
        return userRepository.findByMoney(money);
    }

    public boolean buyItem(String itemCode){
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //현재 로그인 한 사용자 이메일 가져오기
        ItemEntity itemEntity = itemRepository.findByItemCode(itemCode).orElseThrow(); //.orElseThrow를 붙혀서, DB에 없는 상황에 대비
        UserEntity userEntity = userRepository.findByEmail(email); //현재 로그인한 사용자의 UserEntity
        int itemPrice = itemEntity.getPrice(); //아이템의 현재 가격 가져오기
        int userMoney = userEntity.getMoney(); //유저의 잔고 조회
        if(userMoney>=itemPrice){
            userEntity.setMoney(userMoney-itemPrice);
            //이후 가방에 넣어주기
            userRepository.save(userEntity);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean sellItem(String itemCode) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ItemEntity itemEntity = itemRepository.findByItemCode(itemCode).orElseThrow();
        UserEntity userEntity = userRepository.findByEmail(email);
        int itemPrice = itemEntity.getPrice();
        int userMoney = userEntity.getMoney();

        userEntity.setMoney(userMoney+itemPrice);
        userRepository.save(userEntity);
        return true;
    }

    // itementity에서 가져와서 .. db table에 있는 거 전체를 가져오기..
}
