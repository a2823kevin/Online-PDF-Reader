package com.a2823kevin.pdfreader.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Visibility {
    PRIVATE("private"), WITHLINK("withlink"), PUBLIC("public");

    private final String value;

    Visibility(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Visibility fromValue(String value) {
        for (Visibility v: Visibility.values()) {
            if (v.value.equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unknown visibility: " + value);
    }
}