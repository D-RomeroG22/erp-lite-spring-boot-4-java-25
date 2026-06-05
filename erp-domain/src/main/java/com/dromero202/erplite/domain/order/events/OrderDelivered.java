package com.dromero202.erplite.domain.order.events;

import java.time.Instant;

import com.dromero202.erplite.domain.common.DomainEvent;
import com.dromero202.erplite.domain.order.OrderId;

/**
 * Emitted when order transitions SHIPPED -> DELIVERED. Final state.
 */
public record OrderDelivered(
        OrderId orderId,
        Instant timestamp
) implements DomainEvent {
}
