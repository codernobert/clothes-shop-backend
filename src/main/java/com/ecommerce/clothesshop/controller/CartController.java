package com.ecommerce.clothesshop.controller;

import com.ecommerce.clothesshop.dto.AddToCartRequest;
import com.ecommerce.clothesshop.dto.ApiResponse;
import com.ecommerce.clothesshop.dto.CartResponse;
import com.ecommerce.clothesshop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {
    private final CartService cartService;
    
    @GetMapping("/{userId}")
    public Mono<ApiResponse<CartResponse>> getCart(@PathVariable Long userId) {
        return cartService.getOrCreateCart(userId)
            .map(ApiResponse::success)
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @PostMapping("/{userId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponse<CartResponse>> addToCart(
        @PathVariable Long userId,
        @Valid @RequestBody AddToCartRequest request
    ) {
        return cartService.addToCart(userId, request)
            .map(cart -> ApiResponse.success("Item added to cart", cart))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @PutMapping("/{userId}/items/{itemId}")
    public Mono<ApiResponse<CartResponse>> updateCartItem(
        @PathVariable Long userId,
        @PathVariable Long itemId,
        @RequestParam Integer quantity
    ) {
        return cartService.updateCartItem(userId, itemId, quantity)
            .map(cart -> ApiResponse.success("Cart updated", cart))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @DeleteMapping("/{userId}/items/{itemId}")
    public Mono<ApiResponse<CartResponse>> removeFromCart(
        @PathVariable Long userId,
        @PathVariable Long itemId
    ) {
        return cartService.removeFromCart(userId, itemId)
            .map(cart -> ApiResponse.success("Item removed from cart", cart))
            .onErrorResume(e -> Mono.just(ApiResponse.error(e.getMessage())));
    }
    
    @DeleteMapping("/{userId}")
    public Mono<ApiResponse<Void>> clearCart(@PathVariable Long userId) {
        return cartService.clearCart(userId)
            .then(Mono.just(ApiResponse.<Void>success("Cart cleared", null)))
            .onErrorResume(e -> Mono.just(ApiResponse.<Void>error(e.getMessage())));
    }
}