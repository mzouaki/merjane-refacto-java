package com.nimbleways.springboilerplate.repository;

import com.nimbleways.springboilerplate.repository.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// no need to define any methods here, JpaRepository provides basic CRUD operations
// change primary key type to Long
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
