package com.a2823kevin.pdfreader.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UploadPdfStatus {
    ACCEPTED("Accepted"), REJECTED("Rejected");

    private final String value;
    UploadPdfStatus(String status) {
        this.value = status;
    }
    
    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UploadPdfStatus fromValue(String value) {
        for (UploadPdfStatus status: values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}