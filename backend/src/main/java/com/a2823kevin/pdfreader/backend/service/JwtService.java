package com.a2823kevin.pdfreader.backend.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.a2823kevin.pdfreader.backend.config.JwtProperties;
import com.a2823kevin.pdfreader.backend.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final SecretKey signingKey;
    private final Long expirationTime;
    public JwtService(JwtProperties jwtProperties) {
        signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        expirationTime = jwtProperties.getExpirationTime();
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRoles().stream().map(role->role.getName()).toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    private boolean isTokenExpired(Claims jwtClaims) {
        return jwtClaims.getExpiration().before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = getClaims(token);
        final String username = claims.getSubject();
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(claims));
    }
}
