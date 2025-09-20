package com.valven.ecommerce.productservice.config;

import com.valven.ecommerce.productservice.domain.Product;
import com.valven.ecommerce.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            log.info("Initializing sample product data...");
            
            // Electronics
            createProduct("LAP001", "MacBook Pro 16-inch", 
                "Apple MacBook Pro with M2 chip, 16GB RAM, 512GB SSD", 
                new BigDecimal("2499.99"), 10, "Electronics", 
                "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500");
            
            createProduct("PHN001", "iPhone 15 Pro", 
                "Apple iPhone 15 Pro with A17 Pro chip, 128GB storage", 
                new BigDecimal("999.99"), 25, "Electronics", 
                "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=500");
            
            createProduct("AUD001", "Sony WH-1000XM5", 
                "Noise-canceling wireless headphones with 30-hour battery life", 
                new BigDecimal("399.99"), 15, "Electronics", 
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500");
            
            createProduct("TAB001", "iPad Air", 
                "Apple iPad Air with M1 chip, 10.9-inch display, 64GB storage", 
                new BigDecimal("599.99"), 8, "Electronics", 
                "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=500");
            
            // Clothing
            createProduct("TSH001", "Cotton T-Shirt", 
                "100% cotton comfortable t-shirt in various colors", 
                new BigDecimal("29.99"), 50, "Clothing", 
                "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500");
            
            createProduct("JKT001", "Denim Jacket", 
                "Classic blue denim jacket with vintage wash", 
                new BigDecimal("89.99"), 20, "Clothing", 
                "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=500");
            
            // Books
            createProduct("BOK001", "Clean Code", 
                "A Handbook of Agile Software Craftsmanship by Robert C. Martin", 
                new BigDecimal("39.99"), 30, "Books", 
                "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=500");
            
            createProduct("BOK002", "Design Patterns", 
                "Elements of Reusable Object-Oriented Software by Gang of Four", 
                new BigDecimal("49.99"), 12, "Books", 
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500");
            
            // Home & Garden
            createProduct("HOM001", "Coffee Maker", 
                "Programmable drip coffee maker with 12-cup capacity", 
                new BigDecimal("79.99"), 5, "Home & Garden", 
                "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=500");
            
            createProduct("HOM002", "Plant Pot Set", 
                "Set of 3 ceramic plant pots in different sizes", 
                new BigDecimal("34.99"), 18, "Home & Garden", 
                "https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=500");
            
            // Sports
            createProduct("SPT001", "Yoga Mat", 
                "Non-slip yoga mat with carrying strap", 
                new BigDecimal("24.99"), 25, "Sports", 
                "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=500");
            
            createProduct("SPT002", "Running Shoes", 
                "Lightweight running shoes with breathable mesh upper", 
                new BigDecimal("129.99"), 0, "Sports", 
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500");
            
            log.info("Sample product data initialized successfully with {} products", productRepository.count());
        } else {
            log.info("Product data already exists, skipping initialization");
        }
    }
    
    private void createProduct(String sku, String name, String description, 
                             BigDecimal price, Integer stock, String category, String imageUrl) {
        Product product = new Product();
        product.setSku(sku);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        productRepository.save(product);
    }
}