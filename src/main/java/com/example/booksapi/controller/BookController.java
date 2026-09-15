package com.example.booksapi.controller;

import com.example.booksapi.model.BookResponse;
import com.example.booksapi.model.BookSearchRequest;
import com.example.booksapi.service.OpenLibraryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    
    @Autowired
    private OpenLibraryService openLibraryService;
    
    @GetMapping("/search")
    public ResponseEntity<BookResponse> searchBooks(@Valid BookSearchRequest request) {
        BookResponse response = openLibraryService.searchBooks(request);
        return ResponseEntity.ok(response);
    }
}
