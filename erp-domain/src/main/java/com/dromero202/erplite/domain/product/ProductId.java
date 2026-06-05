package com.dromero202.erplite.domain.product;

import java.util.UUID;

/**
 * Unique identifier for Product aggregate.
 */
public record ProductId(UUID value) {

    public ProductId {
        if (value == null) {
            throw new IllegalArgumentException("ProductId value cannot be null");
        }
    }

    public static ProductId of(UUID value) {
        return new ProductId(value);
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }
}
