package com.a2823kevin.pdfreader.backend.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String status;
    private String message;
    private String token;

    public static LoginResponseDTO success(String username, String token) {
        LoginResponseDTO response = new LoginResponseDTO();
        response.status = "ok";
        response.message = String.format("User %s login successful.", username);
        response.token = token;
        return response;
    }

    public static LoginResponseDTO errorInvalidUser(String username) {
        LoginResponseDTO response = new LoginResponseDTO();
        response.status = "error";
        response.message = String.format("User %s doesn't exist.", username);
        return response;
    }

    public static LoginResponseDTO errorPasswordIncorrect(String username) {
        LoginResponseDTO response = new LoginResponseDTO();
        response.status = "error";
        response.message = String.format("Wrong password.", username);
        return response;
    }

}
