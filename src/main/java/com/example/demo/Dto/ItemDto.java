package com.example.demo.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private String itemCode;
    private String itemName;
    private String itemDescription;
    private int price;
    private short type;
       private String imageRoot;
}
