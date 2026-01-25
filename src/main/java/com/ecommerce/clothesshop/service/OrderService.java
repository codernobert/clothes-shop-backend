package com.ecommerce.clothesshop.service;

import com.ecommerce.clothesshop.dto.CheckoutRequest;
import com.ecommerce.clothesshop.dto.OrderItemResponse;
import com.ecommerce.clothesshop.dto.OrderResponse;
import com.ecommerce.clothesshop.model.*;
import com.ecommerce.clothesshop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    
    public Mono<OrderResponse> createOrder(CheckoutRequest request) {
        return cartRepository.findByUserId(request.getUserId())
            .flatMap(cart -> cartItemRepository.findByCartId(cart.getId())
                .collectList()
                .flatMap(cartItems -> {
                    if (cartItems.isEmpty()) {
                        return Mono.error(new RuntimeException("Cart is empty"));
                    }
                    
                    // Calculate total
                    BigDecimal totalAmount = cartItems.stream()
                        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    // Create order
                    Order order = Order.builder()
                        .userId(request.getUserId())
                        .orderNumber(generateOrderNumber())
                        .totalAmount(totalAmount)
                        .status(OrderStatus.PENDING)
                        .paymentMethod(request.getPaymentMethod())
                        .paymentStatus(PaymentStatus.PENDING)
                        .shippingAddress(request.getShippingAddress())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                    
                    return orderRepository.save(order)
                        .flatMap(savedOrder -> {
                            // Create order items and update stock
                            return Flux.fromIterable(cartItems)
                                .flatMap(cartItem -> 
                                    productRepository.findById(cartItem.getProductId())
                                        .flatMap(product -> {
                                            OrderItem orderItem = OrderItem.builder()
                                                .orderId(savedOrder.getId())
                                                .productId(product.getId())
                                                .productName(product.getName())
                                                .quantity(cartItem.getQuantity())
                                                .price(cartItem.getPrice())
                                                .createdAt(LocalDateTime.now())
                                                .build();
                                            
                                            return orderItemRepository.save(orderItem)
                                                .then(productService.updateStock(product.getId(), cartItem.getQuantity()));
                                        })
                                )
                                .then(cartItemRepository.deleteByCartId(cart.getId()))
                                .then(buildOrderResponse(savedOrder));
                        });
                })
            )
            .switchIfEmpty(Mono.error(new RuntimeException("Cart not found")));
    }
    
    public Mono<OrderResponse> getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .flatMap(this::buildOrderResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Order not found")));
    }
    
    public Mono<OrderResponse> getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
            .flatMap(this::buildOrderResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Order not found")));
    }
    
    public Flux<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
            .flatMap(this::buildOrderResponse);
    }
    
    public Flux<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
            .flatMap(this::buildOrderResponse);
    }
    
    public Mono<OrderResponse> updateOrderStatus(Long orderId, OrderStatus status) {
        return orderRepository.findById(orderId)
            .flatMap(order -> {
                order.setStatus(status);
                order.setUpdatedAt(LocalDateTime.now());
                return orderRepository.save(order);
            })
            .flatMap(this::buildOrderResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Order not found")));
    }
    
    public Mono<OrderResponse> updatePaymentStatus(Long orderId, PaymentStatus status, String paymentIntentId) {
        return orderRepository.findById(orderId)
            .flatMap(order -> {
                order.setPaymentStatus(status);
                order.setPaymentIntentId(paymentIntentId);
                if (status == PaymentStatus.COMPLETED) {
                    order.setStatus(OrderStatus.CONFIRMED);
                }
                order.setUpdatedAt(LocalDateTime.now());
                return orderRepository.save(order);
            })
            .flatMap(this::buildOrderResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Order not found")));
    }
    
    public Mono<OrderResponse> cancelOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .flatMap(order -> {
                if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
                    return Mono.error(new RuntimeException("Cannot cancel shipped or delivered orders"));
                }
                order.setStatus(OrderStatus.CANCELLED);
                order.setUpdatedAt(LocalDateTime.now());
                return orderRepository.save(order);
            })
            .flatMap(this::buildOrderResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Order not found")));
    }
    
    private Mono<OrderResponse> buildOrderResponse(Order order) {
        return orderItemRepository.findByOrderId(order.getId())
            .map(item -> OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build()
            )
            .collectList()
            .map(items -> OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus().name())
                .shippingAddress(order.getShippingAddress())
                .items(items)
                .build()
            );
    }
    
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}