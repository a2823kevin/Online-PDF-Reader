package com.a2823kevin.pdfreader.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a2823kevin.pdfreader.backend.model.BlacklistedToken;


public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long>{
    Optional<BlacklistedToken> findByToken(String token);
}