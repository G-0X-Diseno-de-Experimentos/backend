package com.textilflow.platform.configuration.domain.model.valueobjects;

import java.util.Arrays;

/**
 * Language enumeration with string conversion
 */
public enum Language {
    ES("es"),
    EN("en");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    public static Language fromString(String value) {
        if (value == null || value.isBlank()) {
            return ES; // default
        }

        // Make case-insensitive: "ES" or "es" both work
        String normalizedValue = value.toLowerCase();

        return Arrays.stream(values())
                .filter(lang -> lang.value.equals(normalizedValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid language: " + value + ". Valid values: es, en"));
    }

    public String getValue() {
        return value;
    }
}