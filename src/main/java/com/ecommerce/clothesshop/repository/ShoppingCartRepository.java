package com.ecommerce.clothesshop.repository;

import com.ecommerce.clothesshop.model.ShoppingCart;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ShoppingCartRepository extends R2dbcRepository<ShoppingCart, Long> {
    Mono<ShoppingCart> findByUserId(Long userId);
}
