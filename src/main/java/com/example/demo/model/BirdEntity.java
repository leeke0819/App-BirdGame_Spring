package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BirdEntity {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private int age;
    private int health;
    private int thirst;
    private int exp;
    private int hungry;
    private int level;
    private short status; //1 부화전 2부화 함 3 레벨업 몇이상...
    private String imageRoot;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private BirdTypeEntity birdTypeEntity;

    public BirdEntity(String name){
        this.name = name;
        this.age = 1;
        this.health = 20;
        this.thirst = 10;
        this.exp = 0;
        this.hungry = 10;
        this.level = 1;
        //TODO:: Status, imageRoot 동적 변경 필요
        this.status = 2;
        this.imageRoot = "1111";
    }

}
