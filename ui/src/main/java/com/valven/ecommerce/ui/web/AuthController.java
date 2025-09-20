package com.valven.ecommerce.ui.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final WebClient userClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthController() {
        this.userClient = WebClient.builder()
                .baseUrl("http://localhost:8080/api/auth") // Gateway üzerinden
                .build();
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                           @RequestParam(required = false) String success,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", success);
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String success,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", success);
        }
        return "signup";
    }

    @PostMapping("/signin")
    public String signin(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            log.info("Signin attempt for email: {}", email);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("email", email);
            requestBody.put("password", password);

            String response = userClient.post()
                    .uri("/signin")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapper.readTree(response);
            
            if (rootNode.get("success").asBoolean()) {
                JsonNode dataNode = rootNode.get("data");
                String token = dataNode.get("token").asText();
                String name = dataNode.get("name").asText();
                String userId = dataNode.get("userId").asText();

                // Store user info in session
                session.setAttribute("token", token);
                session.setAttribute("userId", userId);
                session.setAttribute("userName", name);
                session.setAttribute("userEmail", email);

                log.info("User signed in successfully: {}", email);
                return "redirect:/products?success=Welcome back, " + name + "!";
            } else {
                String errorMessage = rootNode.get("message").asText();
                log.error("Signin failed: {}", errorMessage);
                return "redirect:/auth/login?error=" + errorMessage;
            }

        } catch (Exception e) {
            log.error("Error during signin: {}", e.getMessage(), e);
            return "redirect:/auth/login?error=Login failed. Please try again.";
        }
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String name,
                        @RequestParam String email,
                        @RequestParam String password,
                        @RequestParam String confirmPassword,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            log.info("Signup attempt for email: {}", email);

            // Validate password confirmation
            if (!password.equals(confirmPassword)) {
                return "redirect:/auth/signup?error=Passwords do not match";
            }

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", name);
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("confirmPassword", confirmPassword);

            String response = userClient.post()
                    .uri("/signup")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapper.readTree(response);
            
            if (rootNode.get("success").asBoolean()) {
                JsonNode dataNode = rootNode.get("data");
                String token = dataNode.get("token").asText();
                String userId = dataNode.get("userId").asText();

                // Store user info in session
                session.setAttribute("token", token);
                session.setAttribute("userId", userId);
                session.setAttribute("userName", name);
                session.setAttribute("userEmail", email);

                log.info("User signed up successfully: {}", email);
                return "redirect:/products?success=Welcome, " + name + "! Your account has been created.";
            } else {
                String errorMessage = rootNode.get("message").asText();
                log.error("Signup failed: {}", errorMessage);
                return "redirect:/auth/signup?error=" + errorMessage;
            }

        } catch (Exception e) {
            log.error("Error during signup: {}", e.getMessage(), e);
            return "redirect:/auth/signup?error=Registration failed. Please try again.";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        log.info("User logging out");
        session.invalidate();
        return "redirect:/auth/login?success=You have been logged out successfully.";
    }
}
