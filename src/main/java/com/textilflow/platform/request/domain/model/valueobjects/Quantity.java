package com.textilflow.platform.request.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Quantity(Integer value) {
    public Quantity {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}
