package com.dromero202.erplite.domain.order.events;

import java.time.Instant;

import com.dromero202.erplite.domain.common.DomainEvent;
import com.dromero202.erplite.domain.order.OrderId;
import com.dromero202.erplite.domain.shared.CustomerId;
import com.dromero202.erplite.domain.shared.Money;

/**
 * Emitted when a new order is created.
 */
public record OrderCreated(
        OrderId orderId,
        CustomerId customerId,
        String customerName,
        Money totalAmount,
        Instant timestamp
) implements DomainEvent {
}
