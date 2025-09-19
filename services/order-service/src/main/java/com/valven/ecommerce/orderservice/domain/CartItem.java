package com.valven.ecommerce.orderservice.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class CartItem {
    private Long productId;
    private Integer quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}


