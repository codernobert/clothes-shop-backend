# 📋 Admin Analytics Dashboard - Architecture & Flow Diagram

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         ADMIN ANALYTICS DASHBOARD                           │
│                         (admin-analytics-dashboard.html)                    │
│                                                                              │
│  ┌────────────────────────────────────────────────────────────────────────┐ │
│  │  Browser UI Layer                                                      │ │
│  │  ┌──────────────────────────────────────────────────────────────────┐ │ │
│  │  │ Navigation Sidebar      │  Main Content Area                    │ │ │
│  │  │                         │                                       │ │ │
│  │  │ • Dashboard      [📊]   │  [Dashboard] [Revenue] [Orders]     │ │ │
│  │  │ • Revenue        [💰]   │  [Products] [Customers] [Payments] │ │ │
│  │  │ • Orders         [📦]   │                                     │ │ │
│  │  │ • Products       [🏪]   │  ┌─────────────────────────────────┐ │ │
│  │  │ • Customers      [👥]   │  │  KPI Cards (6 metrics)          │ │ │
│  │  │ • Payments       [💳]   │  │                                 │ │ │
│  │  │ • Reports        [📋]   │  ├─────────────────────────────────┤ │ │
│  │  │                         │  │  Charts (4 visualizations)      │ │ │
│  │  │                         │  │                                 │ │ │
│  │  │                         │  ├─────────────────────────────────┤ │ │
│  │  │                         │  │  Data Tables (with status)      │ │ │
│  │  │                         │  │                                 │ │ │
│  │  │                         │  └─────────────────────────────────┘ │ │
│  │  └──────────────────────────────────────────────────────────────────┘ │ │
│  │  JavaScript Layer (Chart.js, Fetch API)                              │ │
│  └────────────────────────────────────────────────────────────────────────┘ │
│                                   ↓                                          │
│                           HTTP/REST Calls                                    │
└─────────────────────────────────────────────────────────────────────────────┘
                                   ↓
          ┌────────────────────────────────────────────────┐
          │         SPRING BOOT BACKEND (Port 8080)        │
          │                                                │
          │  ┌──────────────────────────────────────────┐ │
          │  │  AnalyticsController                     │ │
          │  │  ┌────────────────────────────────────┐ │ │
          │  │  │ GET /api/admin/analytics/dashboard │ │ │
          │  │  │ GET /api/admin/analytics/revenue   │ │ │
          │  │  │ GET /api/admin/analytics/orders    │ │ │
          │  │  │ GET /api/admin/analytics/products  │ │ │
          │  │  │ GET /api/admin/analytics/customers │ │ │
          │  │  │ GET /api/admin/analytics/payments  │ │ │
          │  │  └────────────────────────────────────┘ │ │
          │  └──────────────────────────────────────────┘ │
          │                    ↓                          │
          │  ┌──────────────────────────────────────────┐ │
          │  │  AnalyticsService                        │ │
          │  │  ┌────────────────────────────────────┐ │ │
          │  │  │ getDashboardSummary()              │ │ │
          │  │  │ getRevenueAnalytics()              │ │ │
          │  │  │ getOrderAnalytics()                │ │ │
          │  │  │ getProductAnalytics()              │ │ │
          │  │  │ getCustomerAnalytics()             │ │ │
          │  │  │ getPaymentAnalytics()              │ │ │
          │  │  │                                    │ │ │
          │  │  │ Helper Methods:                    │ │ │
          │  │  │ • getTotalRevenue()                │ │ │
          │  │  │ • getTotalOrders()                 │ │ │
          │  │  │ • calculateRevenuGrowth()          │ │ │
          │  │  │ • calculateAverageProcessingTime() │ │ │
          │  │  │ • calculateProfitMargin()          │ │ │
          │  │  └────────────────────────────────────┘ │ │
          │  └──────────────────────────────────────────┘ │
          │                    ↓                          │
          │  ┌──────────────────────────────────────────┐ │
          │  │  Data Models (DTOs)                      │ │
          │  │  ┌────────────────────────────────────┐ │ │
          │  │  │ DashboardSummary                   │ │ │
          │  │  │ RevenueAnalytics                   │ │ │
          │  │  │ OrderAnalytics                     │ │ │
          │  │  │ ProductAnalytics                   │ │ │
          │  │  │ CustomerAnalytics                  │ │ │
          │  │  │ PaymentAnalytics                   │ │ │
          │  │  │ InventoryAnalytics                 │ │ │
          │  │  │ GeographicAnalytics                │ │ │
          │  │  └────────────────────────────────────┘ │ │
          │  └──────────────────────────────────────────┘ │
          │                    ↓                          │
          │  ┌──────────────────────────────────────────┐ │
          │  │  Spring Data R2DBC Repositories          │ │
          │  │  ┌────────────────────────────────────┐ │ │
          │  │  │ OrderRepository.findAll()          │ │ │
          │  │  │ ProductRepository.findAll()        │ │ │
          │  │  │ OrderItemRepository.findAll()      │ │ │
          │  │  │ UserRepository.findAll()           │ │ │
          │  │  │ CartRepository.findByUserId()      │ │ │
          │  │  └────────────────────────────────────┘ │ │
          │  └──────────────────────────────────────────┘ │
          └────────────────────────────────────────────────┘
                            ↓
          ┌─────────────────────────────────────┐
          │    PostgreSQL Database              │
          │                                     │
          │  Tables:                            │
          │  ┌─────────────────────────────────┤
          │  │ • orders                        │
          │  │ • products                      │
          │  │ • order_items                   │
          │  │ • users                         │
          │  │ • carts                         │
          │  │ • cart_items                    │
          │  │ • payments                      │
          │  └─────────────────────────────────┤
          │                                     │
          └─────────────────────────────────────┘
```

---

## Data Flow Diagram

### Dashboard Data Load Flow

```
User Opens Dashboard
         ↓
JavaScript Loaded
         ↓
API Call: GET /api/admin/analytics/dashboard
         ↓
    AnalyticsController
         ↓
    AnalyticsService.getDashboardSummary()
         ↓
    ┌─────────────────────────────────────┐
    │ Parallel Mono Operations:           │
    │ • getTotalRevenue()                 │
    │ • getTotalOrders()                  │
    │ • getTotalCustomers()               │
    │ • getTotalProductsSold()            │
    │ • getOrderStatusCounts()            │
    │ • getAverageOrderValue()            │
    └─────────────────────────────────────┘
         ↓
    Repository Layer (R2DBC)
         ↓
    Database Queries
         ↓
    Aggregate Results
         ↓
    Build DashboardSummary DTO
         ↓
    Return JSON Response
         ↓
JavaScript parses response
         ↓
renderKPICards()
         ↓
Update UI with Data
         ↓
renderRevenueChart()
         ↓
renderOrderStatusChart()
         ↓
renderTopProductsChart()
         ↓
Dashboard Displays
```

### Revenue Analytics Flow

```
User Selects "Revenue" Tab
         ↓
Click: GET /api/admin/analytics/revenue?period=daily
         ↓
    AnalyticsController.getRevenueAnalytics()
         ↓
    AnalyticsService.getRevenueAnalytics(period)
         ↓
    If period = "daily" → getLastNDaysRevenue(30)
         ↓
    OrderRepository.findAll()
         ↓
    Process Orders:
    ├─ Group by date
    ├─ Calculate daily revenue
    ├─ Count orders per day
    ├─ Calculate avg order value
    └─ Sort chronologically
         ↓
    Calculate metrics:
    ├─ Total revenue
    ├─ Revenue growth %
    └─ Trend (UP/DOWN/STABLE)
         ↓
    Return RevenueAnalytics DTO
         ↓
JavaScript:
    ├─ Parse response
    ├─ Update stats list
    └─ Display in UI
         ↓
Dashboard Shows Revenue Data
```

### Order Analytics Flow

```
User Clicks "Orders" Tab
         ↓
GET /api/admin/analytics/orders
         ↓
    AnalyticsService.getOrderAnalytics()
         ↓
    OrderRepository.findAll()
         ↓
    Process Orders by Status:
    ├─ Count PENDING
    ├─ Count PROCESSING
    ├─ Count CONFIRMED
    ├─ Count SHIPPED
    ├─ Count DELIVERED
    └─ Count CANCELLED
         ↓
    Calculate metrics:
    ├─ Total orders
    ├─ Percentage per status
    └─ Average processing time
         ↓
    Build OrderStatusCount list
         ↓
    Return OrderAnalytics DTO
         ↓
JavaScript:
    ├─ Create status distribution
    ├─ Render doughnut chart
    └─ Populate table
         ↓
Orders Section Updated
```

---

## Database Query Pattern

```
┌──────────────────────────────────────────────────┐
│  Analytics Service Query Pattern                  │
│                                                  │
│  1. Repository.findAll() [Non-blocking]         │
│                ↓                                 │
│  2. collectList() [Convert Flux to List]         │
│                ↓                                 │
│  3. Stream Processing [Java Streams]            │
│     ├─ Filter                                    │
│     ├─ Map                                       │
│     ├─ Group by                                  │
│     ├─ Sort                                      │
│     └─ Reduce                                    │
│                ↓                                 │
│  4. Build DTO [Return Object]                   │
│                ↓                                 │
│  5. Response to Controller                      │
│                ↓                                 │
│  6. JSON Serialization                          │
│                ↓                                 │
│  7. Send to Client                              │
└──────────────────────────────────────────────────┘
```

---

## Component Interaction Matrix

```
┌─────────────────────────────────────────────────────────────────┐
│ Frontend Components Interaction                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Sidebar Navigation                                            │
│    ├─ Calls navigateToSection()                              │
│    ├─ Updates active link styling                            │
│    └─ Shows/hides relevant section                           │
│           ↓                                                    │
│         Section Page                                          │
│           ├─ KPI Cards Container                             │
│           ├─ Charts Container                                │
│           ├─ Tables Container                                │
│           └─ Stats Container                                 │
│                  ↓                                            │
│              API Calls                                        │
│           (Fetch API)                                         │
│                  ↓                                            │
│         Backend Endpoints                                     │
│           ↓                                                   │
│      Response Handlers                                        │
│        ├─ renderKPICards()                                   │
│        ├─ renderRevenueChart()                               │
│        ├─ renderOrderStatusChart()                           │
│        ├─ renderTopProductsChart()                           │
│        ├─ renderPaymentMethodsChart()                        │
│        ├─ renderTopProductsTable()                           │
│        └─ renderLowStockAlerts()                             │
│           ↓                                                    │
│         DOM Updates                                           │
│         Display to User                                       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## API Response Structure

```
┌─────────────────────────────────────────────────┐
│  API Response Format (All Endpoints)             │
│                                                 │
│  {                                             │
│    "success": true,          ← Operation status │
│    "message": "...",         ← Human readable   │
│    "data": {                 ← Actual data     │
│      // Dashboard Summary DTO                   │
│      // or Revenue Analytics DTO                │
│      // or Order Analytics DTO                  │
│      // etc.                                    │
│    }                                            │
│  }                                             │
│                                                 │
│  On Error:                                      │
│  {                                             │
│    "success": false,                           │
│    "message": "Error description"              │
│  }                                             │
│                                                 │
└─────────────────────────────────────────────────┘
```

---

## Caching Strategy (Recommended)

```
┌──────────────────────────────────┐
│  Cache Layer (Optional)          │
│                                  │
│  @Cacheable("dashboard")         │
│  → Cache for 5 minutes           │
│  → Key: "dashboard"              │
│                                  │
│  @Cacheable("revenue")           │
│  → Cache per period              │
│  → Key: "revenue:daily"          │
│     "revenue:weekly"             │
│     "revenue:monthly"            │
│                                  │
│  Cache Invalidation:             │
│  → On new order                  │
│  → On product update             │
│  → On payment completion         │
│  → Every 5 minutes (TTL)         │
│                                  │
└──────────────────────────────────┘
```

---

## Error Handling Flow

```
API Call
   ↓
Try Block
   ├─ Query Database
   ├─ Process Data
   └─ Build DTO
   ↓
Exception Caught
   ├─ Log Error
   ├─ Build Error Response
   └─ Send to Client
   ↓
Frontend
   ├─ Check response.success
   ├─ If false → Show error message
   └─ If true → Update UI
   ↓
User Sees Error or Data
```

---

## Performance Considerations

```
┌─────────────────────────────────────────────────┐
│  Performance Optimization Points                │
│                                                 │
│  1. Database                                    │
│     • Use indexes on Order/Product queries      │
│     • Aggregate at DB level if possible         │
│     • Paginate large result sets                │
│                                                 │
│  2. Backend                                     │
│     • Use @Cacheable for frequently called      │
│     • Parallel queries with Mono.zip()          │
│     • Use R2DBC (non-blocking)                  │
│                                                 │
│  3. Frontend                                    │
│     • Lazy load charts                          │
│     • Debounce refresh calls                    │
│     • Cache responses client-side               │
│     • Use CDN for Chart.js                      │
│                                                 │
│  4. Network                                     │
│     • Gzip compression                          │
│     • Minimize payload size                     │
│     • Use HTTP/2                                │
│                                                 │
└─────────────────────────────────────────────────┘
```

---

## Deployment Architecture

```
┌──────────────────────────────────────────────────────┐
│  Production Deployment                              │
│                                                      │
│  ┌────────────────────────────────────────────────┐ │
│  │  Load Balancer (Nginx/HAProxy)                 │ │
│  │  • Route requests to backend instances         │ │
│  │  • SSL/TLS termination                         │ │
│  │  • Serve static HTML dashboard                 │ │
│  └────────────────────────────────────────────────┘ │
│              ↓                                       │
│  ┌────────────────────────────────────────────────┐ │
│  │  Multiple Backend Instances                    │ │
│  │  • Spring Boot App 1 (Port 8080)               │ │
│  │  • Spring Boot App 2 (Port 8081)               │ │
│  │  • Spring Boot App 3 (Port 8082)               │ │
│  │                                                 │ │
│  │  Cache Layer (Redis)                           │ │
│  │  • Cache dashboard data                        │ │
│  │  • Session storage                             │ │
│  └────────────────────────────────────────────────┘ │
│              ↓                                       │
│  ┌────────────────────────────────────────────────┐ │
│  │  PostgreSQL Database                           │ │
│  │  • Primary node (read/write)                   │ │
│  │  • Replica nodes (read-only)                   │ │
│  │  • Automated backups                           │ │
│  │  • Connection pooling                          │ │
│  └────────────────────────────────────────────────┘ │
│                                                      │
└──────────────────────────────────────────────────────┘
```

---

## Security Architecture

```
┌──────────────────────────────────────────┐
│  Security Layers                         │
│                                          │
│  1. HTTPS/TLS                            │
│     └─ Encrypt data in transit           │
│                                          │
│  2. Authentication (JWT)                 │
│     ├─ Verify user identity              │
│     └─ Bearer token in header            │
│                                          │
│  3. Authorization (RBAC)                 │
│     ├─ Check user role is ADMIN          │
│     ├─ @PostAuthorize decorator          │
│     └─ Method-level security             │
│                                          │
│  4. CORS Configuration                   │
│     ├─ Restrict allowed origins          │
│     ├─ Limit HTTP methods                │
│     └─ Control headers                   │
│                                          │
│  5. Rate Limiting                        │
│     └─ Prevent API abuse                 │
│                                          │
│  6. Input Validation                     │
│     └─ Validate request parameters       │
│                                          │
│  7. Audit Logging                        │
│     └─ Log all admin access              │
│                                          │
│  8. Database Encryption                  │
│     └─ Encrypt sensitive data             │
│                                          │
└──────────────────────────────────────────┘
```

---

**Architecture Last Updated**: February 2, 2026
