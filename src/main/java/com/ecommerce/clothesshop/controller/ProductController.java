package com.ecommerce.clothesshop.controller;

import com.ecommerce.clothesshop.dto.ApiResponse;
import com.ecommerce.clothesshop.dto.ProductRequest;
import com.ecommerce.clothesshop.dto.ProductResponse;
import com.ecommerce.clothesshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;
    
    @GetMapping
    public Flux<ProductResponse> getAllProducts() {
        return productService.getAllActiveProducts();
    }
    
    @GetMapping("/{id}")
    public Mono<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
            .map(ApiResponse::success)
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @GetMapping("/search")
    public Flux<ProductResponse> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }
    
    @GetMapping("/filter")
    public Flux<ProductResponse> filterProducts(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) String gender
    ) {
        return productService.filterProducts(category, brand, minPrice, maxPrice, gender);
    }
    
    @GetMapping("/category/{category}")
    public Flux<ProductResponse> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }
}