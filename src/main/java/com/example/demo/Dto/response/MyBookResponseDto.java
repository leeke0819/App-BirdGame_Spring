package com.example.demo.Dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class MyBookResponseDto {
    private String itemName;
    private String itemDescription;
    private String imageRoot;
}
