package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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

    private Date lastFedAt;
    private Date lastThirstAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
    }

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
        this.createdAt = new Date();
        this.lastFedAt = new Date();
        this.lastThirstAt = new Date();
    }



}
