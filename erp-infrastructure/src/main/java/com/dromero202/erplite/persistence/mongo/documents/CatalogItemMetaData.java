package com.dromero202.erplite.persistence.mongo.documents;

import java.math.BigDecimal;
import java.util.List;

public record CatalogItemMetaData(
        String item,
        String color,
        List<String> nextStatusses,
        BigDecimal fee
) {
}
