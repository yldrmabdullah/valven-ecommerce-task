package com.valven.ecommerce.ui.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class HomeController {
    private final WebClient client = WebClient.create("http://localhost:8080");

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", client.get().uri("/api/products").retrieve().bodyToFlux(Object.class).collectList().block());
        return "index";
    }
}


