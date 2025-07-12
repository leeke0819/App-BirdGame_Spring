package com.example.demo.service;

import com.example.demo.model.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ShopService {
    List<ItemEntity> getItems();
    Optional<ItemEntity> getItemName(String itemName);
    Page<ItemEntity> getShopPageItems(int page, int size, int category);
    boolean buyItem(String itemCode, int amount);
    boolean sellItem(String itemCode, int amount);
}
