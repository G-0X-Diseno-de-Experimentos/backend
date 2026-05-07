package com.textilflow.platform.reviews.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * SupplierId Value Object
 * Representa el identificador único de un proveedor en el contexto de reviews
 */
@Embeddable
public record SupplierId(Long supplierId) {

    public SupplierId {
        if (supplierId == null) {
            throw new IllegalArgumentException("Supplier ID cannot be null");
        }
        if (supplierId <= 0) {
            throw new IllegalArgumentException("Supplier ID must be positive");
        }
    }

    public SupplierId(String supplierId) {
        this(Long.valueOf(supplierId));
    }

    public String value() {
        return supplierId.toString();
    }
}