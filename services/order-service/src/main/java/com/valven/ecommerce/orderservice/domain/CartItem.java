package com.valven.ecommerce.orderservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

@Embeddable
public class CartItem {
    @JsonProperty("productId")
    private Long productId;
    
    @JsonProperty("productName")
    private String productName;
    
    @JsonProperty("price")
    private Double price;
    
    @JsonProperty("quantity")
    private Integer quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}


