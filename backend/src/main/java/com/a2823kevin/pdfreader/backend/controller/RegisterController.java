package com.a2823kevin.pdfreader.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a2823kevin.pdfreader.backend.dto.RegisterRequestDTO;
import com.a2823kevin.pdfreader.backend.dto.RegisterResponseDTO;
import com.a2823kevin.pdfreader.backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        if (userService.isUsernameTaken(request.getUsername())) {
            return ResponseEntity.badRequest().body(RegisterResponseDTO.errorUsernameTaken(request.getUsername()));
        }

        RegisterResponseDTO response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }
}
