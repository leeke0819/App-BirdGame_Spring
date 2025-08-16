package com.example.demo.service.Impl;

import com.example.demo.model.BagEntity;
import com.example.demo.model.BookEntity;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CraftService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CraftServiceImpl implements CraftService {

    private final BagRepository bagRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public CraftServiceImpl(BagRepository bagRepository, ItemRepository itemRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.bagRepository = bagRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public void craftItem(String itemCode) {
        log.info(itemCode);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> recipe = getRecipe(itemCode);
        log.info(recipe.toString());
        log.info("Crafting: " + itemCode, " requires: ", recipe);

        // 재료 검증하기
        for (String materialCode : recipe) {
            Optional<BagEntity> bagEntity = bagRepository.findByUserEmailAndItemCode(email, materialCode);
            if (bagEntity.isEmpty() || bagEntity.get().getAmount() <= 0) {
                throw new IllegalArgumentException("재료 부족: " + materialCode);
            }
        }

        // 재료 사용하면 amount에서 -1만큼 줄이기
        for (String materialCode : recipe) {
            BagEntity bag = bagRepository.findByUserEmailAndItemCode(email, materialCode).orElseThrow();
            int updatedAmount = bag.getAmount() - 1;
            if (updatedAmount <= 0) {
                deleteUserBagItem(bag);
            } else {
                bag.setAmount(updatedAmount);
                bagRepository.save(bag);
            }
        }

        // 조합 완료된 아이템 가방에 추가
        ItemEntity craftedItem = itemRepository.findByItemCode(itemCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템 코드입니다: " + itemCode));
        UserEntity user = userRepository.findByEmail(email);

        Optional<BagEntity> existing = bagRepository.findByUserEmailAndItemCode(email, itemCode);
        if (existing.isPresent()) {
            BagEntity bagItem = existing.get();
            bagItem.setAmount(bagItem.getAmount() + 1);
            bagRepository.save(bagItem);
        } else {
            BagEntity newBagItem = new BagEntity();
            newBagItem.setUser(user);
            newBagItem.setItem(craftedItem);
            newBagItem.setAmount(1);
            bagRepository.save(newBagItem);
        }

        // 도감 등록
        boolean alreadyExists =
                bookRepository.existsByUserEntityEmailAndItemEntityItemCode(email, itemCode);
        if (!alreadyExists) {
            BookEntity book = BookEntity.builder()
                    .itemEntity(craftedItem)
                    .userEntity(user)
                    .build();
            bookRepository.save(book);
            log.info("도감 등록 완료: user =" + email + ", itemCode =" + itemCode);
        } else {
            log.info("이미 도감 등록됨: user =" + email + ", itemCode =" + itemCode);
        }

        log.info("아이템 '" + itemCode + "' 조합 완료 및 가방에 추가됨");

        log.info("아이템 '" + itemCode + "' 조합 완료 및 가방에 추가됨");
    }

    @Transactional
    public void deleteUserBagItem(BagEntity bagEntity) {
        bagRepository.delete(bagEntity);
    }

    public List<String> getRecipe(String itemCode) {
        List<String> recipe = new ArrayList<>();
        try{
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/craft.json");
            Map<String,List<String>> recipes = mapper.readValue(inputStream, new TypeReference<Map<String,List<String>>>() {});
            recipe = recipes.get(itemCode);
        } catch (IOException e) {
            log.error("레시피 파일 읽기 실패: ", e);
        }

        return recipe;

    }

}
