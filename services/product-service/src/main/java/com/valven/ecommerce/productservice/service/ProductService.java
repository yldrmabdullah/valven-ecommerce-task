package com.valven.ecommerce.productservice.service;

import com.valven.ecommerce.productservice.domain.Product;
import com.valven.ecommerce.productservice.dto.ApiResponse;
import com.valven.ecommerce.productservice.exception.InsufficientStockException;
import com.valven.ecommerce.productservice.exception.ProductNotFoundException;
import com.valven.ecommerce.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String query) {
        log.info("Searching products with query: {}", query);
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameContainingIgnoreCase(query.trim());
    }

    public Product getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product getProductBySku(String sku) {
        log.info("Fetching product with sku: {}", sku);
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with SKU: " + sku));
    }

    @Transactional
    public Product createProduct(Product product) {
        log.info("Creating new product: {}", product.getName());
        
        // Check if SKU already exists
        if (productRepository.findBySku(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("Product with SKU " + product.getSku() + " already exists");
        }
        
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());
        return savedProduct;
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        log.info("Updating product with id: {}", id);
        
        Product existingProduct = getProductById(id);
        
        // Check if SKU is being changed and if new SKU already exists
        if (!existingProduct.getSku().equals(updatedProduct.getSku())) {
            if (productRepository.findBySku(updatedProduct.getSku()).isPresent()) {
                throw new IllegalArgumentException("Product with SKU " + updatedProduct.getSku() + " already exists");
            }
        }
        
        existingProduct.setSku(updatedProduct.getSku());
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        existingProduct.setCategory(updatedProduct.getCategory());
        
        Product savedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully with id: {}", savedProduct.getId());
        return savedProduct;
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        Product product = getProductById(id);
        productRepository.delete(product);
        log.info("Product deleted successfully with id: {}", id);
    }

    @Transactional
    public void reduceStock(Long productId, int quantity) {
        log.info("Reducing stock for product {} by quantity {}", productId, quantity);
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        Product product = getProductById(productId);
        
        if (!product.hasEnoughStock(quantity)) {
            throw new InsufficientStockException(productId, product.getStock(), quantity);
        }
        
        product.reduceStock(quantity);
        productRepository.save(product);
        log.info("Stock reduced successfully. New stock: {}", product.getStock());
    }

    @Transactional
    public void addStock(Long productId, int quantity) {
        log.info("Adding stock for product {} by quantity {}", productId, quantity);
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        Product product = getProductById(productId);
        product.addStock(quantity);
        productRepository.save(product);
        log.info("Stock added successfully. New stock: {}", product.getStock());
    }

    public boolean isProductInStock(Long productId) {
        Product product = getProductById(productId);
        return product.isInStock();
    }

    public boolean hasEnoughStock(Long productId, int quantity) {
        Product product = getProductById(productId);
        return product.hasEnoughStock(quantity);
    }

    public List<Product> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        return productRepository.findByCategoryIgnoreCase(category);
    }

    public List<Product> getLowStockProducts(int threshold) {
        log.info("Fetching products with stock below threshold: {}", threshold);
        return productRepository.findByStockLessThan(threshold);
    }

    public List<Product> findProductsWithFilters(String name, String category, 
                                                java.math.BigDecimal minPrice, 
                                                java.math.BigDecimal maxPrice, 
                                                Boolean inStock) {
        log.info("Finding products with filters - name: {}, category: {}, price range: {}-{}, inStock: {}", 
                name, category, minPrice, maxPrice, inStock);
        return productRepository.findProductsWithFilters(name, category, minPrice, maxPrice, inStock);
    }
}
