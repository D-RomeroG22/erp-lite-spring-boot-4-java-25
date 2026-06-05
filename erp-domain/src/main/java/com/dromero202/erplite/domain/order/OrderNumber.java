package com.dromero202.erplite.domain.order;

import java.time.Year;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Unique order number. Pattern: ORD-2025-001.
 */
public record OrderNumber(String value) {

    private static final Pattern ORDER_NUMBER_PATTERN = Pattern.compile("^ORD-\\d{4}-\\d{3}$");
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    public OrderNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("OrderNumber value cannot be null or blank");
        }
        if (!ORDER_NUMBER_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "OrderNumber must match pattern ORD-YYYY-NNN (e.g. ORD-2025-001), got: " + value);
        }
    }

    public static OrderNumber of(String value) {
        return new OrderNumber(value);
    }

    public static OrderNumber generate() {
        int seq = SEQUENCE.incrementAndGet() % 1000;
        return new OrderNumber(String.format("ORD-%d-%03d", Year.now().getValue(), seq));
    }
}
