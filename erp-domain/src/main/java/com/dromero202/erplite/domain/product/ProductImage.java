package com.dromero202.erplite.domain.product;

import java.net.URI;

/**
 * Product image URL stored in AWS S3.
 */
public record ProductImage(String imageUrl) {

    public ProductImage {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("ProductImage imageUrl cannot be null or blank");
        }
        try {
            URI uri = URI.create(imageUrl);
            if (uri.getScheme() == null || (!uri.getScheme().equals("http") && !uri.getScheme().equals("https"))) {
                throw new IllegalArgumentException("ProductImage imageUrl must be a valid http/https URL, got: " + imageUrl);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ProductImage imageUrl is not a valid URL: " + imageUrl, e);
        }
    }

    public static ProductImage of(String imageUrl) {
        return new ProductImage(imageUrl);
    }

    public String getFullUrl() {
        return imageUrl;
    }

    public String getFileName() {
        String path = URI.create(imageUrl).getPath();
        int lastSlash = path.lastIndexOf('/');
        return lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
    }
}
