# 🏗️ Railway Deployment Architecture
## System Architecture Overview
```
┌─────────────────────────────────────────────────────────────────────┐
│                         USERS / CUSTOMERS                            │
│                    (Browsers, Mobile Devices)                        │
└──────────────────────────┬──────────────────────────────────────────┘
                           │
                           │ HTTPS
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      RAILWAY CLOUD PLATFORM                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │               FRONTEND PROJECT (Separate)                      │ │
│  ├────────────────────────────────────────────────────────────────┤ │
│  │  Service: PHP Frontend                                         │ │
│  │  URL: https://your-frontend.railway.app                       │ │
│  │                                                                 │ │
│  │  Components:                                                    │ │
│  │  • index.php (Product listing)                                 │ │
│  │  • product_detail.php                                          │ │
│  │  • cart.php                                                    │ │
│  │  • checkout.php                                                │ │
│  │  • payment_callback.php                                        │ │
│  │  • admin/ (Admin panel)                                        │ │
│  │  • ajax/ (API calls)                                           │ │
│  │                                                                 │ │
│  │  Environment Variables:                                         │ │
│  │  • BACKEND_API_URL=https://your-backend.railway.app/api       │ │
│  │  • PAYSTACK_PUBLIC_KEY=pk_live_...                            │ │
│  └────────────────┬───────────────────────────────────────────────┘ │
│                   │                                                   │
│                   │ REST API Calls (AJAX)                            │
│                   │ GET, POST, PUT, DELETE                           │
│                   │                                                   │
│  ┌────────────────▼───────────────────────────────────────────────┐ │
│  │               BACKEND PROJECT (Current)                        │ │
│  ├────────────────────────────────────────────────────────────────┤ │
│  │  Service: Spring Boot API (Java 17)                           │ │
│  │  URL: https://your-backend.railway.app                        │ │
│  │  Port: $PORT (auto-assigned by Railway)                       │ │
│  │                                                                 │ │
│  │  API Endpoints:                                                 │ │
│  │  • GET  /api/products           (Browse products)              │ │
│  │  • GET  /api/products/{id}      (Product details)              │ │
│  │  • POST /api/cart/add           (Add to cart)                 │ │
│  │  • GET  /api/cart/{sessionId}   (View cart)                   │ │
│  │  • POST /api/orders/checkout    (Create order)                │ │
│  │  • POST /api/payments/initialize (Start payment)              │ │
│  │  • POST /api/payments/verify    (Verify payment)              │ │
│  │  • GET  /actuator/health        (Health check)                │ │
│  │                                                                 │ │
│  │  Environment Variables:                                         │ │
│  │  • PORT=$PORT (Railway auto)                                   │ │
│  │  • PGHOST, PGPORT, PGDATABASE, PGUSER, PGPASSWORD (Railway)   │ │
│  │  • PAYSTACK_API_KEY=sk_live_...                               │ │
│  │  • PAYSTACK_CALLBACK_URL=https://your-frontend.../callback    │ │
│  │                                                                 │ │
│  │  Technologies:                                                  │ │
│  │  • Spring Boot 3.5.9 (WebFlux)                                │ │
│  │  • Spring Data R2DBC (Reactive DB)                            │ │
│  │  • Resilience4j (Circuit breaker)                             │ │
│  │  • Maven 3.9.5                                                │ │
│  └────────────────┬───────────────────────────────────────────────┘ │
│                   │                                                   │
│                   │ R2DBC Connection                                 │
│                   │ Reactive PostgreSQL Driver                       │
│                   │                                                   │
│  ┌────────────────▼───────────────────────────────────────────────┐ │
│  │          DATABASE (Same Backend Project)                       │ │
│  ├────────────────────────────────────────────────────────────────┤ │
│  │  Service: PostgreSQL (Managed)                                │ │
│  │  Version: 15+                                                  │ │
│  │                                                                 │ │
│  │  Tables:                                                        │ │
│  │  • products        (Product catalog)                           │ │
│  │  • cart_items      (Shopping cart)                             │ │
│  │  • orders          (Customer orders)                           │ │
│  │  • order_items     (Order line items)                          │ │
│  │  • payments        (Payment records)                           │ │
│  │                                                                 │ │
│  │  Auto-provisioned Environment Variables:                       │ │
│  │  • PGHOST          (Database host)                             │ │
│  │  • PGPORT          (Database port, default 5432)               │ │
│  │  • PGDATABASE      (Database name)                             │ │
│  │  • PGUSER          (Database user)                             │ │
│  │  • PGPASSWORD      (Database password)                         │ │
│  │  • DATABASE_URL    (Full connection string)                    │ │
│  └────────────────────────────────────────────────────────────────┘ │
│                                                                       │
└───────────────────────────┬───────────────────────────────────────────┘
                            │
                            │ HTTPS API Calls
                            ▼
                ┌─────────────────────────┐
                │   PAYSTACK API          │
                │   (External Service)    │
                ├─────────────────────────┤
                │ • Payment initialization│
                │ • Payment verification  │
                │ • Webhooks              │
                │ • MPESA integration     │
                │ • Card processing       │
                │ • Mobile money          │
                └─────────────────────────┘
```
---
## Request Flow: Customer Journey
### 1️⃣ Browse Products
```
User → Frontend (index.php)
     → Backend (GET /api/products)
     → Database (SELECT * FROM products)
     → Backend (JSON response)
     → Frontend (Display products)
```
### 2️⃣ Add to Cart
```
User → Frontend (Click "Add to Cart")
     → Backend (POST /api/cart/add)
     → Database (INSERT INTO cart_items)
     → Backend (Success response)
     → Frontend (Update cart count)
```
### 3️⃣ Checkout & Payment
```
User → Frontend (checkout.php)
     → Backend (POST /api/orders/checkout)
     → Database (INSERT INTO orders, order_items)
     → Backend (POST /api/payments/initialize)
     → Paystack API (Create payment link)
     → Frontend (Redirect to Paystack)
     → User completes payment on Paystack
     → Paystack redirects to Frontend (payment_callback.php)
     → Backend (POST /api/payments/verify)
     → Paystack API (Verify transaction)
     → Database (UPDATE payments, orders)
     → Frontend (Show success message)
```
---
## Deployment Flow
### Git Push Triggers Auto-Deploy
```
Developer → Git Commit & Push
         → GitHub Repository
         → Railway Webhook (detects push)
         → Railway Build Process
         → Maven Build (mvn clean package)
         → Create Docker Container
         → Deploy to Railway Cloud
         → Assign Public URL
         → Health Check (/actuator/health)
         → Service Running ✅
```
---
## Environment Variable Flow
### Railway Auto-Injects Database Credentials
```
PostgreSQL Service Created
    ↓
Railway generates credentials
    ↓
Sets environment variables:
    • PGHOST
    • PGPORT
    • PGDATABASE
    • PGUSER
    • PGPASSWORD
    ↓
Backend application.properties reads:
    spring.r2dbc.url=r2dbc:postgresql://${PGHOST}:${PGPORT}/${PGDATABASE}
    spring.r2dbc.username=${PGUSER}
    spring.r2dbc.password=${PGPASSWORD}
    ↓
R2DBC Connection Pool established
    ↓
Database queries work seamlessly
```
---
## Monitoring & Observability
### Railway Dashboard
```
┌─────────────────────────────────┐
│   Railway Project Dashboard     │
├─────────────────────────────────┤
│                                 │
│ • Deployments (build logs)      │
│ • Metrics (CPU, Memory, Network)│
│ • Environment Variables         │
│ • Database Console              │
│ • Custom Domains                │
│ • Collaboration                 │
│                                 │
└─────────────────────────────────┘
```
### Application Endpoints
```
Health Check:
  https://your-backend.railway.app/actuator/health
Metrics:
  https://your-backend.railway.app/actuator/metrics
Circuit Breakers:
  https://your-backend.railway.app/actuator/circuitbreakers
```
---
## Security Architecture
### HTTPS Everywhere
```
All traffic encrypted with TLS:
  • Railway provides SSL certificates
  • Automatic HTTPS redirection
  • Secure environment variables
  • Database encryption at rest
```
### CORS Protection
```java
// WebConfig.java restricts origins
.allowedOrigins("https://your-frontend.railway.app")
// Only your frontend can call your backend
// Prevents unauthorized API access
```
### API Key Security
```
Paystack Secret Keys:
  • Stored in Railway environment variables
  • Never committed to Git
  • Only accessible to Railway services
  • Rotatable without code changes
```
---
## Cost Breakdown
```
Railway Hobby Plan: `/month
├── Includes: ` execution credit
├── Backend Service: ~`-3/month (covered by credit)
├── PostgreSQL: ~`-2/month
├── Frontend Service: ~`-2/month
└── Total: ~`-10/month (very low traffic)
Scaling:
├── Railway Pro: `/month (more resources)
├── Pay-as-you-go for overage
└── PostgreSQL grows with data size
```
---
## Backup Strategy
### Database Backups (Railway)
```
Automatic backups:
  • Daily snapshots
  • Point-in-time recovery
  • Manual snapshot option
  • Download backup data
```
### Code Backups (GitHub)
```
Version control:
  • All code in Git
  • Push to GitHub
  • Tagged releases
  • Easy rollback
```
---
## Disaster Recovery
### If Backend Crashes
1. Railway auto-restarts (configured in railway.json)
2. Health check monitors /actuator/health
3. Circuit breaker prevents cascade failures
4. Logs available for debugging
### If Database Fails
1. Railway automatically restarts PostgreSQL
2. Restore from latest backup
3. R2DBC reconnects automatically
4. Transactions ensure data consistency
### If Frontend Fails
1. Railway restarts PHP service
2. Independent from backend
3. Static assets cached
4. Users can retry requests
---
This architecture provides:
- ✅ High availability (Railway's infrastructure)
- ✅ Auto-scaling (Railway handles load)
- ✅ Easy deployment (Git push = deploy)
- ✅ Cost-effective (pay for what you use)
- ✅ Secure (HTTPS, env vars, CORS)
- ✅ Observable (logs, metrics, health checks)
- ✅ Maintainable (separate services)
