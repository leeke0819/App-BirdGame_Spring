package com.example.demo.controller;

import com.example.demo.Dto.MyPageResponseDto;
import com.example.demo.model.ItemEntity;
import com.example.demo.service.ShopService;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
=======
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

>>>>>>> 5ea4cad941c2c8c82ee28c837ea87c8e820b5720
import java.util.List;

@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {
<<<<<<< HEAD

=======
>>>>>>> 5ea4cad941c2c8c82ee28c837ea87c8e820b5720
    @Autowired
    private ShopService shopService;

    @GetMapping
    public List<ItemEntity> shop() {
        return shopService.getItems();
    }
<<<<<<< HEAD

    @GetMapping("/page")
    public Page<ItemEntity> shopPage(@RequestParam String pageNo) {
        try {
            int pageNumber = Integer.parseInt(pageNo);
            return shopService.getPageItems(pageNumber, 10);
        }
        catch(NumberFormatException e){
            System.out.println("사용자가 잘못된 포맷으로 접근했습니다.");
        }
        return null;
    }

=======
>>>>>>> 5ea4cad941c2c8c82ee28c837ea87c8e820b5720
}
