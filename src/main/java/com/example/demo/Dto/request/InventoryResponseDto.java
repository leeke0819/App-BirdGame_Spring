package com.example.demo.Dto.request;


import com.example.demo.model.ItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class InventoryResponseDto {
    int amount;
    ItemEntity itemEntity;
}
