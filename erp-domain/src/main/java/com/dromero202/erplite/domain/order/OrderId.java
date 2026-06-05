package com.dromero202.erplite.domain.order;

import java.util.UUID;

/**
 * Unique identifier for Order aggregate.
 */
public record OrderId(UUID value) {

    public OrderId {
        if (value == null) {
            throw new IllegalArgumentException("OrderId value cannot be null");
        }
    }

    public static OrderId of(UUID value) {
        return new OrderId(value);
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }
}
