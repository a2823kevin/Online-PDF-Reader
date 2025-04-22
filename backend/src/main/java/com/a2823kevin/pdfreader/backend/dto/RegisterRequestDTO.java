package com.a2823kevin.pdfreader.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotBlank
    @Size(min = 8, max = 32)
    private String username;

    @NotBlank
    private String password;
}
