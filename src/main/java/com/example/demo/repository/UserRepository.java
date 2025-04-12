package com.example.demo.repository;

import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public boolean existsByEmail(String email);
    public boolean existsByNickname(String nickname);
    UserEntity findByEmail(String email);
    UserEntity findByNickname(String nickname);
    List<UserEntity> findByGold(Integer money);
    void deleteByEmail(String email);

}