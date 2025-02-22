package com.example.demo.service;

import com.example.demo.model.ItemEntity;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {
    @Autowired
    private ItemRepository itemRepository;
    public List<ItemEntity> getItems() {
        List<ItemEntity> itemEntities = itemRepository.findAll();
        return itemEntities;
    }

    public Page<ItemEntity> getPageItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAll(pageable);
    }

    // itementity에서 가져와서 .. db table에 있는 거 전체를 가져오기..
}
