package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BookEntity {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name="item_code", nullable = false)
    private ItemEntity itemEntity; //이렇게 적으면 itemEntity의 PK인 item_code가 칼럼으로 생성된다.

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private UserEntity userEntity; //UserEntity의 PK인 email이 칼럼으로 생성된다.
}
