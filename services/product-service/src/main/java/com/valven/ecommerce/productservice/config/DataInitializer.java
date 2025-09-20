package com.valven.ecommerce.productservice.config;

import com.valven.ecommerce.productservice.domain.Product;
import com.valven.ecommerce.productservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            Product laptop = new Product();
            laptop.setSku("LAP001");
            laptop.setName("MacBook Pro 16-inch");
            laptop.setDescription("Apple MacBook Pro with M2 chip, 16GB RAM, 512GB SSD");
            laptop.setPrice(new BigDecimal("2499.99"));
            laptop.setStock(10);
            productRepository.save(laptop);

            Product phone = new Product();
            phone.setSku("PHN001");
            phone.setName("iPhone 15 Pro");
            phone.setDescription("Apple iPhone 15 Pro with A17 Pro chip, 128GB storage");
            phone.setPrice(new BigDecimal("999.99"));
            phone.setStock(25);
            productRepository.save(phone);

            Product headphones = new Product();
            headphones.setSku("AUD001");
            headphones.setName("Sony WH-1000XM5");
            headphones.setDescription("Noise-canceling wireless headphones with 30-hour battery life");
            headphones.setPrice(new BigDecimal("399.99"));
            headphones.setStock(15);
            productRepository.save(headphones);

            Product tablet = new Product();
            tablet.setSku("TAB001");
            tablet.setName("iPad Air");
            tablet.setDescription("Apple iPad Air with M1 chip, 10.9-inch display, 64GB storage");
            tablet.setPrice(new BigDecimal("599.99"));
            tablet.setStock(20);
            productRepository.save(tablet);

            Product watch = new Product();
            watch.setSku("WAT001");
            watch.setName("Apple Watch Series 9");
            watch.setDescription("Apple Watch Series 9 with GPS, 45mm case, aluminum");
            watch.setPrice(new BigDecimal("429.99"));
            watch.setStock(30);
            productRepository.save(watch);

            Product keyboard = new Product();
            keyboard.setSku("KEY001");
            keyboard.setName("Mechanical Keyboard");
            keyboard.setDescription("RGB mechanical keyboard with Cherry MX switches");
            keyboard.setPrice(new BigDecimal("149.99"));
            keyboard.setStock(50);
            productRepository.save(keyboard);
        }
    }
}
