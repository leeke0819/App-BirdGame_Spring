package com.example.demo.service.Impl;

import com.example.demo.Dto.request.InventoryResponseDto;
import com.example.demo.model.BagEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.service.BagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BagServiceImpl implements BagService {


    private final BagRepository bagRepository;

    public BagServiceImpl(BagRepository bagRepository) {
        this.bagRepository = bagRepository;
    }

    public List<InventoryResponseDto> getInventory(String email, int page, int size) {
        List<BagEntity> bagEntities = bagRepository.findByUserEmail(email);
        List<InventoryResponseDto> inventoryResponseDtos = new ArrayList<>();
        for(int i=0;i<bagEntities.size();i++){
            InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
            inventoryResponseDto.setItemEntity(bagEntities.get(i).getItem());
            inventoryResponseDto.setAmount(bagEntities.get(i).getAmount());
            inventoryResponseDtos.add(inventoryResponseDto);
        }
        return inventoryResponseDtos;
    }

}
