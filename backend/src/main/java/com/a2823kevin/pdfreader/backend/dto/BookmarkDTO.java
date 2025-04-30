package com.a2823kevin.pdfreader.backend.dto;

import java.util.Date;
import java.util.UUID;

import com.a2823kevin.pdfreader.backend.model.Bookmark;

import lombok.Data;

@Data
public class BookmarkDTO {
    private UUID bookId;
    private String bookName;
    private Integer page;
    private Integer totalPage;
    private Date updateTime;

    public BookmarkDTO(Bookmark bookmark) {
        setBookId(bookmark.getBook().getId());
        setBookName(bookmark.getBook().getName());
        setPage(bookmark.getPage());
        setTotalPage(bookmark.getTotalPage());
        setUpdateTime(bookmark.getUpdateTime());
    }
}
