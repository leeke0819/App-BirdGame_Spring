package com.example.demo.controller;

import com.example.demo.service.CraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/craft")
public class CraftController {

    @Autowired
    private CraftService craftService;

    @PostMapping
    public void craft(@RequestParam String itemCode) {
        craftService.craftItem(itemCode);
    }
}
