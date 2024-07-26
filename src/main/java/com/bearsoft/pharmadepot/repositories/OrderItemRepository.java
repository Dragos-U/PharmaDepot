package com.bearsoft.pharmadepot.repositories;

import com.bearsoft.pharmadepot.models.domain.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
