package com.ecommerce.clothesshop.service;

import com.ecommerce.clothesshop.dto.AddToCartRequest;
import com.ecommerce.clothesshop.dto.CartItemResponse;
import com.ecommerce.clothesshop.dto.CartResponse;
import com.ecommerce.clothesshop.model.CartItem;
import com.ecommerce.clothesshop.model.ShoppingCart;
import com.ecommerce.clothesshop.repository.CartItemRepository;
import com.ecommerce.clothesshop.repository.ProductRepository;
import com.ecommerce.clothesshop.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    
    public Mono<CartResponse> getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
            .switchIfEmpty(createCart(userId))
            .flatMap(this::buildCartResponse);
    }
    
    private Mono<ShoppingCart> createCart(Long userId) {
        ShoppingCart cart = ShoppingCart.builder()
            .userId(userId)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        return cartRepository.save(cart);
    }
    
    public Mono<CartResponse> addToCart(Long userId, AddToCartRequest request) {
        return getOrCreateCart(userId)
            .flatMap(cart -> productRepository.findById(request.getProductId())
                .flatMap(product -> {
                    if (product.getStockQuantity() < request.getQuantity()) {
                        return Mono.error(new RuntimeException("Insufficient stock"));
                    }
                    
                    return cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                        .flatMap(existingItem -> {
                            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
                            return cartItemRepository.save(existingItem);
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            CartItem newItem = CartItem.builder()
                                .cartId(cart.getId())
                                .productId(product.getId())
                                .quantity(request.getQuantity())
                                .price(product.getPrice())
                                .createdAt(LocalDateTime.now())
                                .build();
                            return cartItemRepository.save(newItem);
                        }));
                })
                .then(buildCartResponse(ShoppingCart.builder()
                    .id(cart.getId())
                    .userId(userId)
                    .build()))
            );
    }
    
    public Mono<CartResponse> updateCartItem(Long userId, Long itemId, Integer quantity) {
        if (quantity <= 0) {
            return removeFromCart(userId, itemId);
        }
        
        return cartRepository.findByUserId(userId)
            .flatMap(cart -> cartItemRepository.findById(itemId)
                .flatMap(item -> {
                    if (!item.getCartId().equals(cart.getId())) {
                        return Mono.error(new RuntimeException("Cart item not found"));
                    }
                    item.setQuantity(quantity);
                    return cartItemRepository.save(item);
                })
                .then(buildCartResponse(cart))
            );
    }
    
    public Mono<CartResponse> removeFromCart(Long userId, Long itemId) {
        return cartRepository.findByUserId(userId)
            .flatMap(cart -> cartItemRepository.findById(itemId)
                .flatMap(item -> {
                    if (!item.getCartId().equals(cart.getId())) {
                        return Mono.error(new RuntimeException("Cart item not found"));
                    }
                    return cartItemRepository.deleteById(itemId)
                        .then(buildCartResponse(cart));
                })
            );
    }
    
    public Mono<Void> clearCart(Long userId) {
        return cartRepository.findByUserId(userId)
            .flatMap(cart -> cartItemRepository.deleteByCartId(cart.getId()));
    }
    
    private Mono<CartResponse> buildCartResponse(ShoppingCart cart) {
        return cartItemRepository.findByCartId(cart.getId())
            .flatMap(item -> productRepository.findById(item.getProductId())
                .map(product -> CartItemResponse.builder()
                    .id(item.getId())
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .build()
                )
            )
            .collectList()
            .map(items -> {
                BigDecimal total = items.stream()
                    .map(CartItemResponse::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                return CartResponse.builder()
                    .id(cart.getId())
                    .userId(cart.getUserId())
                    .items(items)
                    .totalAmount(total)
                    .build();
            });
    }
}