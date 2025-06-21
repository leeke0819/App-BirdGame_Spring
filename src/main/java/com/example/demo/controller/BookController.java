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

    @GetMapping
    public List<MyBookResponseDto> getBook() {
        return bookService.getBooksFromEmail();
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
