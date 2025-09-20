package com.valven.ecommerce.orderservice.repository;

import com.valven.ecommerce.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);
}


