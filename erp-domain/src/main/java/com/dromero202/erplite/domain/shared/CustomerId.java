package com.dromero202.erplite.domain.shared;

/**
 * Reference to external customer system (JSONPlaceholder).
 * Must be greater than 0.
 */
public record CustomerId(Long value) {

    public CustomerId {
        if (value == null) {
            throw new IllegalArgumentException("CustomerId value cannot be null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("CustomerId value must be greater than 0, got: " + value);
        }
    }

    public static CustomerId of(Long value) {
        return new CustomerId(value);
    }
}
