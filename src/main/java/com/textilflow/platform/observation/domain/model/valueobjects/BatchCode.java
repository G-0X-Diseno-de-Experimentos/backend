package com.textilflow.platform.observation.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class BatchCode {
    private String code;

    public BatchCode() {}

    public BatchCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Batch code cannot be null or empty");
        }
        this.code = code.trim();
    }
}
