package com.valven.ecommerce.productservice.exception;

public class InsufficientStockException extends RuntimeException {
    private final Long productId;
    private final int availableStock;
    private final int requestedQuantity;

    public InsufficientStockException(Long productId, int availableStock, int requestedQuantity) {
        super(String.format("Insufficient stock for product %d. Available: %d, Requested: %d", 
                productId, availableStock, requestedQuantity));
        this.productId = productId;
        this.availableStock = availableStock;
        this.requestedQuantity = requestedQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }
}
