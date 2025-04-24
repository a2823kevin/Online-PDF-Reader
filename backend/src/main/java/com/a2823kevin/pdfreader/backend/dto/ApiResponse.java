package com.a2823kevin.pdfreader.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>("ok", msg, data);
    }

    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>("error", msg, null);
    }
}
