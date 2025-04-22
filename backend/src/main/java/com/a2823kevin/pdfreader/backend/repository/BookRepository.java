package com.a2823kevin.pdfreader.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.a2823kevin.pdfreader.backend.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
    Book getByPdfPath(String pdfPath);
}
