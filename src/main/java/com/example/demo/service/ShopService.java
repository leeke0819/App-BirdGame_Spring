package com.example.demo.service;

import com.example.demo.model.ItemEntity;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
=======
>>>>>>> 5ea4cad941c2c8c82ee28c837ea87c8e820b5720
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {
    @Autowired
    private ItemRepository itemRepository;
<<<<<<< HEAD

=======
>>>>>>> 5ea4cad941c2c8c82ee28c837ea87c8e820b5720
    public List<ItemEntity> getItems() {
        List<ItemEntity> itemEntities = itemRepository.findAll();
        return itemEntities;
    }

<<<<<<< HEAD
    public Page<ItemEntity> getPageItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAll(pageable);
    }

=======
>>>>>>> 5ea4cad941c2c8c82ee28c837ea87c8e820b5720
    // itementity에서 가져와서 .. db table에 있는 거 전체를 가져오기..
}
