package com.ecommerce.clothesshop.service;

import com.ecommerce.clothesshop.dto.AnalyticsDto.*;
import com.ecommerce.clothesshop.model.*;
import com.ecommerce.clothesshop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // Dashboard Summary
    public Mono<DashboardSummary> getDashboardSummary() {
        return Mono.zip(
            getTotalRevenue(),
            getTotalOrders(),
            getTotalCustomers(),
            getTotalProductsSold(),
            getOrderStatusCounts(),
            getAverageOrderValue()
        ).map(tuple -> {
            BigDecimal revenue = tuple.getT1();
            Integer orders = tuple.getT2();
            Integer customers = tuple.getT3();
            Integer productsSold = tuple.getT4();
            Map<String, Integer> statusCounts = tuple.getT5();
            BigDecimal avgOrderValue = tuple.getT6();

            Double conversionRate = customers > 0 ? (orders.doubleValue() / customers) * 100 : 0.0;

            return DashboardSummary.builder()
                .totalRevenue(revenue)
                .totalOrders(orders)
                .totalCustomers(customers)
                .totalProductsSold(productsSold)
                .conversionRate(conversionRate)
                .averageOrderValue(avgOrderValue)
                .pendingOrders(statusCounts.getOrDefault("PENDING", 0))
                .completedOrders(statusCounts.getOrDefault("DELIVERED", 0))
                .build();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    // Revenue Analytics
    public Mono<RevenueAnalytics> getRevenueAnalytics(String period) {
        return orderRepository.findAll()
            .collectList()
            .map(orders -> {
                List<RevenueData> revenueData = new ArrayList<>();

                if ("daily".equalsIgnoreCase(period)) {
                    revenueData = getLastNDaysRevenue(orders, 30);
                } else if ("weekly".equalsIgnoreCase(period)) {
                    revenueData = getWeeklyRevenue(orders);
                } else if ("monthly".equalsIgnoreCase(period)) {
                    revenueData = getMonthlyRevenue(orders);
                }

                BigDecimal totalRevenue = revenueData.stream()
                    .map(RevenueData::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal growth = calculateRevenuGrowth(revenueData);
                String trend = growth.compareTo(BigDecimal.ZERO) > 0 ? "UP" :
                              growth.compareTo(BigDecimal.ZERO) < 0 ? "DOWN" : "STABLE";

                return RevenueAnalytics.builder()
                    .dailyRevenue(period.equalsIgnoreCase("daily") ? revenueData : null)
                    .weeklyRevenue(period.equalsIgnoreCase("weekly") ? revenueData : null)
                    .monthlyRevenue(period.equalsIgnoreCase("monthly") ? revenueData : null)
                    .totalRevenue(totalRevenue)
                    .revenueGrowth(growth)
                    .growthTrend(trend)
                    .build();
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    // Order Analytics
    public Mono<OrderAnalytics> getOrderAnalytics() {
        return orderRepository.findAll()
            .collectList()
            .map(orders -> {
                Map<String, Integer> statusCounts = orders.stream()
                    .collect(Collectors.groupingBy(
                        order -> order.getStatus().toString(),
                        Collectors.summingInt(order -> 1)
                    ));

                List<OrderStatusCount> distribution = statusCounts.entrySet().stream()
                    .map(entry -> {
                        double percentage = (entry.getValue().doubleValue() / orders.size()) * 100;
                        return OrderStatusCount.builder()
                            .status(entry.getKey())
                            .count(entry.getValue())
                            .percentage(percentage)
                            .build();
                    })
                    .collect(Collectors.toList());

                double avgProcessingTime = calculateAverageProcessingTime(orders);

                return OrderAnalytics.builder()
                    .totalOrders(orders.size())
                    .pendingOrders(statusCounts.getOrDefault("PENDING", 0))
                    .processingOrders(statusCounts.getOrDefault("PROCESSING", 0))
                    .confirmedOrders(statusCounts.getOrDefault("CONFIRMED", 0))
                    .shippedOrders(statusCounts.getOrDefault("SHIPPED", 0))
                    .deliveredOrders(statusCounts.getOrDefault("DELIVERED", 0))
                    .cancelledOrders(statusCounts.getOrDefault("CANCELLED", 0))
                    .averageProcessingTime(avgProcessingTime)
                    .statusDistribution(distribution)
                    .build();
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    // Product Analytics
    public Mono<ProductAnalytics> getProductAnalytics() {
        return Mono.zip(
            productRepository.findAll().collectList(),
            orderItemRepository.findAll().collectList()
        ).map(tuple -> {
            List<Product> products = tuple.getT1();
            List<OrderItem> orderItems = tuple.getT2();

            int activeProducts = (int) products.stream().filter(Product::getIsActive).count();
            int lowStockProducts = (int) products.stream()
                .filter(p -> p.getIsActive() && p.getStockQuantity() < 10).count();

            Map<Long, Integer> unitsSoldMap = orderItems.stream()
                .collect(Collectors.groupingBy(
                    OrderItem::getProductId,
                    Collectors.summingInt(OrderItem::getQuantity)
                ));

            List<ProductPerformance> topSelling = products.stream()
                .map(p -> ProductPerformance.builder()
                    .productId(p.getId())
                    .productName(p.getName())
                    .category(p.getCategory())
                    .unitsSold(unitsSoldMap.getOrDefault(p.getId(), 0))
                    .revenue(p.getPrice().multiply(BigDecimal.valueOf(
                        unitsSoldMap.getOrDefault(p.getId(), 0))))
                    .currentStock(p.getStockQuantity())
                    .profitMargin(calculateProfitMargin(p))
                    .rating(0.0)
                    .build())
                .sorted(Comparator.comparing((ProductPerformance pp) -> pp.getUnitsSold()).reversed())
                .limit(10)
                .collect(Collectors.toList());

            List<ProductPerformance> lowStockWarnings = products.stream()
                .filter(p -> p.getStockQuantity() < 10)
                .map(p -> ProductPerformance.builder()
                    .productId(p.getId())
                    .productName(p.getName())
                    .category(p.getCategory())
                    .currentStock(p.getStockQuantity())
                    .build())
                .collect(Collectors.toList());

            return ProductAnalytics.builder()
                .totalProducts(products.size())
                .activeProducts(activeProducts)
                .inactiveProducts(products.size() - activeProducts)
                .lowStockProducts(lowStockProducts)
                .topSellingProducts(topSelling)
                .topRevenueProducts(topSelling.stream()
                    .sorted(Comparator.comparing(ProductPerformance::getRevenue).reversed())
                    .limit(5)
                    .collect(Collectors.toList()))
                .lowStockWarnings(lowStockWarnings)
                .build();
        })
        .subscribeOn(Schedulers.boundedElastic());
    }

    // Customer Analytics
    public Mono<CustomerAnalytics> getCustomerAnalytics() {
        return Mono.zip(
            userRepository.findAll().collectList(),
            orderRepository.findAll().collectList()
        ).map(tuple -> {
            List<User> users = tuple.getT1();
            List<Order> orders = tuple.getT2();

            LocalDate monthAgo = LocalDate.now().minusMonths(1);
            int newCustomers = (int) orders.stream()
                .filter(o -> o.getCreatedAt().toLocalDate().isAfter(monthAgo))
                .map(Order::getUserId)
                .distinct()
                .count();

            Map<Long, Integer> customerOrderCounts = orders.stream()
                .collect(Collectors.groupingBy(
                    Order::getUserId,
                    Collectors.summingInt(o -> 1)
                ));

            int returningCustomers = (int) customerOrderCounts.values().stream()
                .filter(count -> count > 1)
                .count();

            double retentionRate = users.size() > 0 ?
                ((double) returningCustomers / users.size()) * 100 : 0.0;

            BigDecimal totalSpent = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal avgLifetimeValue = users.size() > 0 ?
                totalSpent.divide(BigDecimal.valueOf(users.size()), 2, java.math.RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

            return CustomerAnalytics.builder()
                .totalCustomers(users.size())
                .activeCustomers(customerOrderCounts.size())
                .newCustomersThisMonth(newCustomers)
                .returningCustomers(returningCustomers)
                .customerRetentionRate(retentionRate)
                .averageCustomerLifetimeValue(avgLifetimeValue.doubleValue())
                .build();
        })
        .subscribeOn(Schedulers.boundedElastic());
    }

    // Payment Analytics
    public Mono<PaymentAnalytics> getPaymentAnalytics() {
        return orderRepository.findAll()
            .collectList()
            .map(orders -> {
                Map<String, Integer> paymentStatusCounts = orders.stream()
                    .collect(Collectors.groupingBy(
                        order -> order.getPaymentStatus().toString(),
                        Collectors.summingInt(order -> 1)
                    ));

                Map<String, Integer> methodCounts = orders.stream()
                    .collect(Collectors.groupingBy(
                        order -> order.getPaymentMethod(),
                        Collectors.summingInt(order -> 1)
                    ));

                BigDecimal totalPaymentValue = orders.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                int successful = paymentStatusCounts.getOrDefault("COMPLETED", 0);
                double successRate = orders.size() > 0 ?
                    ((double) successful / orders.size()) * 100 : 0.0;

                Map<String, PaymentMethodStats> methodStats = methodCounts.entrySet().stream()
                    .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            BigDecimal methodTotal = orders.stream()
                                .filter(o -> o.getPaymentMethod().equals(entry.getKey()))
                                .map(Order::getTotalAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                            return PaymentMethodStats.builder()
                                .count(entry.getValue())
                                .totalAmount(methodTotal)
                                .percentage((entry.getValue().doubleValue() / orders.size()) * 100)
                                .successRate(successRate)
                                .build();
                        }
                    ));

                return PaymentAnalytics.builder()
                    .totalPayments(orders.size())
                    .successfulPayments(successful)
                    .failedPayments(paymentStatusCounts.getOrDefault("FAILED", 0))
                    .pendingPayments(paymentStatusCounts.getOrDefault("PENDING", 0))
                    .totalPaymentValue(totalPaymentValue)
                    .successRate(successRate)
                    .paymentMethods(methodStats)
                    .build();
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    // Helper methods
    private Mono<BigDecimal> getTotalRevenue() {
        return orderRepository.findAll()
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Mono<Integer> getTotalOrders() {
        return orderRepository.findAll()
            .count()
            .map(Math::toIntExact);
    }

    private Mono<Integer> getTotalCustomers() {
        return userRepository.findAll()
            .count()
            .map(Math::toIntExact);
    }

    private Mono<Integer> getTotalProductsSold() {
        return orderItemRepository.findAll()
            .map(OrderItem::getQuantity)
            .reduce(0, Integer::sum)
            .map(total -> total);
    }

    private Mono<Map<String, Integer>> getOrderStatusCounts() {
        return orderRepository.findAll()
            .collectList()
            .map(orders -> orders.stream()
                .collect(Collectors.groupingBy(
                    order -> order.getStatus().toString(),
                    Collectors.summingInt(order -> 1)
                ))
            );
    }

    private Mono<BigDecimal> getAverageOrderValue() {
        return Mono.zip(
            getTotalRevenue(),
            getTotalOrders()
        ).map(tuple -> {
            BigDecimal revenue = tuple.getT1();
            Integer orders = tuple.getT2();
            return orders > 0 ? revenue.divide(BigDecimal.valueOf(orders), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
        });
    }

    private List<RevenueData> getLastNDaysRevenue(List<Order> orders, int days) {
        Map<LocalDate, BigDecimal> revenueMap = new LinkedHashMap<>();
        LocalDate startDate = LocalDate.now().minusDays(days);

        for (int i = 0; i < days; i++) {
            revenueMap.put(startDate.plusDays(i), BigDecimal.ZERO);
        }

        orders.forEach(order -> {
            LocalDate date = order.getCreatedAt().toLocalDate();
            if (!date.isBefore(startDate)) {
                revenueMap.put(date, revenueMap.getOrDefault(date, BigDecimal.ZERO).add(order.getTotalAmount()));
            }
        });

        return revenueMap.entrySet().stream()
            .map(entry -> RevenueData.builder()
                .date(entry.getKey())
                .label(entry.getKey().toString())
                .amount(entry.getValue())
                .build())
            .collect(Collectors.toList());
    }

    private List<RevenueData> getWeeklyRevenue(List<Order> orders) {
        // Implementation for weekly revenue
        Map<Integer, BigDecimal> weekRevenueMap = new TreeMap<>();

        orders.forEach(order -> {
            int weekOfYear = order.getCreatedAt().getYear() * 100 +
                            order.getCreatedAt().toLocalDate().getDayOfWeek().getValue();
            weekRevenueMap.put(weekOfYear,
                weekRevenueMap.getOrDefault(weekOfYear, BigDecimal.ZERO).add(order.getTotalAmount()));
        });

        return weekRevenueMap.entrySet().stream()
            .map(entry -> RevenueData.builder()
                .label("Week " + entry.getKey())
                .amount(entry.getValue())
                .build())
            .collect(Collectors.toList());
    }

    private List<RevenueData> getMonthlyRevenue(List<Order> orders) {
        Map<YearMonth, BigDecimal> monthRevenueMap = new TreeMap<>();

        orders.forEach(order -> {
            YearMonth yearMonth = YearMonth.from(order.getCreatedAt());
            monthRevenueMap.put(yearMonth,
                monthRevenueMap.getOrDefault(yearMonth, BigDecimal.ZERO).add(order.getTotalAmount()));
        });

        return monthRevenueMap.entrySet().stream()
            .map(entry -> RevenueData.builder()
                .label(entry.getKey().toString())
                .amount(entry.getValue())
                .build())
            .collect(Collectors.toList());
    }

    private BigDecimal calculateRevenuGrowth(List<RevenueData> revenueData) {
        if (revenueData.size() < 2) return BigDecimal.ZERO;

        List<BigDecimal> amounts = revenueData.stream()
            .map(RevenueData::getAmount)
            .collect(Collectors.toList());

        BigDecimal previous = amounts.get(amounts.size() / 2);
        BigDecimal current = amounts.stream()
            .skip(amounts.size() / 2)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (previous.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return current.subtract(previous)
            .divide(previous, 2, java.math.RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    }

    private double calculateAverageProcessingTime(List<Order> orders) {
        return orders.stream()
            .filter(o -> o.getUpdatedAt() != null)
            .mapToLong(o -> {
                long hours = java.time.temporal.ChronoUnit.HOURS.between(
                    o.getCreatedAt(), o.getUpdatedAt());
                return hours;
            })
            .average()
            .orElse(0.0);
    }

    private double calculateProfitMargin(Product product) {
        // Assuming cost is 40% of selling price (adjust based on business logic)
        if (product.getPrice().compareTo(BigDecimal.ZERO) == 0) return 0.0;
        BigDecimal cost = product.getPrice().multiply(BigDecimal.valueOf(0.4));
        return cost.divide(product.getPrice(), 4, java.math.RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .doubleValue();
    }
}
