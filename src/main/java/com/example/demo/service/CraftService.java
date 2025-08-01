package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CraftService {
    void craftItem(String itemCode);
    List<String> getRecipe(String itemCode);
}
