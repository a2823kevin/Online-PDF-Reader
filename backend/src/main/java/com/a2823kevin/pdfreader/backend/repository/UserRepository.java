package com.a2823kevin.pdfreader.backend.repository;

import com.a2823kevin.pdfreader.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT r.name FROM User u JOIN u.roles r WHERE u.id = :userId")
    List<String> findRoleNamesByUserId(@Param("userId") Long userId);
}
