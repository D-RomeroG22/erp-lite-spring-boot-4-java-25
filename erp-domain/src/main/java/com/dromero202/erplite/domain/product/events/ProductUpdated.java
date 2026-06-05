package com.dromero202.erplite.domain.product.events;

import java.time.Instant;

import com.dromero202.erplite.domain.common.DomainEvent;
import com.dromero202.erplite.domain.product.ProductId;

/**
 * Emitted when product info is updated. TRIGGERS sync to MongoDB.
 */
public record ProductUpdated(
        ProductId productId,
        Instant timestamp
) implements DomainEvent {
}
