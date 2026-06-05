package com.dromero202.erplite.domain.product;

/**
 * Product name. Must be between 3 and 200 characters.
 */
public record ProductName(String value) {

    public ProductName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductName cannot be null or blank");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException(
                    "ProductName must be at least 3 characters, got: " + value.length());
        }
        if (value.length() > 200) {
            throw new IllegalArgumentException(
                    "ProductName must be at most 200 characters, got: " + value.length());
        }
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }
}
