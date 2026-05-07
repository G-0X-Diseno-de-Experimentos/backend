package com.textilflow.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Specialization value object for suppliers
 */
@Embeddable
public record Specialization(String area) {
    public Specialization {
        // ✅ PERMITIR null temporalmente hasta configuración
        if (area != null && area.isBlank()) {
            throw new IllegalArgumentException("Specialization area cannot be blank when provided");
        }
    }

    public Specialization() {
        this(null);
    }
}