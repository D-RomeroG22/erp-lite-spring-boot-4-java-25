package com.dromero202.erplite.domain.product;

import java.util.regex.Pattern;

/**
 * Stock Keeping Unit. Unique and immutable after Product creation.
 * Pattern: LAPTOP-001
 */
public record SKU(String value) {

    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z]+-\\d{3}$");

    public SKU {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SKU value cannot be null or blank");
        }
        if (!SKU_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "SKU must match pattern [A-Z]+-\\d{3} (e.g. LAPTOP-001), got: " + value);
        }
    }

    public static SKU of(String value) {
        return new SKU(value);
    }
}
