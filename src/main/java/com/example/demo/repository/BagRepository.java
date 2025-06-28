package com.example.demo.repository;

import com.example.demo.model.BagEntity;
import com.example.demo.model.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BagRepository extends JpaRepository<BagEntity, Long> {
    public List<BagEntity> findByUserEmail(String email);

    @Query("SELECT b FROM BagEntity b WHERE b.user.email = :email AND b.item.itemCode = :itemCode")// JPQL 문법
    Optional<BagEntity> findByUserEmailAndItemCode(@Param("email") String email, @Param("itemCode") String itemCode);
    void deleteByUserEmail(String email);


}
