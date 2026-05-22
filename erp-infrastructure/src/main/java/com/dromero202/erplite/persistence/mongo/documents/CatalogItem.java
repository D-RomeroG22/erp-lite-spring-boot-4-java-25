package com.dromero202.erplite.persistence.mongo.documents;

public record CatalogItem(
        String id,
        String code,
        String value,
        String description,
        Integer displayOrder,
        CatalogItemMetaData metadata
) {}
