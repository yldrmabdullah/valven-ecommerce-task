package com.valven.ecommerce.productservice.web;

import com.valven.ecommerce.productservice.domain.Product;
import com.valven.ecommerce.productservice.dto.ApiResponse;
import com.valven.ecommerce.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minPrice", required = false) java.math.BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) java.math.BigDecimal maxPrice,
            @RequestParam(value = "inStock", required = false) Boolean inStock) {
        
        log.info("Searching products with query: {}, category: {}, price range: {}-{}, inStock: {}", 
                query, category, minPrice, maxPrice, inStock);
        
        List<Product> products;
        
        if (query != null || category != null || minPrice != null || maxPrice != null || inStock != null) {
            products = productService.findProductsWithFilters(query, category, minPrice, maxPrice, inStock);
        } else {
            products = productService.searchProducts(query);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", product));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ApiResponse<Product>> getProductBySku(@PathVariable String sku) {
        log.info("Fetching product with SKU: {}", sku);
        Product product = productService.getProductBySku(sku);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", product));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody Product product) {
        log.info("Creating new product: {}", product.getName());
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/api/products/" + savedProduct.getId()))
                .body(ApiResponse.success("Product created successfully", savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id, 
            @Valid @RequestBody Product product) {
        log.info("Updating product with id: {}", id);
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    @PostMapping("/{id}/stock/reduce")
    public ResponseEntity<ApiResponse<Void>> reduceStock(
            @PathVariable Long id, 
            @RequestBody int quantity) {
        log.info("Reducing stock for product {} by quantity {}", id, quantity);
        productService.reduceStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock reduced successfully", null));
    }

    @PostMapping("/{id}/stock/add")
    public ResponseEntity<ApiResponse<Void>> addStock(
            @PathVariable Long id, 
            @RequestBody int quantity) {
        log.info("Adding stock for product {} by quantity {}", id, quantity);
        productService.addStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock added successfully", null));
    }

    @GetMapping("/{id}/stock/check")
    public ResponseEntity<ApiResponse<Boolean>> checkStock(
            @PathVariable Long id, 
            @RequestParam int quantity) {
        log.info("Checking stock for product {} with quantity {}", id, quantity);
        boolean hasEnoughStock = productService.hasEnoughStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock check completed", hasEnoughStock));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@PathVariable String category) {
        log.info("Fetching products by category: {}", category);
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<Product>>> getLowStockProducts(
            @RequestParam(defaultValue = "5") int threshold) {
        log.info("Fetching products with stock below threshold: {}", threshold);
        List<Product> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(ApiResponse.success("Low stock products retrieved successfully", products));
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<ApiResponse<List<Product>>> getOutOfStockProducts() {
        log.info("Fetching out of stock products");
        List<Product> products = productService.getLowStockProducts(1);
        return ResponseEntity.ok(ApiResponse.success("Out of stock products retrieved successfully", products));
    }
}


