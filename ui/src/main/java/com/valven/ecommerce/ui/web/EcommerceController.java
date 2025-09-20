package com.valven.ecommerce.ui.web;

import com.valven.ecommerce.ui.model.Product;
import com.valven.ecommerce.ui.model.CartItem;
import com.valven.ecommerce.ui.model.Cart;
import com.valven.ecommerce.ui.model.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class EcommerceController {

    private final WebClient productClient;
    private final WebClient orderClient;

    public EcommerceController() {
        this.productClient = WebClient.builder()
                .baseUrl("http://localhost:8081/api")
                .build();
        this.orderClient = WebClient.builder()
                .baseUrl("http://localhost:8082/api")
                .build();
    }

    @GetMapping("/")
    public String home(Model model) {
        try {
            List<Product> products = productClient.get()
                    .uri("/products")
                    .retrieve()
                    .bodyToFlux(Product.class)
                    .collectList()
                    .block();
            model.addAttribute("products", products != null ? products : List.of());
        } catch (Exception e) {
            model.addAttribute("products", List.of());
            model.addAttribute("error", "Failed to load products: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/products")
    public String products(@RequestParam(required = false) String search, Model model) {
        try {
            String uri = search != null ? "/products?q=" + search : "/products";
            List<Product> products = productClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToFlux(Product.class)
                    .collectList()
                    .block();
            model.addAttribute("products", products != null ? products : List.of());
            model.addAttribute("search", search);
        } catch (Exception e) {
            model.addAttribute("products", List.of());
            model.addAttribute("error", "Failed to load products: " + e.getMessage());
        }
        return "products";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        try {
            Product product = productClient.get()
                    .uri("/products/" + id)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .block();
            model.addAttribute("product", product);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load product: " + e.getMessage());
        }
        return "product-detail";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, 
                           @RequestParam String productName,
                           @RequestParam Double price,
                           @RequestParam(defaultValue = "1") Integer quantity,
                           @RequestParam(defaultValue = "user123") String userId) {
        try {
            CartItem item = new CartItem();
            item.setProductId(productId);
            item.setProductName(productName);
            item.setPrice(price);
            item.setQuantity(quantity);

            orderClient.post()
                    .uri("/carts/" + userId + "/items")
                    .bodyValue(item)
                    .retrieve()
                    .bodyToMono(Cart.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart(@RequestParam(defaultValue = "user123") String userId, Model model) {
        // Geçici olarak basit cart döndür
        Cart emptyCart = new Cart();
        emptyCart.setUserId(userId);
        model.addAttribute("cart", emptyCart);
        model.addAttribute("userId", userId);
        return "cart-simple";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId, 
                                @RequestParam(defaultValue = "user123") String userId) {
        // Implementation for removing items
        return "redirect:/cart";
    }

    @PostMapping("/order/create")
    public String createOrder(@RequestParam(defaultValue = "user123") String userId, Model model) {
        try {
            Cart cart = orderClient.get()
                    .uri("/carts/" + userId)
                    .retrieve()
                    .bodyToMono(Cart.class)
                    .block();

            if (cart != null && !cart.getItems().isEmpty()) {
                Order order = new Order();
                order.setUserId(userId);
                order.setItems(cart.getItems());
                order.setTotalAmount(cart.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum());

                Order createdOrder = orderClient.post()
                        .uri("/orders")
                        .bodyValue(order)
                        .retrieve()
                        .bodyToMono(Order.class)
                        .block();

                model.addAttribute("order", createdOrder);
                return "order-success";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create order: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(defaultValue = "user123") String userId, Model model) {
        // Implementation for viewing orders
        return "orders";
    }
}
