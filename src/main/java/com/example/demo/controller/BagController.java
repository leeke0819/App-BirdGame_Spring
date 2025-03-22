package com.example.demo.controller;

import com.example.demo.model.ItemEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.service.BagService;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bag")

public class BagController {
    private final BagService bagService;

    public BagController(BagService bagService) {
        this.bagService = bagService;
    }

    @GetMapping("/page")
    public Page<ItemEntity> bagPage(@RequestParam String pageNo) {
        try {
            int pageNumber = Integer.parseInt(pageNo);
            return bagService.getBagPageItems(pageNumber, 10);
        } catch (NumberFormatException e){
            System.out.println("사용자가 잘못된 포맷으로 접근했습니다.");
        }
        return null;
    }


}
