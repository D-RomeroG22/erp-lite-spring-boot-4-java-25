package com.dromero202.erplite.persistence.jpa.repositories;

import com.dromero202.erplite.persistence.jpa.entities.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, UUID> {
}
