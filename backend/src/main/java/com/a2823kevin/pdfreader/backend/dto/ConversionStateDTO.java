package com.a2823kevin.pdfreader.backend.dto;

import com.a2823kevin.pdfreader.backend.model.ConversionStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversionStateDTO {
    private ConversionStatus status;
    private String progress;
    private String message;

    ConversionStateDTO() {}
    ConversionStateDTO(ConversionStatus status, String progress, String message) {
        setStatus(status);
        setProgress(progress);
        setMessage(message);
    }

    public static ConversionStateDTO failedNotFound() {
        return new ConversionStateDTO(ConversionStatus.FAILED, "0", "Error requesting task state: Task not found");
    }
}