package com.example.booksapi.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookSearchRequest {
    @Size(max = 200)
    private String query;

    @Pattern(regexp = "author|subject|isbn", message = "filterType must be one of: author, subject, isbn")
    private String filterType;

    @Size(max = 200)
    private String filterValue;

    @Min(value = 0, message = "page must be greater than or equal to 0")
    private int page = 0;

    @Min(value = 1, message = "size must be at least 1")
    @Max(value = 100, message = "size must not exceed 100")
    private int size = 10;

    @AssertTrue(message = "filterType and filterValue must be provided together")
    public boolean isFilterComplete() {
        return (filterType == null) == (filterValue == null);
    }
}
