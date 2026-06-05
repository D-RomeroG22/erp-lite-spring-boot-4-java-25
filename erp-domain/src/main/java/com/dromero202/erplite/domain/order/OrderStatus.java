package com.dromero202.erplite.domain.order;

import java.util.Set;

/**
 * Order state with valid transitions.
 * DELIVERED and CANCELLED are final states.
 *
 * Valid transitions:
 *   PENDING    -> CONFIRMED | CANCELLED
 *   CONFIRMED  -> SHIPPED   | CANCELLED
 *   SHIPPED    -> DELIVERED
 *   DELIVERED  -> (final)
 *   CANCELLED  -> (final)
 */
public record OrderStatus(String value) {

    private static final Set<String> VALID_VALUES =
            Set.of("PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED");

    public OrderStatus {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("OrderStatus value cannot be null or blank");
        }
        if (!VALID_VALUES.contains(value)) {
            throw new IllegalArgumentException(
                    "OrderStatus must be one of " + VALID_VALUES + ", got: " + value);
        }
    }

    public static OrderStatus of(String value) {
        return new OrderStatus(value);
    }

    public static OrderStatus pending() {
        return new OrderStatus("PENDING");
    }

    public static OrderStatus confirmed() {
        return new OrderStatus("CONFIRMED");
    }

    public static OrderStatus shipped() {
        return new OrderStatus("SHIPPED");
    }

    public static OrderStatus delivered() {
        return new OrderStatus("DELIVERED");
    }

    public static OrderStatus cancelled() {
        return new OrderStatus("CANCELLED");
    }

    public boolean canTransitionTo(OrderStatus nextStatus) {
        if (nextStatus == null) return false;
        return switch (this.value) {
            case "PENDING"   -> Set.of("CONFIRMED", "CANCELLED").contains(nextStatus.value);
            case "CONFIRMED" -> Set.of("SHIPPED", "CANCELLED").contains(nextStatus.value);
            case "SHIPPED"   -> "DELIVERED".equals(nextStatus.value);
            default          -> false; // DELIVERED and CANCELLED are final states
        };
    }

    public boolean isPending()    { return "PENDING".equals(value); }
    public boolean isConfirmed()  { return "CONFIRMED".equals(value); }
    public boolean isShipped()    { return "SHIPPED".equals(value); }
    public boolean isDelivered()  { return "DELIVERED".equals(value); }
    public boolean isCancelled()  { return "CANCELLED".equals(value); }
    public boolean isFinalState() { return isDelivered() || isCancelled(); }
}
