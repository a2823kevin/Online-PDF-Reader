package com.a2823kevin.pdfreader.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.a2823kevin.pdfreader.backend.model.Book;
import com.a2823kevin.pdfreader.backend.model.User;


@Repository
public interface BookRepository extends JpaRepository<Book, UUID>{
    // Optional<Book> findByPdfPath(String pdfPath);
    List<Book> findByOwner(User owner);
}
