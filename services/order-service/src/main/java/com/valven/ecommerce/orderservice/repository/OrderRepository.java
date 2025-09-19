package com.valven.ecommerce.orderservice.repository;

import com.valven.ecommerce.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}


