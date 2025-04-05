package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BirdTypeEntity {

    @Id
    private String birdCode;

    private String type;

    private String description;

    private int price;
}
