package com.example.booksapi.service;

import com.example.booksapi.model.Book;
import com.example.booksapi.model.BookResponse;
import com.example.booksapi.model.BookSearchRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenLibraryService {
    
    private static final String OPEN_LIBRARY_API_URL = "https://openlibrary.org/search.json";
    
    @Autowired
    private RestTemplate restTemplate;
    
    public BookResponse searchBooks(BookSearchRequest request) {
        String url = buildSearchUrl(request);
        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        return mapToBookResponse(response, request.getPage(), request.getSize());
    }
    
    private String buildSearchUrl(BookSearchRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(OPEN_LIBRARY_API_URL)
            .queryParam("page", request.getPage() + 1)
            .queryParam("limit", request.getSize());

        if (request.getFilterType() != null) {
            switch (request.getFilterType().toLowerCase()) {
                case "author":
                    builder.queryParam("author", request.getFilterValue());
                    break;
                case "subject":
                    builder.queryParam("subject", request.getFilterValue());
                    break;
                case "isbn":
                    builder.queryParam("isbn", request.getFilterValue());
                    break;
                default:
                    builder.queryParam("q", request.getQuery());
            }
        } else if (request.getQuery() != null) {
            builder.queryParam("q", request.getQuery());
        }

        return builder.build().toUriString();
    }
    
    private BookResponse mapToBookResponse(JsonNode response, int page, int size) {
        BookResponse bookResponse = new BookResponse();
        List<Book> books = new ArrayList<>();
        
        JsonNode docs = response.get("docs");
        if (docs != null && docs.isArray()) {
            for (JsonNode doc : docs) {
                Book book = new Book();
                book.setKey(doc.path("key").asText());
                book.setTitle(doc.path("title").asText());
                
                List<String> authors = new ArrayList<>();
                if (doc.has("author_name")) {
                    doc.get("author_name").forEach(author -> authors.add(author.asText()));
                }
                book.setAuthors(authors);
                
                if (doc.has("first_publish_year")) {
                    book.setPublishYear(doc.get("first_publish_year").asText());
                }
                
                List<String> subjects = new ArrayList<>();
                if (doc.has("subject")) {
                    doc.get("subject").forEach(subject -> subjects.add(subject.asText()));
                }
                book.setSubjects(subjects);
                
                if (doc.has("isbn")) {
                    book.setIsbn(doc.get("isbn").get(0).asText());
                }
                
                if (doc.has("cover_i")) {
                    String coverId = doc.get("cover_i").asText();
                    book.setCoverUrl("https://covers.openlibrary.org/b/id/" + coverId + "-L.jpg");
                }
                
                books.add(book);
            }
        }
        
        bookResponse.setBooks(books);
        bookResponse.setPage(page);
        bookResponse.setSize(size);
        bookResponse.setTotalElements(response.path("numFound").asLong());
        bookResponse.setTotalPages((int) Math.ceil(response.path("numFound").asDouble() / size));
        
        return bookResponse;
    }
}
