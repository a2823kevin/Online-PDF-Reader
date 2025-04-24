package com.a2823kevin.pdfreader.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a2823kevin.pdfreader.backend.dto.ApiResponse;
import com.a2823kevin.pdfreader.backend.dto.UserDTO;
import com.a2823kevin.pdfreader.backend.service.BookService;
import com.a2823kevin.pdfreader.backend.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final UserService userService;
    private final BookService bookService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(
            ApiResponse.success(
                "User list", 
                userService.getAllUser()
            )
        );
    }

    @PutMapping("/user/role/{id}")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        UserDTO userDTO = userService.updateUserRoles(id, roleIds);
        return ResponseEntity.ok(
            ApiResponse.success(
                "Role update succeed.", 
                userDTO
            )
        );
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok(
                ApiResponse.success("User delete succeed.", null)
            );
        }
        return ResponseEntity.badRequest().body(
            ApiResponse.error("User not found.")
        );
    }

    @GetMapping("/books")
    public ResponseEntity<?> getAllBooks() {
        return ResponseEntity.ok(
            ApiResponse.success(
                "Book list", 
                bookService.getAllBooks()
            )
        );
    }
}
