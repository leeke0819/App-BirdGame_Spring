package com.example.demo.repository;

import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public boolean existsByEmail(String email);
    public boolean existsByNickname(String nickname);
    UserEntity findByEmail(String email);
    UserEntity findByNickname(String nickname);
    void deleteByEmail(String email);

    @Query("SELECT u.gold FROM UserEntity u WHERE u.email = :email")
    Integer findGoldByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.gold = u.gold + :amount WHERE u.email = :email")
    void addGoldToUser(@Param("email") String email, @Param("amount") int amount);

}