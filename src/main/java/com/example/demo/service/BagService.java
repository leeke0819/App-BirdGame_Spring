package com.example.demo.service;

import com.example.demo.Dto.InventoryResponseDto;
import com.example.demo.model.BagEntity;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BagService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BagRepository bagRepository;

    public BagService(ItemRepository itemRepository, UserRepository userRepository, BagRepository bagRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bagRepository = bagRepository;
    }


    public List<InventoryResponseDto> getInventory(String email, int page, int size) {
        List<BagEntity> bagEntities = bagRepository.findByUserEmail(email);
        List<InventoryResponseDto> inventoryResponseDtos = new ArrayList<>();
        for(int i=0;i<bagEntities.size();i++){
            InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
            inventoryResponseDto.setItemEntity(
                    bagEntities.get(i).getItem());
            inventoryResponseDto.setAmount(
                    bagEntities.get(i).getAmount());
            inventoryResponseDtos.add(inventoryResponseDto);
        }
        return inventoryResponseDtos;
    }

    public List<UserEntity> getMoney(int money) {
        return userRepository.findByMoney(money);
    }
}
