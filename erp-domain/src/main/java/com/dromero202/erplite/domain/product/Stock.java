package com.dromero202.erplite.domain.product;

/**
 * Product stock quantity. Cannot be negative.
 */
public record Stock(Integer value) {

    public Stock {
        if (value == null) {
            throw new IllegalArgumentException("Stock value cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Stock cannot be negative, got: " + value);
        }
    }

    public static Stock of(int value) {
        return new Stock(value);
    }

    public static Stock zero() {
        return new Stock(0);
    }

    public Stock increment(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Increment quantity must be positive, got: " + quantity);
        }
        return new Stock(this.value + quantity);
    }

    public Stock decrement(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Decrement quantity must be positive, got: " + quantity);
        }
        if (this.value < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock: available=" + this.value + ", requested=" + quantity);
        }
        return new Stock(this.value - quantity);
    }

    public boolean hasAvailable(int required) {
        if (required <= 0) {
            throw new IllegalArgumentException("Required quantity must be positive, got: " + required);
        }
        return this.value >= required;
    }
}
