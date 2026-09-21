package com.example.booksapi.service;

import com.example.booksapi.BooksApiApplication;
import com.example.booksapi.model.BookSearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(classes = BooksApiApplication.class)
class OpenLibraryServiceCachingTest {

    @Autowired
    private OpenLibraryService openLibraryService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void identicalSearchUsesCachedResponse() {
        BookSearchRequest request = new BookSearchRequest();
        request.setQuery("dune");

        server.expect(requestTo("https://openlibrary.org/search.json?page=1&limit=10&q=dune"))
                .andRespond(withSuccess("""
                        {
                          "numFound": 1,
                          "docs": [
                            {
                              "key": "/works/OL1W",
                              "title": "Dune",
                              "author_name": ["Frank Herbert"]
                            }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        BookSearchRequest repeatedRequest = new BookSearchRequest();
        repeatedRequest.setQuery("dune");

        openLibraryService.searchBooks(request);
        openLibraryService.searchBooks(repeatedRequest);

        server.verify();
    }
}
