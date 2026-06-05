package com.dromero202.erplite.domain.product.events;

import java.time.Instant;

import com.dromero202.erplite.domain.common.DomainEvent;
import com.dromero202.erplite.domain.product.ProductId;

/**
 * Emitted when product is deactivated. TRIGGERS sync to MongoDB.
 */
public record ProductDeactivated(
        ProductId productId,
        Instant timestamp
) implements DomainEvent {
}
