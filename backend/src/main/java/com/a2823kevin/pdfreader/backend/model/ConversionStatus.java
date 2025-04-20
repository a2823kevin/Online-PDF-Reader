package com.a2823kevin.pdfreader.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ConversionStatus {
    WORKING("working"), FINISHED("finished"), FAILED("failed");

    private final String value;
    ConversionStatus(String status) {
        this.value = status;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ConversionStatus fromValue(String value) {
        for (ConversionStatus status: values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
