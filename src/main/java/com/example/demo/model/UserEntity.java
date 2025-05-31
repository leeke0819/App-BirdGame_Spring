package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class UserEntity {

    @Id
    private String email;

    private String password;

    private String nickname;

    private int gold;

    private int starCoin;

    private String Authority;

    private int exp;
    private int level;

    //유저의 레벨과 경험치 추가.
    //프로필 아이콘 url 추가 (추후에..)
}
