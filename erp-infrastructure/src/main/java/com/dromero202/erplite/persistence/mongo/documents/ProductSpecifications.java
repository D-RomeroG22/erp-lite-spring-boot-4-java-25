package com.dromero202.erplite.persistence.mongo.documents;

public record ProductSpecifications(
        String processor,
        String ram,
        String storage,
        String display,
        String weight
) {}