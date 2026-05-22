package com.dromero202.erplite.persistence.mongo.repositories;

import com.dromero202.erplite.persistence.mongo.documents.CatalogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CatalogRepository extends MongoRepository<CatalogDocument,String> {
}
