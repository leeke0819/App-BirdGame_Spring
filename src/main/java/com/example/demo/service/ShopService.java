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

    public Page<ItemEntity> getShopPageItems(int page, int size, int category) {

        Pageable pageable = PageRequest.of(page, size);
        if (category == 1){
            return itemRepository.findAllShopItems(pageable);
        } else if ( category == 2) {
            return itemRepository.findAllShopEggs(pageable);
        }
        return itemRepository.findAllShopItems(pageable);
    }


    public boolean buyItem(String itemCode, int amount){
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //현재 로그인 한 사용자 이메일 가져오기
        ItemEntity itemEntity = itemRepository.findByItemCode(itemCode).orElseThrow(); //.orElseThrow를 붙혀서, DB에 없는 상황에 대비
        UserEntity userEntity = userRepository.findByEmail(email); //현재 로그인한 사용자의 UserEntity, ''

        int itemPrice = itemEntity.getPrice(); //아이템의 현재 가격 가져오기
        int userMoney = userEntity.getGold(); //유저의 잔고 조회
        int totalPrice = itemPrice * amount;

        if(userMoney >= totalPrice){
            userEntity.setGold(userMoney-totalPrice);

            // user 가방 안에 있는 아이템 가져오기 (user 가방 가져오기)
            Optional<BagEntity> bagEntity = bagRepository.findByUserEmailAndItemCode(email,itemCode);
            if(bagEntity.isPresent()){ // 가방 안에 아이템이 있으면?
                BagEntity bag = bagEntity.get();
                bag.setAmount(bag.getAmount() + amount);
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

    public boolean sellItem(String itemCode, int amount) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ItemEntity itemEntity = itemRepository.findByItemCode(itemCode).orElseThrow();
        UserEntity userEntity = userRepository.findByEmail(email);
        int itemPrice = itemEntity.getPrice(); //아이템의 현재 가격 가져오기
        int userMoney = userEntity.getGold(); // 유저 잔고 조회

        // user 가방 안에 있는 아이템 가져오기 (user 가방 가져오기)
        Optional<BagEntity> bagEntity = bagRepository.findByUserEmailAndItemCode(email,itemCode);

        // 가방 안에 아이템이 있으면? (아이템 존재 여부)
        if(bagEntity.isPresent()) {
            BagEntity bag = bagEntity.get(); // 가방 안에 있는 아이템들을 가져온 걸 bag에 저장. 즉, bag = 유저 가방

            if(bag.getAmount() >= amount) { // 가방 안에 판매하려는 아이템이 있다면?

                bag.decreaseItemAmount(amount); // 아이템을 판매하고

                // user의 잔고에 (user의 현재 돈 + (아이템 가격 * 아이템 개수)) 만큼의 money 추가
                userEntity.setGold(userMoney + (itemPrice * amount));

                bagRepository.save(bag); // 가방 정보 저장
                userRepository.save(userEntity); // user 정보 저장

            } else {
                // 판매하려는 아이템이 없을 때
                System.out.println("판매하려는 아이템이 부족합니다.");
                bagRepository.save(bag); // 가방 정보 저장
                return false;
            }
        } else {
            // 가방 안에 아이템 자체가 없을 때
            System.out.println("가방 안에 아이템을 보유하고 있지 않아 아이템을 판매할 수 없습니다.");
            return false;
        }

        userEntity.setGold(userMoney + itemPrice);
        userRepository.save(userEntity);
        return true;
    }

    // itementity에서 가져와서 .. db table에 있는 거 전체를 가져오기..
}
