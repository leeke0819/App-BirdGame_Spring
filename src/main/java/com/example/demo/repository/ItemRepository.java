package com.example.demo.repository;

import com.example.demo.model.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    Optional<ItemEntity> findByItemName(String itemName);
    Optional<ItemEntity> findByItemCode(String itemCode);
}
