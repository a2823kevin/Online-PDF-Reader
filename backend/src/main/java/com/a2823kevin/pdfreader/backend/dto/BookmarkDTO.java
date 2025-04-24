package com.a2823kevin.pdfreader.backend.dto;

import java.util.UUID;

import com.a2823kevin.pdfreader.backend.model.Bookmark;

import lombok.Data;

@Data
public class BookmarkDTO {
    private UUID bookId;
    private Integer page;
    private Integer totalPage;

    public BookmarkDTO(Bookmark bookmark) {
        setBookId(bookmark.getBook().getId());
        setPage(bookmark.getPage());
        setTotalPage(bookmark.getTotalPage());
    }
}
