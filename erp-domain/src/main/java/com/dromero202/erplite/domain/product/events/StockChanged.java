package com.dromero202.erplite.domain.product.events;

import java.time.Instant;

import com.dromero202.erplite.domain.common.DomainEvent;
import com.dromero202.erplite.domain.product.ProductId;

/**
 * Emitted when stock changes (increment or decrement). TRIGGERS sync to MongoDB.
 */
public record StockChanged(
        ProductId productId,
        Integer oldStock,
        Integer newStock,
        String reason,
        Instant timestamp
) implements DomainEvent {
}
