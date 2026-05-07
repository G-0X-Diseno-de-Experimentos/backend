package com.textilflow.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Business Type value object
 */
@Embeddable
public record BusinessType(String type) {
    public BusinessType {
        // ✅ PERMITIR null temporalmente hasta configuración
        if (type != null && type.isBlank()) {
            throw new IllegalArgumentException("Business type cannot be blank when provided");
        }
    }

    public BusinessType() {
        this(null);
    }
}