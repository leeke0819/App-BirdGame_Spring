package com.example.demo.repository;

import com.example.demo.model.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    Optional<ItemEntity> findByItemName(String itemName);
    Optional<ItemEntity> findByItemCode(String itemCode);

    @Query("SELECT i FROM ItemEntity i WHERE i.isDisplay = TRUE AND i.isEgg = FALSE")
    Page<ItemEntity> findAllItems(Pageable pageable);

    @Query("SELECT i FROM ItemEntity i WHERE i.isDisplay = FALSE AND i.isEgg = TRUE")
    Page<ItemEntity> findAllEggs(Pageable pageable);

    // 아이템이 재료일때..
    @Query("SELECT i FROM ItemEntity i WHERE i.isDisplay = TRUE AND i.isEgg = FALSE")
    List<ItemEntity> findByIsDisplayTrueAndIsEggFalse();

    // 아이템이 알일때..
    @Query("SELECT i FROM ItemEntity i WHERE i.isDisplay = FALSE AND i.isEgg = TRUE")
    List<ItemEntity> findByIsDisplayFalseAndIsEggTrue();
}
