package com.valven.ecommerce.productservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU must not exceed 50 characters")
    @Column(unique = true, nullable = false, length = 50)
    private String sku;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    @Column(nullable = false)
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 10 integer digits and 2 decimal places")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Column(nullable = false)
    private Integer stock;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    @Column(length = 500)
    private String imageUrl;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    @Column(length = 100)
    private String category;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business logic methods
    public boolean isInStock() {
        return stock != null && stock > 0;
    }

    public boolean hasEnoughStock(int requestedQuantity) {
        return stock != null && stock >= requestedQuantity;
    }

    public void reduceStock(int quantity) {
        if (hasEnoughStock(quantity)) {
            this.stock -= quantity;
        } else {
            throw new IllegalArgumentException("Insufficient stock. Available: " + stock + ", Requested: " + quantity);
        }
    }

    public void addStock(int quantity) {
        if (quantity > 0) {
            this.stock += quantity;
        }
    }
}


