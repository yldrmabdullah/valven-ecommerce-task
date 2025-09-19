package com.valven.ecommerce.productservice.web;

import com.valven.ecommerce.productservice.ProductServiceApplication;
import com.valven.ecommerce.productservice.domain.Product;
import com.valven.ecommerce.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ProductServiceApplication.class)
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository repository;

    @Test
    void searchReturnsOk() throws Exception {
        Product p = new Product();
        p.setName("Sample");
        p.setSku("T-1");
        p.setPrice(new BigDecimal("9.99"));
        p.setStock(5);
        repository.save(p);

        mockMvc.perform(get("/api/products").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}


