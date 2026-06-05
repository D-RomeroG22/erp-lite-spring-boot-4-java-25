package com.dromero202.erplite.domain.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dromero202.erplite.domain.common.AggregateRoot;
import com.dromero202.erplite.domain.order.events.OrderCancelled;
import com.dromero202.erplite.domain.order.events.OrderConfirmed;
import com.dromero202.erplite.domain.order.events.OrderCreated;
import com.dromero202.erplite.domain.order.events.OrderDelivered;
import com.dromero202.erplite.domain.order.events.OrderShipped;
import com.dromero202.erplite.domain.shared.AuditInfo;
import com.dromero202.erplite.domain.shared.Money;

import static lombok.AccessLevel.PROTECTED;

/**
 * Order aggregate root.
 *
 * Business rules:
 * - totalAmount MUST equal sum of all OrderItem.subtotal
 * - items list must have at least 1 item
 * - all Money amounts must use the same Currency
 * - status transitions are strictly validated
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Order extends AggregateRoot<OrderId> {

    private OrderNumber orderNumber;
    private Customer customer;
    private OrderStatus status;
    private List<OrderItem> items;
    private Money totalAmount;
    private AuditInfo auditInfo;

    private Order(
            OrderId id,
            OrderNumber orderNumber,
            Customer customer,
            OrderStatus status,
            List<OrderItem> items,
            Money totalAmount,
            AuditInfo auditInfo
    ) {
        super(id);
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.status = status;
        this.items = new ArrayList<>(items);
        this.totalAmount = totalAmount;
        this.auditInfo = auditInfo;
    }

    /**
     * Factory method: creates a new Order in PENDING state and registers OrderCreated event.
     */
    public static Order create(
            OrderNumber orderNumber,
            Customer customer,
            List<OrderItem> items,
            String createdBy
    ) {
        if (orderNumber == null) throw new IllegalArgumentException("OrderNumber cannot be null");
        if (customer == null) throw new IllegalArgumentException("Customer cannot be null");
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        OrderId id = OrderId.generate();
        Instant now = Instant.now();
        AuditInfo auditInfo = AuditInfo.create(createdBy, now);
        OrderStatus status = OrderStatus.pending();

        Order order = new Order(id, orderNumber, customer, status, items, null, auditInfo);
        order.calculateTotal();

        order.registerEvent(new OrderCreated(
                id,
                customer.customerId(),
                customer.customerName(),
                order.totalAmount,
                now
        ));
        return order;
    }

    /**
     * Transitions order from PENDING to CONFIRMED. TRIGGERS stock decrement via event.
     */
    public void confirm() {
        validateTransition(OrderStatus.confirmed());
        this.status = OrderStatus.confirmed();
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new OrderConfirmed(this.id, Instant.now()));
    }

    /**
     * Transitions order from CONFIRMED to SHIPPED.
     */
    public void ship() {
        validateTransition(OrderStatus.shipped());
        this.status = OrderStatus.shipped();
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new OrderShipped(this.id, Instant.now()));
    }

    /**
     * Transitions order from SHIPPED to DELIVERED. Final state.
     */
    public void deliver() {
        validateTransition(OrderStatus.delivered());
        this.status = OrderStatus.delivered();
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new OrderDelivered(this.id, Instant.now()));
    }

    /**
     * Cancels the order. If was CONFIRMED, stock must be released (handled by event consumer).
     */
    public void cancel(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Cancellation reason cannot be null or blank");
        }
        validateTransition(OrderStatus.cancelled());
        this.status = OrderStatus.cancelled();
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new OrderCancelled(this.id, reason, Instant.now()));
    }

    /**
     * Adds an item to the order and recalculates the total.
     */
    public void addItem(OrderItem item) {
        if (item == null) throw new IllegalArgumentException("OrderItem cannot be null");
        if (!status.isPending()) {
            throw new IllegalStateException("Items can only be added to PENDING orders");
        }
        validateCurrencyConsistency(item);
        items.add(item);
        calculateTotal();
        this.auditInfo = auditInfo.updateTimestamp();
    }

    /**
     * Removes an item from the order and recalculates the total.
     */
    public void removeItem(OrderItem item) {
        if (item == null) throw new IllegalArgumentException("OrderItem cannot be null");
        if (!status.isPending()) {
            throw new IllegalStateException("Items can only be removed from PENDING orders");
        }
        boolean removed = items.remove(item);
        if (!removed) {
            throw new IllegalArgumentException("Item not found in order: " + item.getId());
        }
        validateItems();
        calculateTotal();
        this.auditInfo = auditInfo.updateTimestamp();
    }

    /**
     * Recalculates totalAmount as the sum of all item subtotals.
     */
    public void calculateTotal() {
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Cannot calculate total: order has no items");
        }
        Money total = items.getFirst().getSubtotal();
        for (int i = 1; i < items.size(); i++) {
            total = total.add(items.get(i).getSubtotal());
        }
        this.totalAmount = total;
    }

    /**
     * Returns an unmodifiable view of the items list.
     */
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    // -------------------------------------------------------------------------
    // Validation helpers
    // -------------------------------------------------------------------------

    private void validateItems() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }
    }

    private void validateTransition(OrderStatus nextStatus) {
        if (!this.status.canTransitionTo(nextStatus)) {
            throw new IllegalStateException(
                    "Invalid order status transition: " + this.status.value() + " -> " + nextStatus.value());
        }
    }

    private void validateCurrencyConsistency(OrderItem newItem) {
        if (!items.isEmpty()) {
            Money existing = items.getFirst().getSubtotal();
            if (!existing.currency().equals(newItem.getSubtotal().currency())) {
                throw new IllegalArgumentException(
                        "Currency mismatch: order uses " + existing.currency()
                        + " but item uses " + newItem.getSubtotal().currency());
            }
        }
    }
}
