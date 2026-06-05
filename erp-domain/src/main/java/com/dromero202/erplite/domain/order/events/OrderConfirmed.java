package com.dromero202.erplite.domain.order.events;

import java.time.Instant;

import com.dromero202.erplite.domain.common.DomainEvent;
import com.dromero202.erplite.domain.order.OrderId;

/**
 * Emitted when order transitions PENDING -> CONFIRMED.
 * TRIGGERS stock decrement.
 */
public record OrderConfirmed(
        OrderId orderId,
        Instant timestamp
) implements DomainEvent {
}
