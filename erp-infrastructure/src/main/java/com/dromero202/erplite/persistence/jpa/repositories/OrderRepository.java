package com.dromero202.erplite.persistence.jpa.repositories;

import com.dromero202.erplite.persistence.jpa.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
