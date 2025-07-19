package com.example.demo.Dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyBookResponseDto {
    private String itemName;
    private String itemDescription;
    private String imageRoot;
    private boolean isEgg;
    private boolean isDisplay;

    @JsonProperty("obtained")
    private boolean isObtained;
}
