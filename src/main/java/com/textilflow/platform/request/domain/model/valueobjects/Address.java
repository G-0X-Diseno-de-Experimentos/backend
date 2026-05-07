package com.textilflow.platform.request.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(String value) {
    public Address {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or blank");
        }
    }
}
