package com.dromero202.erplite.persistence.mongo.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "catalogs")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDocument {
    @Id
    private String id;
    private String name;
    private String description;
    private CatalogType catalogType;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean active;
    private List<CatalogItem> items;
}