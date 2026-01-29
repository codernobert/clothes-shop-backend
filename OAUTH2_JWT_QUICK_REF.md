# 🚀 OAuth 2 + JWT Quick Reference Card

## ⚡ Quick Start

### 1. Start Backend
```bash
cd backend/clothes-shop-backend
mvn spring-boot:run
```

### 2. Open Frontend
Open `frontend/register.html` in browser

---

## 📡 API Endpoints

### Public Endpoints
```bash
POST /api/auth/register     # Register new user
POST /api/auth/login        # Login user
GET  /api/products          # Get all products
GET  /api/products/{id}     # Get product by ID
```

### Protected Endpoints (Requires Token)
```bash
GET  /api/auth/me           # Get current user
POST /api/auth/logout       # Logout
POST /api/cart/add          # Add to cart
GET  /api/cart/{userId}     # Get cart
POST /api/checkout/create   # Create checkout
GET  /api/orders/user/{id}  # Get user orders
```

### Admin Only
```bash
POST /api/admin/products    # Add product
PUT  /api/admin/products    # Update product
DELETE /api/admin/products/{id}  # Delete product
```

---

## 🔑 Token Usage

### Get Token (Login)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"pass123"}'
```

### Use Token in Request
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## 📝 Registration Example

```javascript
// JavaScript
const response = await fetch('http://localhost:8080/api/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        email: 'user@example.com',
        password: 'password123',
        firstName: 'John',
        lastName: 'Doe',
        phone: '+254712345678'
    })
});

const data = await response.json();
localStorage.setItem('accessToken', data.accessToken);
localStorage.setItem('refreshToken', data.refreshToken);
```

---

## 🔄 Token Refresh

```javascript
// When access token expires
const response = await fetch('http://localhost:8080/api/auth/refresh', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        refreshToken: localStorage.getItem('refreshToken')
    })
});

const data = await response.json();
localStorage.setItem('accessToken', data.accessToken);
```

---

## 🛡️ Security Headers

All protected requests must include:
```
Authorization: Bearer <access_token>
Content-Type: application/json
```

---

## ⚙️ Configuration

### application.properties
```properties
jwt.secret=YOUR_SECRET_KEY_64_CHARS_MIN
jwt.expiration=86400000              # 24 hours
jwt.refresh-expiration=604800000     # 7 days
```

### Generate Secret Key (Bash)
```bash
openssl rand -base64 64
```

### Generate Secret Key (PowerShell)
```powershell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
```

---

## 🎯 User Roles

| Role     | Access                              |
|----------|-------------------------------------|
| CUSTOMER | Products, Cart, Orders (own)       |
| ADMIN    | All endpoints + product management  |

---

## 📦 Response Format

### Success Response
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "tokenType": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "CUSTOMER"
}
```

### Error Response
```json
{
  "status": 400,
  "message": "Email already exists",
  "timestamp": "2026-01-28T10:30:00"
}
```

---

## 🧪 Test Sequence

1. **Register** → Get tokens
2. **Login** → Get tokens (if already registered)
3. **Get Products** → Browse available products
4. **Add to Cart** → Use access token
5. **Checkout** → Use access token
6. **Get Orders** → Use access token

---

## 🐛 Common Issues

### 401 Unauthorized
- Token expired → Use refresh token
- Invalid token → Login again
- Missing Authorization header

### 403 Forbidden
- Insufficient permissions
- Check user role

### CORS Error
- Update `WebConfig.java` with frontend URL
- Check allowed origins

---

## 📂 File Structure

```
backend/
├── config/
│   ├── SecurityConfig.java        # Security setup
│   └── JwtProperties.java         # JWT config
├── security/
│   ├── JwtTokenProvider.java      # Token generation
│   ├── JwtAuthenticationFilter.java  # Token validation
│   └── UserPrincipal.java         # User context
├── controller/
│   └── AuthController.java        # Auth endpoints
├── service/
│   └── UserService.java           # User business logic
└── dto/
    ├── RegisterRequest.java
    ├── LoginRequest.java
    └── AuthResponse.java

frontend/
├── js/
│   └── auth-api.js                # API client
├── login.html                     # Login page
└── register.html                  # Registration page
```

---

## 🔗 Resources

- **Documentation:** `OAUTH2_JWT_IMPLEMENTATION_GUIDE.md`
- **Postman Collection:** `Clothes_Shop_Authentication.postman_collection.json`
- **Frontend Example:** `frontend/js/auth-api.js`

---

## ✅ Checklist

- [ ] Backend running on port 8080
- [ ] Database connected
- [ ] JWT secret configured
- [ ] CORS configured for frontend
- [ ] Test registration endpoint
- [ ] Test login endpoint
- [ ] Test protected endpoints with token
- [ ] Test token refresh
- [ ] Frontend pages loading correctly

---

**Quick Help:**
```bash
# Check if backend is running
curl http://localhost:8080/actuator/health

# View logs
mvn spring-boot:run

# Run tests
mvn test
```

---

**Version:** 1.0.0 | **Date:** January 28, 2026
