package com.example.demo.controller;

import com.example.demo.Dto.response.MyBookResponseDto;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("get-list")
    public List<MyBookResponseDto> getBook(@RequestParam(defaultValue = "1") int category) {
        return bookService.getBooksFromEmail(category);
    }

    // 도감 다 획득했을 때 기준으로 모든 아이템 리스트 가져오고 획득 여부 알기
    @GetMapping("get-complete-list")
    public List<MyBookResponseDto> getCompleteBookList(@RequestParam(defaultValue = "1") int category) {
        return bookService.getCompleteBookList(category);
    }

//
//    @PostMapping
//    public void postBook() {
//
//    }
//
//    @PutMapping
//    public void updateBook() {
//
//    }
//
//    @DeleteMapping
//    public void deleteBook() {
//
//    }
}
