package com.example.demo.controller;

import com.example.demo.Dto.InventoryResponseDto;
import com.example.demo.model.BagEntity;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.service.BagService;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.hibernate.mapping.Bag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bag")

public class BagController {
    private final BagService bagService;
    private final BagRepository bagRepository;


    public BagController(BagService bagService, BagRepository bagRepository) {
        this.bagService = bagService;
        this.bagRepository = bagRepository;
    }

    @GetMapping("/page")
    public ResponseEntity<List<InventoryResponseDto>> bagPage(@RequestParam String pageNo) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            int pageNumber = Integer.parseInt(pageNo);
            return ResponseEntity.ok(bagService.getInventory(email,pageNumber, 10));
        } catch (NumberFormatException e){
            throw new NumberFormatException("사용자가 잘못된 포멧으로 접근했습니다.");
        }
    }

    @GetMapping("/my-bag")
    public List<BagEntity> getMyBag() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return bagRepository.findByUserEmail(email);
    }

}
