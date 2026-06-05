package com.dromero202.erplite.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.dromero202.erplite.domain.common.Entity;
import com.dromero202.erplite.domain.product.Product;
import com.dromero202.erplite.domain.product.ProductId;
import com.dromero202.erplite.domain.shared.Money;
import com.dromero202.erplite.domain.shared.Quantity;

/**
 * Order line item entity.
 * productName and unitPrice are snapshots taken at order creation — immutable.
 * subtotal = quantity * unitPrice.
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class OrderItem extends Entity<OrderItemId> {

    private OrderItemId id;
    private ProductId productReference;
    private String productName;
    private Quantity quantity;
    private Money unitPrice;
    private Money subtotal;

    /**
     * Factory method: creates an OrderItem as a snapshot from a Product at the moment of ordering.
     */
    public static OrderItem from(Product product, Quantity quantity) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        if (quantity == null) throw new IllegalArgumentException("Quantity cannot be null");

        OrderItemId itemId = OrderItemId.generate();
        Money unitPrice = product.getPrice();
        Money subtotal = unitPrice.multiply(quantity);

        return new OrderItem(
                itemId,
                product.getId(),
                product.getName().value(),
                quantity,
                unitPrice,
                subtotal
        );
    }

    /**
     * Returns quantity * unitPrice.
     */
    public Money calculateSubtotal() {
        return unitPrice.multiply(quantity);
    }
}
