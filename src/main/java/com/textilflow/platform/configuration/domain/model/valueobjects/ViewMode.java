package com.textilflow.platform.configuration.domain.model.valueobjects;

import java.util.Arrays;

/**
 * View mode enumeration with string conversion
 */
public enum ViewMode {
    LIGHT("light"),
    DARK("dark"),
    AUTO("auto");

    private final String value;

    ViewMode(String value) {
        this.value = value;
    }

    public static ViewMode fromString(String value) {
        if (value == null || value.isBlank()) {
            return AUTO; // default
        }

        // Make case-insensitive: "DARK", "Dark", "dark" all work
        String normalizedValue = value.toLowerCase();

        return Arrays.stream(values())
                .filter(mode -> mode.value.equals(normalizedValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid view mode: " + value + ". Valid values: light, dark, auto"));
    }

    public String getValue() {
        return value;
    }
}