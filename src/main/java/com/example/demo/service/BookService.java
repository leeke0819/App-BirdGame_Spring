package com.example.demo.service;

import com.example.demo.Dto.response.MyBookResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BookService {
    List<MyBookResponseDto> getBooksFromEmail();

}
