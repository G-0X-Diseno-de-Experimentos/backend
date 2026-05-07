package com.textilflow.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * RUC (Registro Único de Contribuyentes) value object
 */
@Embeddable
public record Ruc(String number) {
    public Ruc {
        // ✅ PERMITIR null temporalmente hasta configuración
        if (number != null && (number.isBlank() || !number.matches("\\d{11}"))) {
            throw new IllegalArgumentException("RUC must be 11 digits when provided");
        }
    }

    public Ruc() {
        this(null);
    }
}