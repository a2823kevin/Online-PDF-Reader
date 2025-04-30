package com.a2823kevin.pdfreader.backend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.a2823kevin.pdfreader.backend.model.User;
import com.a2823kevin.pdfreader.backend.model.Book;
import com.a2823kevin.pdfreader.backend.model.Bookmark;



public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{
    Optional<Bookmark> findByUserAndBook(User user, Book book);

    List<Bookmark> findByUserOrderByUpdateTimeDesc(User user, Pageable pageable);
}
