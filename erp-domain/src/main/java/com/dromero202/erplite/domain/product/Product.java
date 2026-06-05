package com.dromero202.erplite.domain.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

import com.dromero202.erplite.domain.common.AggregateRoot;
import com.dromero202.erplite.domain.product.events.ProductCreated;
import com.dromero202.erplite.domain.product.events.ProductDeactivated;
import com.dromero202.erplite.domain.product.events.ProductUpdated;
import com.dromero202.erplite.domain.product.events.StockChanged;
import com.dromero202.erplite.domain.shared.AuditInfo;
import com.dromero202.erplite.domain.shared.Money;

import static lombok.AccessLevel.PROTECTED;

/**
 * Product aggregate root.
 * SKU is immutable after creation.
 * Price must always be > 0.
 * Stock cannot be negative.
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Product extends AggregateRoot<ProductId> {

    private SKU sku;
    private ProductName name;
    private String description;
    private Money price;
    private Stock stock;
    private CategoryReference category;
    private ProductImage image;
    private boolean active;
    private AuditInfo auditInfo;

    private Product(
            ProductId id,
            SKU sku,
            ProductName name,
            String description,
            Money price,
            Stock stock,
            CategoryReference category,
            ProductImage image,
            boolean active,
            AuditInfo auditInfo
    ) {
        super(id);
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.image = image;
        this.active = active;
        this.auditInfo = auditInfo;
    }

    /**
     * Factory method: creates a new Product and registers ProductCreated event.
     */
    public static Product create(
            SKU sku,
            ProductName name,
            String description,
            Money price,
            Stock stock,
            CategoryReference category,
            ProductImage image,
            String createdBy
    ) {
        if (sku == null) throw new IllegalArgumentException("SKU cannot be null");
        if (name == null) throw new IllegalArgumentException("ProductName cannot be null");
        if (price == null) throw new IllegalArgumentException("Price cannot be null");
        if (price.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0, got: " + price.amount());
        }
        if (stock == null) throw new IllegalArgumentException("Stock cannot be null");
        if (category == null) throw new IllegalArgumentException("CategoryReference cannot be null");

        ProductId id = ProductId.generate();
        Instant now = Instant.now();
        AuditInfo auditInfo = AuditInfo.create(createdBy, now);

        Product product = new Product(id, sku, name, description, price, stock, category, image, true, auditInfo);
        product.registerEvent(new ProductCreated(id, sku, name, price, now));
        return product;
    }

    /**
     * Updates product information and registers ProductUpdated event.
     */
    public void update(
            ProductName name,
            String description,
            Money price,
            CategoryReference category,
            ProductImage image
    ) {
        if (name == null) throw new IllegalArgumentException("ProductName cannot be null");
        if (price == null) throw new IllegalArgumentException("Price cannot be null");
        if (price.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0, got: " + price.amount());
        }
        if (category == null) throw new IllegalArgumentException("CategoryReference cannot be null");

        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.auditInfo = auditInfo.updateTimestamp();

        registerEvent(new ProductUpdated(this.id, Instant.now()));
    }

    /**
     * Increments stock and registers StockChanged event.
     */
    public void incrementStock(int quantity, String reason) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Increment quantity must be positive, got: " + quantity);
        }
        int oldValue = this.stock.value();
        this.stock = this.stock.increment(quantity);
        this.auditInfo = auditInfo.updateTimestamp();

        registerEvent(new StockChanged(this.id, oldValue, this.stock.value(), reason, Instant.now()));
    }

    /**
     * Decrements stock and registers StockChanged event. Validates stock won't go negative.
     */
    public void decrementStock(int quantity, String reason) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Decrement quantity must be positive, got: " + quantity);
        }
        int oldValue = this.stock.value();
        this.stock = this.stock.decrement(quantity);
        this.auditInfo = auditInfo.updateTimestamp();

        registerEvent(new StockChanged(this.id, oldValue, this.stock.value(), reason, Instant.now()));
    }

    /**
     * Changes the product price. Must be > 0.
     */
    public void changePrice(Money newPrice) {
        if (newPrice == null) throw new IllegalArgumentException("New price cannot be null");
        if (newPrice.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0, got: " + newPrice.amount());
        }
        this.price = newPrice;
        this.auditInfo = auditInfo.updateTimestamp();

        registerEvent(new ProductUpdated(this.id, Instant.now()));
    }

    /**
     * Deactivates the product and registers ProductDeactivated event.
     */
    public void deactivate() {
        this.active = false;
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new ProductDeactivated(this.id, Instant.now()));
    }

    /**
     * Activates the product.
     */
    public void activate() {
        this.active = true;
        this.auditInfo = auditInfo.updateTimestamp();
    }

    /**
     * Returns true if there is enough stock for the required quantity.
     */
    public boolean hasAvailableStock(int requiredQuantity) {
        return this.stock.hasAvailable(requiredQuantity);
    }
}
