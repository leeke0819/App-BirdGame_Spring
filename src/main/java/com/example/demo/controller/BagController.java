package com.example.demo.controller;

import com.example.demo.Dto.response.InventoryResponseDto;
import com.example.demo.model.BagEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.service.BagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bag")

public class BagController {
    private final BagService bagService;
    private final BagRepository bagRepository;


    public BagController(BagService bagService, BagRepository bagRepository) {
        this.bagService = bagService;
        this.bagRepository = bagRepository;
    }

    //PageNo를 받아서... 가방 페이지별 조회
    @GetMapping("/page")
    public ResponseEntity<List<InventoryResponseDto>> bagPage(@RequestParam String pageNo) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            int pageNumber = Integer.parseInt(pageNo);
            return ResponseEntity.ok(bagService.getInventory(email, pageNumber, 20));
        } catch (NumberFormatException e){
            throw new NumberFormatException("사용자가 잘못된 포멧으로 접근했습니다.");
        }
    }

    //repository 호출 조정 요망
    @GetMapping("/my-bag")
    public List<BagEntity> getMyBag() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return bagRepository.findByUserEmail(email);
    }

}
