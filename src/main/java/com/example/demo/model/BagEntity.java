package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private ItemEntity item;

    private int amount;


    // item 양 늘리기 (item 구매 함수)
    public void increaseItemAmount(int amount) {
        this.amount += amount;
    }

    // item 양 줄이기 (item 판매 함수)
    public void decreaseItemAmount(int amount) {
        this.amount = Math.max(0, this.amount - amount);
    }
}
