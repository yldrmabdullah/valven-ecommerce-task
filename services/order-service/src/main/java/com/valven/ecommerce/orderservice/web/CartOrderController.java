package com.valven.ecommerce.orderservice.web;

import com.valven.ecommerce.orderservice.domain.Cart;
import com.valven.ecommerce.orderservice.domain.CartItem;
import com.valven.ecommerce.orderservice.domain.Order;
import com.valven.ecommerce.orderservice.repository.CartRepository;
import com.valven.ecommerce.orderservice.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class CartOrderController {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public CartOrderController(CartRepository cartRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/carts/{userId}/items")
    public ResponseEntity<Cart> addItem(@PathVariable("userId") String userId, @RequestBody CartItem item) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart c = new Cart();
            c.setUserId(userId);
            return c;
        });
        cart.getItems().add(item);
        Cart saved = cartRepository.save(cart);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/carts/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable("userId") String userId) {
        return cartRepository.findByUserId(userId).map(ResponseEntity::ok).orElse(ResponseEntity.ok(new Cart()));
    }

    @DeleteMapping("/carts/{userId}/items/{productId}")
    public ResponseEntity<Cart> removeItem(@PathVariable("userId") String userId, @PathVariable("productId") Long productId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getProductId().equals(productId));
            cart = cartRepository.save(cart);
        }
        return ResponseEntity.ok(cart != null ? cart : new Cart());
    }

    @DeleteMapping("/carts/{userId}")
    public ResponseEntity<Cart> clearCart(@PathVariable("userId") String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart != null) {
            cart.getItems().clear();
            cart = cartRepository.save(cart);
        }
        return ResponseEntity.ok(cart != null ? cart : new Cart());
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order saved = orderRepository.save(order);
        return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
    }

    @GetMapping("/orders")
    public ResponseEntity<java.util.List<Order>> getAllOrders(@RequestParam(defaultValue = "user123") String userId) {
        java.util.List<Order> orders = orderRepository.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}


