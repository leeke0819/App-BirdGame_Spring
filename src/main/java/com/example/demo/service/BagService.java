package com.example.demo.service;

import com.example.demo.model.ItemEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BagService {

    final private ItemRepository itemRepository;
    final private UserRepository userRepository;

    public BagService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }


    public Page<ItemEntity> getBagPageItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAll(pageable);
    }

    public List<UserEntity> getMoney(int money) {
        return userRepository.findByMoney(money);
    }
}
