package com.dromero202.erplite.domain.product.events;

import java.time.Instant;

import com.dromero202.erplite.domain.common.DomainEvent;
import com.dromero202.erplite.domain.product.ProductId;
import com.dromero202.erplite.domain.product.ProductName;
import com.dromero202.erplite.domain.product.SKU;
import com.dromero202.erplite.domain.shared.Money;

/**
 * Emitted when a new product is created. TRIGGERS sync to MongoDB (CQRS).
 */
public record ProductCreated(
        ProductId productId,
        SKU sku,
        ProductName name,
        Money price,
        Instant timestamp
) implements DomainEvent {
}
