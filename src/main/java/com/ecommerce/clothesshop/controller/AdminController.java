package com.ecommerce.clothesshop.controller;

import com.ecommerce.clothesshop.dto.ApiResponse;
import com.ecommerce.clothesshop.dto.OrderResponse;
import com.ecommerce.clothesshop.dto.ProductRequest;
import com.ecommerce.clothesshop.dto.ProductResponse;
import com.ecommerce.clothesshop.model.OrderStatus;
import com.ecommerce.clothesshop.service.OrderService;
import com.ecommerce.clothesshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {
    private final ProductService productService;
    private final OrderService orderService;
    
    // Product Management
    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        return productService.createProduct(request)
            .map(product -> ApiResponse.success("Product created successfully", product))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @PutMapping("/products/{id}")
    public Mono<ApiResponse<ProductResponse>> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody ProductRequest request
    ) {
        return productService.updateProduct(id, request)
            .map(product -> ApiResponse.success("Product updated successfully", product))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @DeleteMapping("/products/{id}")
    public Mono<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id)
            .then(Mono.just(ApiResponse.<Void>success("Product deleted successfully", null)))
            .onErrorResume(e -> Mono.just(ApiResponse.<Void>error(e.getMessage())));
    }
    
    @PatchMapping("/products/{id}/stock")
    public Mono<ApiResponse<ProductResponse>> updateStock(
        @PathVariable Long id,
        @RequestParam Integer quantity
    ) {
        return productService.updateStock(id, quantity)
            .map(product -> ApiResponse.success("Stock updated", product))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    // Order Management
    @GetMapping("/orders")
    public Flux<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    @PatchMapping("/orders/{orderId}/status")
    public Mono<ApiResponse<OrderResponse>> updateOrderStatus(
        @PathVariable Long orderId,
        @RequestParam OrderStatus status
    ) {
        return orderService.updateOrderStatus(orderId, status)
            .map(order -> ApiResponse.success("Order status updated", order))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @PostMapping("/orders/{orderId}/cancel")
    public Mono<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId)
            .map(order -> ApiResponse.success("Order cancelled", order))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
}