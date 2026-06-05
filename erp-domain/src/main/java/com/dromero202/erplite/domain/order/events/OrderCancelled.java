package com.dromero202.erplite.domain.order.events;

import java.time.Instant;

import com.dromero202.erplite.domain.common.DomainEvent;
import com.dromero202.erplite.domain.order.OrderId;

/**
 * Emitted when order is cancelled.
 * If was CONFIRMED, stock must be released.
 */
public record OrderCancelled(
        OrderId orderId,
        String reason,
        Instant timestamp
) implements DomainEvent {
}
