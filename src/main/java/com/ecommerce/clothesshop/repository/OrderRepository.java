package com.ecommerce.clothesshop.repository;

import com.ecommerce.clothesshop.model.Order;
import com.ecommerce.clothesshop.model.OrderStatus;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository extends R2dbcRepository<Order, Long> {
    Flux<Order> findByUserId(Long userId);
    Mono<Order> findByOrderNumber(String orderNumber);
    Flux<Order> findByStatus(OrderStatus status);
}
