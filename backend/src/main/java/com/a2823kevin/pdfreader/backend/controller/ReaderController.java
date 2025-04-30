package com.a2823kevin.pdfreader.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a2823kevin.pdfreader.backend.dto.ApiResponse;
import com.a2823kevin.pdfreader.backend.dto.BookmarkDTO;
import com.a2823kevin.pdfreader.backend.security.AppUserDetails;
import com.a2823kevin.pdfreader.backend.service.BookService;
import com.a2823kevin.pdfreader.backend.service.BookmarkService;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/reader")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ReaderController {
    private final BookService bookService;
    private final BookmarkService bookmarkService;

    @PostMapping("/bookmark/{bookId}")
    public ResponseEntity<?> addBookmark(@PathVariable UUID bookId, @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(
            ApiResponse.success(
                "Add bookmark successfully.", 
                bookmarkService.addBookmark(bookId, userDetails.getId())
            )
        );
    }
    
    @GetMapping("/content/{id}")
    public ResponseEntity<?> getBookContent(@PathVariable UUID id, @AuthenticationPrincipal AppUserDetails userDetails) {
        byte[] content = bookService.getBookContent(id, userDetails.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @GetMapping("/bookmark/{id}")
    public ResponseEntity<?> getBookmark(@PathVariable UUID id, @AuthenticationPrincipal AppUserDetails userDetails) {
        BookmarkDTO bookmark = bookmarkService.getBookmark(id, userDetails.getId());
        if (bookmark!=null) {
            return ResponseEntity.ok(
                ApiResponse.success(
                    "Get bookmark successfully.", 
                    bookmarkService.getBookmark(id, userDetails.getId())
                )
            );
        }
        return ResponseEntity.ok(
            ApiResponse.error("Bookmark hasn't been created.")
        );
    }

    @PutMapping("/bookmark/{id}")
    public ResponseEntity<?> updateBookmark(@PathVariable UUID id, @RequestBody Integer page, @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(
            ApiResponse.success(
                "Update bookmark successfully.", 
                bookmarkService.updateBookmark(id, userDetails.getId(), page)
            )
        );
    }
}
