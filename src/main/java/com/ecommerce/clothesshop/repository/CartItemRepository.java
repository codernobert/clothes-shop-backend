package com.ecommerce.clothesshop.repository;

import com.ecommerce.clothesshop.model.CartItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartItemRepository extends R2dbcRepository<CartItem, Long> {
    Flux<CartItem> findByCartId(Long cartId);
    Mono<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    Mono<Void> deleteByCartId(Long cartId);
}
