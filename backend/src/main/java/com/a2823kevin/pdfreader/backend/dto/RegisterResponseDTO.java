package com.a2823kevin.pdfreader.backend.dto;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    private String status;
    private String message;

    public static RegisterResponseDTO success(String username) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        response.status = "ok";
        response.message = String.format("Registration of %s is success.", username);
        return response;
    }

    public static RegisterResponseDTO errorUsernameTaken(String username) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        response.status = "error";
        response.message = String.format("Username %s has been taken.", username);
        return response;
    }
}
