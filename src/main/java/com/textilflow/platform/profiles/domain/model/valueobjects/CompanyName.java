package com.textilflow.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Company Name value object
 */
@Embeddable
public record CompanyName(String name) {
    public CompanyName {
        // ✅ PERMITIR null temporalmente hasta configuración
        if (name != null && name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be blank");
        }
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("Company name cannot exceed 100 characters");
        }
    }

    public CompanyName() {
        this(null);
    }
}