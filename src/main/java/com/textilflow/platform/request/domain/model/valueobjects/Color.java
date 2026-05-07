package com.textilflow.platform.request.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Color(String value) {
    public Color {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Color cannot be null or blank");
        }
    }
}
