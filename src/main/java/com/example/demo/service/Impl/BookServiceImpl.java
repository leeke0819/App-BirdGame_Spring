package com.example.demo.service.Impl;

import com.example.demo.Dto.response.MyBookResponseDto;
import com.example.demo.model.BookEntity;
import com.example.demo.model.ItemEntity;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private ItemRepository itemRepository;

    public BookServiceImpl(BookRepository bookRepository, ItemRepository itemRepository) {
        this.bookRepository = bookRepository;
        this.itemRepository = itemRepository;
    }

    public List<MyBookResponseDto> getBooksFromEmail() {
        log.info("===============START: GET BOOKS FROM EMAIL ===================");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug(email);
        List<BookEntity> bookEntities = bookRepository.findByUserEntityEmail(email);
        List<MyBookResponseDto> myBookResponseDtos = new ArrayList<>();
        for(int i = 0; i < bookEntities.size(); i++) {
            ItemEntity itemEntity = bookEntities.get(i).getItemEntity();
            MyBookResponseDto dto = MyBookResponseDto.builder()
                    .itemName(itemEntity.getItemName())
                    .itemDescription(itemEntity.getItemDescription())
                    .imageRoot(itemEntity.getImageRoot())
                    .build();
            myBookResponseDtos.add(dto);
        }
        return myBookResponseDtos;
    }
    /*
    public Page<ItemEntity> getBookPageItems(int page, int size, int category) {

        Pageable pageable = PageRequest.of(page, size);
        if (category == 1){
            return itemRepository.findAllItems(pageable);
        } else if ( category == 2) {
            return itemRepository.findAllEggs(pageable);
        }
        return itemRepository.findAllItems(pageable);
    }
    */

}
