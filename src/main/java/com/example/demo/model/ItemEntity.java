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

    private String itemDescription;

    private int price;

    private short type;

    private String imageRoot;

}
