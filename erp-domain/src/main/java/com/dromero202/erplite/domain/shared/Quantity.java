package com.dromero202.erplite.domain.shared;

/**
 * Quantity of items in an order. Must be greater than 0.
 */
public record Quantity(Integer value) {

    public Quantity {
        if (value == null) {
            throw new IllegalArgumentException("Quantity value cannot be null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0, got: " + value);
        }
    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }
}
