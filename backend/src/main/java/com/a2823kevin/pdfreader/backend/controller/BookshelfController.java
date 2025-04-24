package com.a2823kevin.pdfreader.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.a2823kevin.pdfreader.backend.dto.ApiResponse;
import com.a2823kevin.pdfreader.backend.dto.BookDTO;
import com.a2823kevin.pdfreader.backend.model.Visibility;
import com.a2823kevin.pdfreader.backend.security.AppUserDetails;
import com.a2823kevin.pdfreader.backend.service.BookService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/bookshelf")
@RequiredArgsConstructor
public class BookshelfController {

    private final BookService bookService;

    @PostMapping("/book")
    public ResponseEntity<?> addBook(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal AppUserDetails userDetails) {
        BookDTO book = bookService.addBook(file, userDetails.getId());
        return ResponseEntity.ok(
            ApiResponse.success(
                String.format("Add book successfully."), 
                book
            )
        );
    }

    @GetMapping("/books")
    public ResponseEntity<?> getBooks(@AuthenticationPrincipal AppUserDetails userDetails) {
        if (userDetails==null) {
            return ResponseEntity.ok(
                ApiResponse.success(
                    String.format("Find %d books.", 0), 
                    new ArrayList<BookDTO>()
                )
            );
        }
        List<BookDTO> books = bookService.getUserBooks(userDetails.getId());
        return ResponseEntity.ok(
            ApiResponse.success(
                String.format("Find %d books.", books.size()), 
                books
            )
        );
    }

    @PutMapping("/book/name/{id}")
    public ResponseEntity<?> updateBookName(@PathVariable UUID id, @RequestBody String name, @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(
            ApiResponse.success(
                "Update book name successfully.", 
                bookService.updateBookName(id, userDetails.getId(), name)
            )
        );
    }

    @PutMapping("/book/category/{id}")
    public ResponseEntity<?> updateBookCategory(@PathVariable UUID id, @RequestBody String category, @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(
            ApiResponse.success(
                "Update book name successfully.", 
                bookService.updateBookCategory(id, userDetails.getId(), category)
            )
        );
    }

    @PutMapping("/book/visibility/{id}")
    public ResponseEntity<?> updateBookVisibility(@PathVariable UUID id, @RequestBody Visibility visibility, @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(
            ApiResponse.success(
                "Update book name successfully.", 
                bookService.updateBookVisibility(id, userDetails.getId(), visibility)
            )
        );
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable UUID id, @AuthenticationPrincipal AppUserDetails userDetails) {
        boolean success = bookService.deleteBook(id, userDetails.getId());
        if (success) {
            return ResponseEntity.ok(
                ApiResponse.success(
                    "Delete book successfully.", 
                    null
                )
            );
        }
        return ResponseEntity.badRequest().body(
            ApiResponse.error(
                "Book not found."
            )
        );
    }
}
