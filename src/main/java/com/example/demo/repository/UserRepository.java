package com.example.demo.repository;

import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public boolean existsByEmail(String email);
    public boolean existsByNickname(String nickname);
    UserEntity findByEmail(String email);
    UserEntity findByNickname(String nickname);
    void deleteByEmail(String email);

    @Query("SELECT u.gold FROM User u WHERE u.email = :email")
    Integer findGoldByEmail(@Param("email") String email);

}