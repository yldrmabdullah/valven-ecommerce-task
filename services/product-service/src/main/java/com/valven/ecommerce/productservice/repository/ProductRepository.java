package com.valven.ecommerce.productservice.repository;

import com.valven.ecommerce.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Search methods
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
    List<Product> findByCategoryIgnoreCase(String category);
    
    // SKU methods
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
    
    // Stock methods
    List<Product> findByStockLessThan(Integer stock);
    List<Product> findByStockGreaterThan(Integer stock);
    List<Product> findByStockBetween(Integer minStock, Integer maxStock);
    
    // Price methods
    List<Product> findByPriceBetween(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice);
    List<Product> findByPriceLessThan(java.math.BigDecimal price);
    List<Product> findByPriceGreaterThan(java.math.BigDecimal price);
    
    // Custom queries
    @Query("SELECT p FROM Product p WHERE p.stock = 0")
    List<Product> findOutOfStockProducts();
    
    @Query("SELECT p FROM Product p WHERE p.stock > 0 ORDER BY p.createdAt DESC")
    List<Product> findAvailableProductsOrderByCreatedDate();
    
    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:category IS NULL OR LOWER(p.category) = LOWER(:category)) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:inStock IS NULL OR (:inStock = true AND p.stock > 0) OR (:inStock = false AND p.stock = 0))")
    List<Product> findProductsWithFilters(
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") java.math.BigDecimal minPrice,
            @Param("maxPrice") java.math.BigDecimal maxPrice,
            @Param("inStock") Boolean inStock
    );
}


