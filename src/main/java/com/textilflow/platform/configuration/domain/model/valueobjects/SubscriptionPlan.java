package com.textilflow.platform.configuration.domain.model.valueobjects;

import java.util.Arrays;

/**
 * Subscription plan enumeration with string conversion
 */
public enum SubscriptionPlan {
    BASIC("basic"),
    CORPORATE("corporate");

    private final String value;

    SubscriptionPlan(String value) {
        this.value = value;
    }

    public static SubscriptionPlan fromString(String value) {
        if (value == null || value.isBlank()) {
            return BASIC; // default
        }

        // Make case-insensitive: "BASIC", "Basic", "basic" all work
        String normalizedValue = value.toLowerCase();

        return Arrays.stream(values())
                .filter(plan -> plan.value.equals(normalizedValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription plan: " + value + ". Valid values: basic, corporate"));
    }

    public String getValue() {
        return value;
    }
}