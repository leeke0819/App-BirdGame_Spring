package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemEntity {

    @Id
    @Column(unique = true)
    private String itemCode;

    private String itemName;

    private int itemType; //목마름을 채우는 Type, 배고픔을 채우는 Type, 혹은 먹일수 없는 Type

    private short status;

    private int feed; //얼마만큼 배고픔(혹은 목마름)을 채울것인가.

    private int  thirst;

    private String itemDescription;

    private int price;

    private String imageRoot;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isEgg;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isDisplay;

}
