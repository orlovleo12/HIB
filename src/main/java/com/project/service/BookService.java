package com.project.service;

import com.project.model.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    List<BookDTO> getAllBookDTO();
    void addBook(BookDTO bookDTO);
    Page<BookDTO> findAll(Pageable pageable);
    void delBook(long id);
    void updateBook(BookDTO bookDTO);
}
