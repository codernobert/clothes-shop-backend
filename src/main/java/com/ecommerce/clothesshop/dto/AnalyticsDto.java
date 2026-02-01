package com.ecommerce.clothesshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AnalyticsDto {

    // Dashboard Summary
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardSummary {
        private BigDecimal totalRevenue;
        private Integer totalOrders;
        private Integer totalCustomers;
        private Integer totalProductsSold;
        private Double conversionRate;
        private BigDecimal averageOrderValue;
        private Integer pendingOrders;
        private Integer completedOrders;
    }

    // Revenue Analytics
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueAnalytics {
        private List<RevenueData> dailyRevenue;
        private List<RevenueData> weeklyRevenue;
        private List<RevenueData> monthlyRevenue;
        private BigDecimal totalRevenue;
        private BigDecimal revenueGrowth;
        private String growthTrend; // UP, DOWN, STABLE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueData {
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        private String label;
        private BigDecimal amount;
        private Integer orderCount;
        private BigDecimal averageOrderValue;
    }

    // Order Analytics
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderAnalytics {
        private Integer totalOrders;
        private Integer pendingOrders;
        private Integer processingOrders;
        private Integer confirmedOrders;
        private Integer shippedOrders;
        private Integer deliveredOrders;
        private Integer cancelledOrders;
        private Double averageProcessingTime;
        private List<OrderStatusCount> statusDistribution;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderStatusCount {
        private String status;
        private Integer count;
        private Double percentage;
    }

    // Product Analytics
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAnalytics {
        private Integer totalProducts;
        private Integer activeProducts;
        private Integer inactiveProducts;
        private Integer lowStockProducts;
        private List<ProductPerformance> topSellingProducts;
        private List<ProductPerformance> topRevenueProducts;
        private List<ProductPerformance> lowStockWarnings;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPerformance {
        private Long productId;
        private String productName;
        private String category;
        private Integer unitsSold;
        private BigDecimal revenue;
        private Integer currentStock;
        private Double profitMargin;
        private Double rating;
    }

    // Customer Analytics
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerAnalytics {
        private Integer totalCustomers;
        private Integer activeCustomers;
        private Integer newCustomersThisMonth;
        private Integer returningCustomers;
        private Double customerRetentionRate;
        private Double averageCustomerLifetimeValue;
        private List<CustomerSegment> customerSegments;
        private List<TopCustomer> topCustomers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerSegment {
        private String segment;
        private Integer count;
        private BigDecimal averageOrderValue;
        private Double purchaseFrequency;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopCustomer {
        private Long customerId;
        private String customerName;
        private Integer totalOrders;
        private BigDecimal totalSpent;
        private LocalDate lastOrderDate;
    }

    // Payment Analytics
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentAnalytics {
        private Integer totalPayments;
        private Integer successfulPayments;
        private Integer failedPayments;
        private Integer pendingPayments;
        private BigDecimal totalPaymentValue;
        private Double successRate;
        private Map<String, PaymentMethodStats> paymentMethods;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentMethodStats {
        private Integer count;
        private BigDecimal totalAmount;
        private Double percentage;
        private Double successRate;
    }

    // Geographic Analytics
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeographicAnalytics {
        private List<LocationData> ordersByLocation;
        private List<LocationData> revenueByLocation;
        private String topLocation;
        private BigDecimal topLocationRevenue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationData {
        private String location;
        private Integer orderCount;
        private BigDecimal totalRevenue;
        private Double percentage;
    }

    // Traffic & Conversion
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrafficConversionAnalytics {
        private Integer pageViews;
        private Integer uniqueVisitors;
        private Integer cartsCreated;
        private Integer ordersPlaced;
        private Double conversionRate;
        private Double cartAbandonment;
        private Double averageSessionDuration;
        private List<ChannelData> trafficByChannel;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChannelData {
        private String channel;
        private Integer visitors;
        private Double conversionRate;
        private BigDecimal revenue;
    }

    // Inventory Analytics
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryAnalytics {
        private Integer totalItems;
        private Integer itemsSold;
        private Integer itemsInStock;
        private BigDecimal inventoryValue;
        private Double stockTurnoverRate;
        private List<CategoryStock> stockByCategory;
        private List<String> lowStockAlerts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStock {
        private String category;
        private Integer totalItems;
        private Integer itemsSold;
        private Integer currentStock;
        private Double turnoverRate;
    }
}
