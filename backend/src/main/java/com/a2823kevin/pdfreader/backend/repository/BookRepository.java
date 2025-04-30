package com.a2823kevin.pdfreader.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.a2823kevin.pdfreader.backend.model.Book;
import com.a2823kevin.pdfreader.backend.model.User;


@Repository
public interface BookRepository extends JpaRepository<Book, UUID>{
    List<Book> findByOwner(User owner);

    List<Book> findByOwnerAndCategory(User owner, String category);

    @Query("SELECT DISTINCT b.category FROM Book b WHERE b.owner.id = :userId")
    List<String> findCategories(@Param("userId") Long userId);
}
