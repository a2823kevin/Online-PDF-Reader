package com.a2823kevin.pdfreader.backend.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.a2823kevin.pdfreader.backend.dto.LoginRequestDTO;
import com.a2823kevin.pdfreader.backend.dto.LoginResponseDTO;
import com.a2823kevin.pdfreader.backend.dto.RegisterRequestDTO;
import com.a2823kevin.pdfreader.backend.dto.RegisterResponseDTO;
import com.a2823kevin.pdfreader.backend.model.User;
import com.a2823kevin.pdfreader.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return RegisterResponseDTO.success(user.getUsername());
    }

    public LoginResponseDTO authenticateUser(LoginRequestDTO request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
    
        if (userOpt.isEmpty()) {
            return LoginResponseDTO.errorInvalidUser(request.getUsername());
        }
    
        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return LoginResponseDTO.errorPasswordIncorrect(request.getUsername());
        }

        String token = jwtService.generateToken(user.getUsername());
        return LoginResponseDTO.success(request.getUsername(), token);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
