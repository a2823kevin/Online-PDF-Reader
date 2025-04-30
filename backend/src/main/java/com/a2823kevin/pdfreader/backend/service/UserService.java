package com.a2823kevin.pdfreader.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.a2823kevin.pdfreader.backend.dto.ApiResponse;
import com.a2823kevin.pdfreader.backend.dto.UserDTO;
import com.a2823kevin.pdfreader.backend.dto.auth.LoginRequestDTO;
import com.a2823kevin.pdfreader.backend.dto.auth.RegisterRequestDTO;
import com.a2823kevin.pdfreader.backend.model.BlacklistedToken;
import com.a2823kevin.pdfreader.backend.model.Role;
import com.a2823kevin.pdfreader.backend.model.User;
import com.a2823kevin.pdfreader.backend.repository.BlacklistedTokenRepository;
import com.a2823kevin.pdfreader.backend.repository.RoleRepository;
import com.a2823kevin.pdfreader.backend.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtService jwtService;

    // Authentication
    public ApiResponse<?> registerUser(RegisterRequestDTO request) {
        if (!isUsernameTaken(request.getUsername())) {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            Role userRole = roleRepository.findByName("USER");
            user.setRoles(List.of(userRole));
            userRepository.save(user);

            // get jwt
            ApiResponse<?> loginResponse = authenticateUser(new LoginRequestDTO(user.getUsername(), request.getPassword()));
            if (loginResponse.getData() instanceof String token) {
                return ApiResponse.success(
                    String.format("Registration of %s is success.", user.getUsername()), 
                    token
                );
            }
            return ApiResponse.error("Unexpected error during registration.");
        }
        return ApiResponse.error(String.format("Username %s has been taken.", request.getUsername()));
    }

    public ApiResponse<?> authenticateUser(LoginRequestDTO request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
    
        if (userOpt.isEmpty()) {
            return ApiResponse.error(String.format("User %s doesn't exist.", request.getUsername()));
        }
    
        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResponse.error("Wrong password.");
        }

        String token = jwtService.generateToken(user);
        return ApiResponse.success(
            String.format("User %s login successful.", user.getUsername()), 
            token
        );
    }

    public ApiResponse<?> logoutUser(String jwt) {
        Claims claims = jwtService.getClaims(jwt);
        BlacklistedToken bToken = new BlacklistedToken();
        bToken.setToken(jwt);
        bToken.setExpiration(claims.getExpiration());
        blacklistedTokenRepository.save(bToken);

        return ApiResponse.success(
            String.format("User %s logout successful.", claims.getSubject()), 
            null
        );
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // User CRUD
    public List<UserDTO> getAllUser() {
        return userRepository.findAll()
            .stream()
            .map(user->new UserDTO(user.getId(), user.getUsername(), userRepository.findRoleNamesByUserId(user.getId())))
            .toList();
    }

    public UserDTO updateUserRoles(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.size()!=roleIds.size()) {
            throw new RuntimeException("Some roles not found");
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new UserDTO(
            userId, 
            user.getUsername(), 
            roles.stream().map(role->role.getName()).toList()
        );
    }

    public boolean deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }
}
