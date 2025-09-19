package com.valven.ecommerce.orderservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valven.ecommerce.orderservice.OrderServiceApplication;
import com.valven.ecommerce.orderservice.domain.CartItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OrderServiceApplication.class)
@AutoConfigureMockMvc
class CartOrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addItemAndFetchCart() throws Exception {
        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setQuantity(2);
        mockMvc.perform(post("/api/carts/u1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/carts/u1"))
                .andExpect(status().isOk());
    }
}


