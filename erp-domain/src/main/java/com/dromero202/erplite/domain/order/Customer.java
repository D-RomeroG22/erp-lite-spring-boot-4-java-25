package com.dromero202.erplite.domain.order;

import com.dromero202.erplite.domain.shared.CustomerId;

/**
 * Customer reference with basic info (snapshot at order creation).
 */
public record Customer(CustomerId customerId, String customerName) {

    public Customer {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer customerId cannot be null");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer customerName cannot be null or blank");
        }
    }

    public static Customer of(CustomerId customerId, String customerName) {
        return new Customer(customerId, customerName);
    }
}
