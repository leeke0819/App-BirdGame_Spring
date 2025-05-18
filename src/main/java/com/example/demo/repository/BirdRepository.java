package com.example.demo.repository;

import com.example.demo.model.BirdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BirdRepository extends JpaRepository<BirdEntity, Long> {
    public Optional<BirdEntity> findByUserEmail(String email);
}
