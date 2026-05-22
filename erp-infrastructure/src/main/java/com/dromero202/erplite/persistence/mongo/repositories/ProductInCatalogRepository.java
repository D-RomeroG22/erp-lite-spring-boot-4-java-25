package com.dromero202.erplite.persistence.mongo.repositories;

import com.dromero202.erplite.persistence.mongo.documents.ProductInCatalogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductInCatalogRepository extends MongoRepository<ProductInCatalogDocument, String> {
}
