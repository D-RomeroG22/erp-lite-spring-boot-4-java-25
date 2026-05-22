package com.dromero202.erplite.persistence.mongo.documents;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "audit_logs")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDocument {

    @Id
    private ObjectId id;           // _id como ObjectId ($oid)
    private String className;
    private String endpoint;
    private String errorMessage;   // nullable
    private Long executionTimeMs;
    private String ipAddress;
    private String methodName;
    private boolean success;
    private Instant timestamp;
    private String userId;
}