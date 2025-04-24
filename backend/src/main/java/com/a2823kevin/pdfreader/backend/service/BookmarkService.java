package com.a2823kevin.pdfreader.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import com.a2823kevin.pdfreader.backend.dto.BookmarkDTO;
import com.a2823kevin.pdfreader.backend.model.Book;
import com.a2823kevin.pdfreader.backend.model.Bookmark;
import com.a2823kevin.pdfreader.backend.model.User;
import com.a2823kevin.pdfreader.backend.model.Visibility;
import com.a2823kevin.pdfreader.backend.repository.BookRepository;
import com.a2823kevin.pdfreader.backend.repository.BookmarkRepository;
import com.a2823kevin.pdfreader.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

    public BookmarkDTO addBookmark(UUID bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(()->new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));

        checkVisibility(book, user);

        PDDocument document;
        try {
            document = Loader.loadPDF(new File(book.getPdfPath()));
            Bookmark bookmark = new Bookmark();
            bookmark.setBook(book);
            bookmark.setUser(user);
            bookmark.setPage(1);
            bookmark.setTotalPage(document.getNumberOfPages());
            bookmarkRepository.save(bookmark);
            return new BookmarkDTO(bookmark);
        }
        catch (IOException e) {
            throw new RuntimeException("PDF file not found");
        }
    }

    public BookmarkDTO getBookmark(UUID bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(()->new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));

        Bookmark bookmark = bookmarkRepository.findByUserAndBook(user, book)
            .orElseThrow(()->new RuntimeException("Bookmark not found"));
        return new BookmarkDTO(bookmark);
    }

    public BookmarkDTO updateBookmark(UUID bookId, Long userId, Integer page) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(()->new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));

        Bookmark bookmark = bookmarkRepository.findByUserAndBook(user, book)
            .orElseThrow(()->new RuntimeException("Bookmark not found"));
        
        if (page>bookmark.getTotalPage()) {
            throw new RuntimeException("Invalid page number");
        }
        bookmark.setPage(page);
        bookmarkRepository.save(bookmark);
        return new BookmarkDTO(bookmark);
    }

    private void checkVisibility(Book book, User user) {
        if (book.getVisibility()==Visibility.PRIVATE) {
            if (book.getOwner().getId()!=user.getId()) {
                throw new RuntimeException("Book is private");
            }
        }
    }
}
