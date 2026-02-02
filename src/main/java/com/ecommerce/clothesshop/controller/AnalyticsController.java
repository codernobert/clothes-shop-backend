package com.ecommerce.clothesshop.controller;

import com.ecommerce.clothesshop.dto.AnalyticsDto.*;
import com.ecommerce.clothesshop.dto.ApiResponse;
import com.ecommerce.clothesshop.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/admin/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    /**
     * Get dashboard summary with key metrics
     */
    @GetMapping("/dashboard")
    public Mono<ApiResponse<DashboardSummary>> getDashboardSummary() {
        return analyticsService.getDashboardSummary()
            .map(data -> ApiResponse.success("Dashboard summary retrieved", data))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }

    /**
     * Get revenue analytics for specified period
     * @param period daily, weekly, or monthly
     */
    @GetMapping("/revenue")
    public Mono<ApiResponse<RevenueAnalytics>> getRevenueAnalytics(
        @RequestParam(defaultValue = "daily") String period
    ) {
        return analyticsService.getRevenueAnalytics(period)
            .map(data -> ApiResponse.success("Revenue analytics retrieved", data))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }

    /**
     * Get order analytics and status distribution
     */
    @GetMapping("/orders")
    public Mono<ApiResponse<OrderAnalytics>> getOrderAnalytics() {
        return analyticsService.getOrderAnalytics()
            .map(data -> ApiResponse.success("Order analytics retrieved", data))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }

    /**
     * Get product analytics including top sellers and low stock warnings
     */
    @GetMapping("/products")
    public Mono<ApiResponse<ProductAnalytics>> getProductAnalytics() {
        return analyticsService.getProductAnalytics()
            .map(data -> ApiResponse.success("Product analytics retrieved", data))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }

    /**
     * Get customer analytics including retention and segments
     */
    @GetMapping("/customers")
    public Mono<ApiResponse<CustomerAnalytics>> getCustomerAnalytics() {
        return analyticsService.getCustomerAnalytics()
            .map(data -> ApiResponse.success("Customer analytics retrieved", data))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }

    /**
     * Get payment analytics including success rates and methods
     */
    @GetMapping("/payments")
    public Mono<ApiResponse<PaymentAnalytics>> getPaymentAnalytics() {
        return analyticsService.getPaymentAnalytics()
            .map(data -> ApiResponse.success("Payment analytics retrieved", data))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
}
