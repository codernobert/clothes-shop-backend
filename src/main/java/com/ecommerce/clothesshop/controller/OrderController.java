package com.ecommerce.clothesshop.controller;

import com.ecommerce.clothesshop.dto.ApiResponse;
import com.ecommerce.clothesshop.dto.OrderResponse;
import com.ecommerce.clothesshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;
    
    @GetMapping("/{orderId}")
    public Mono<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
            .map(ApiResponse::success)
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @GetMapping("/number/{orderNumber}")
    public Mono<ApiResponse<OrderResponse>> getOrderByNumber(@PathVariable String orderNumber) {
        return orderService.getOrderByNumber(orderNumber)
            .map(ApiResponse::success)
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @GetMapping("/user/{userId}")
    public Flux<OrderResponse> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }
}