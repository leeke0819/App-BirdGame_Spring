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
public class UserEntity {

    @Id
    private String email;

    private String password;

    private String nickname;

    private Integer money;

    private String Authority;

    @OneToMany(mappedBy = "user")
    private List<BagEntity> bagEntities;



}
