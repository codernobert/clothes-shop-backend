package com.ecommerce.clothesshop.controller;

import com.ecommerce.clothesshop.dto.*;
import com.ecommerce.clothesshop.model.PaymentStatus;
import com.ecommerce.clothesshop.service.OrderService;
import com.ecommerce.clothesshop.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CheckoutController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponse<OrderResponse>> checkout(@Valid @RequestBody CheckoutRequest request) {
        return orderService.createOrder(request)
            .map(order -> ApiResponse.success("Order created successfully", order))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @PostMapping("/payment-intent")
    public Mono<ApiResponse<PaymentIntentResponse>> createPaymentIntent(
        @Valid @RequestBody PaymentIntentRequest request
    ) {
        return paymentService.initializePayment(request)
            .map(paystackResponse -> {
                PaymentIntentResponse response = paymentService.toPaymentIntentResponse(paystackResponse);
                return ApiResponse.success("Payment initialized successfully", response);
            })
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @PostMapping("/verify-payment")
    public Mono<ApiResponse<Boolean>> verifyPayment(@RequestParam String reference) {
        return paymentService.verifyPaymentStatus(reference)
            .map(isSuccessful -> {
                if (isSuccessful) {
                    return ApiResponse.success("Payment verified successfully", true);
                } else {
                    return ApiResponse.<Boolean>error("Payment verification failed");
                }
            })
            .onErrorResume(e -> Mono.just(ApiResponse.<Boolean>error(e.getMessage())));
    }

    @GetMapping("/paystack/callback")
    public Mono<String> paystackCallback(@RequestParam String reference) {
        // This endpoint is called by Paystack after payment
        // You can redirect to frontend with the reference
        return paymentService.verifyPaymentStatus(reference)
            .map(success -> success ?
                "redirect:/payment-success?reference=" + reference :
                "redirect:/payment-failed?reference=" + reference);
    }

    @PostMapping("/confirm-payment/{orderId}")
    public Mono<ApiResponse<OrderResponse>> confirmPayment(
        @PathVariable Long orderId,
        @RequestParam String reference
    ) {
        return paymentService.verifyPaymentStatus(reference)
            .flatMap(isSuccessful -> {
                if (isSuccessful) {
                    return orderService.updatePaymentStatus(orderId, PaymentStatus.COMPLETED, reference)
                        .map(order -> ApiResponse.success("Payment confirmed", order));
                } else {
                    return orderService.updatePaymentStatus(orderId, PaymentStatus.FAILED, reference)
                        .then(Mono.just(ApiResponse.<OrderResponse>error("Payment failed")));
                }
            })
            .onErrorResume(e -> Mono.just(ApiResponse.<OrderResponse>error(e.getMessage())));
    }
}

