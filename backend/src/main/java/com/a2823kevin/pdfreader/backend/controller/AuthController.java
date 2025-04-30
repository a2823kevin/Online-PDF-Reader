package com.a2823kevin.pdfreader.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a2823kevin.pdfreader.backend.dto.ApiResponse;
import com.a2823kevin.pdfreader.backend.dto.auth.LoginRequestDTO;
import com.a2823kevin.pdfreader.backend.dto.auth.RegisterRequestDTO;
import com.a2823kevin.pdfreader.backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        ApiResponse<?> response = userService.registerUser(request);
        if (response.getStatus().equals("ok")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        ApiResponse<?> response = userService.authenticateUser(request);
        if (response.getStatus().equals("ok")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("No token provided");
        }

        String jwt = authHeader.substring(7);
        return ResponseEntity.ok(userService.logoutUser(jwt));
    }
    
}
