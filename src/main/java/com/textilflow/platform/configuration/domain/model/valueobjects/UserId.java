package com.textilflow.platform.configuration.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * User ID value object
 * Embeddable value object for JPA mapping
 */
@Embeddable
public class UserId {

    private Long value;

    // JPA requires no-arg constructor
    protected UserId() {
    }

    public UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        this.value = value;
    }

    public Long value() {
        return value;
    }

    // Equals and hashCode for value object semantics
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserId userId = (UserId) obj;
        return value.equals(userId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "UserId{" + "value=" + value + '}';
    }
}