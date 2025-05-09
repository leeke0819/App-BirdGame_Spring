package com.example.demo.model;

import jakarta.persistence.Entity;
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
    private int id;
    private String name;
    private int age;
    private int health;
    private int thirst;
    private int hungry;
    private int level;
    private short status; //1 부화전 2부화 함 3 레벨업 몇이상...
    private String imageRoot;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private BirdTypeEntity birdTypeEntity;
}
