package com.example.demo.service.Impl;

import com.example.demo.Dto.response.MyBookResponseDto;
import com.example.demo.model.BookEntity;
import com.example.demo.model.ItemEntity;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private ItemRepository itemRepository;

    public BookServiceImpl(BookRepository bookRepository, ItemRepository itemRepository) {
        this.bookRepository = bookRepository;
        this.itemRepository = itemRepository;
    }

    public List<MyBookResponseDto> getBooksFromEmail(int category) {
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

    // 전체 도감 아이템 가져오기 + 아이템 획득 여부
    public List<MyBookResponseDto> getCompleteBookList(int category) {
        log.info("===============START: GET COMPLETE BOOK LIST ===================");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ItemEntity> allItems;

        // 아이템이 재료일때.. (카테고리1)
        if(category == 1) {
            allItems = itemRepository.findByIsDisplayTrueAndIsEggFalse();
        } else if(category == 2) { // 그게 아니고 알일때.. (카테고리2)
            allItems = itemRepository.findByIsDisplayFalseAndIsEggTrue();
        } else { // 기본값은 재료로 설정.
            allItems = itemRepository.findByIsDisplayTrueAndIsEggFalse();
        }

        // user가 획득한 ItemCode 목록
        List<BookEntity> userBooks = bookRepository.findByUserEntityEmail(email);
        Set<String> obtainedItemCodes = userBooks.stream()
                .map(book -> book.getItemEntity().getItemCode())
                .collect(Collectors.toSet());

        // 전체 아이템에 획득 여부 보이게끔..
        List<MyBookResponseDto> completeList = new ArrayList<>();
        for (ItemEntity item : allItems) {
            boolean isObtained = obtainedItemCodes.contains(item.getItemCode());

            MyBookResponseDto dto = MyBookResponseDto.builder()
                    .itemName(item.getItemName())
                    .itemDescription(item.getItemDescription())
                    .imageRoot(item.getImageRoot())
                    .isEgg(item.isEgg())
                    .isDisplay(item.isDisplay())
                    .isObtained(isObtained)
                    .build();

            completeList.add(dto);
        }
        return completeList;
    }

}
