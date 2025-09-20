package com.valven.ecommerce.ui.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String category;
    private String imageUrl;

    // Business logic methods
    public boolean isInStock() {
        return stock != null && stock > 0;
    }

    public boolean hasEnoughStock(int requestedQuantity) {
        return stock != null && stock >= requestedQuantity;
    }

    public String getStockStatus() {
        if (stock == null || stock == 0) {
            return "Out of Stock";
        } else if (stock <= 5) {
            return "Low Stock (" + stock + " left)";
        } else {
            return "In Stock (" + stock + " available)";
        }
    }

    public String getStockStatusClass() {
        if (stock == null || stock == 0) {
            return "out-of-stock";
        } else if (stock <= 5) {
            return "low-stock";
        } else {
            return "in-stock";
        }
    }
}
