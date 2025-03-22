package com.example.demo.service;

import com.example.demo.model.BagEntity;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    final private ItemRepository itemRepository;
    final private UserRepository userRepository;
    final private BagRepository bagRepository;

    public ShopService(ItemRepository itemRepository, UserRepository userRepository, BagRepository bagRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bagRepository = bagRepository;
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

    public boolean buyItem(String itemCode, int amount){
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //현재 로그인 한 사용자 이메일 가져오기
        ItemEntity itemEntity = itemRepository.findByItemCode(itemCode).orElseThrow(); //.orElseThrow를 붙혀서, DB에 없는 상황에 대비
        UserEntity userEntity = userRepository.findByEmail(email); //현재 로그인한 사용자의 UserEntity, ''

        int itemPrice = itemEntity.getPrice(); //아이템의 현재 가격 가져오기
        int userMoney = userEntity.getMoney(); //유저의 잔고 조회
        int totalPrice = itemPrice * amount;

        if(userMoney >= totalPrice){
            userEntity.setMoney(userMoney-totalPrice);

            // user 가방 안에 있는 아이템 가져오기 (user 가방 가져오기)
            Optional<BagEntity> bagEntity = bagRepository.findByUserEmailAndItemCode(email,itemCode);
            if(bagEntity.isPresent()){
                BagEntity bag = bagEntity.get();
                bag.setAmount(bag.getAmount()+amount);
                bagRepository.save(bag);
            }else{
                BagEntity bag = new BagEntity();
                bag.setAmount(amount);
                bag.setUser(userEntity);
                bag.setItem(itemEntity);
                bagRepository.save(bag);
            }
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
