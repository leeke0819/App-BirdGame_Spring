package com.example.demo.service;

import com.example.demo.Dto.response.MyBookResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    List<MyBookResponseDto> getBooksFromEmail(int category);
    List<MyBookResponseDto> getCompleteBookList(int category);

}
