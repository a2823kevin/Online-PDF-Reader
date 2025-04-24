package com.a2823kevin.pdfreader.backend.dto;

import java.util.UUID;

import com.a2823kevin.pdfreader.backend.model.Book;
import com.a2823kevin.pdfreader.backend.model.Visibility;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDTO {
    private UUID id;
    private String bookname;
    private String category;
    private Visibility visibility;
    private String owner;

    public BookDTO(Book book) {
        setId(book.getId());
        setBookname(book.getName());
        setCategory(book.getCategory());
        setVisibility(book.getVisibility());
        setOwner(book.getOwner().getUsername());

    }
}
