package com.valven.ecommerce.productservice.repository;

import com.valven.ecommerce.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String q);
    Product findBySku(String sku);
}


