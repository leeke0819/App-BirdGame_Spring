package com.example.demo.service;

import com.example.demo.Dto.response.InventoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BagService {
    List<InventoryResponseDto> getInventory(String email, int page, int size);
}
