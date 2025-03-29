package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BirdTypeEntity {

    @Id
    private String birdCode;
    private String type;
    private String description;

    // item db에 직접 넣는것처럼 넣기 + item_entity에 image_root처럼 사진 삽입도 ..

}
