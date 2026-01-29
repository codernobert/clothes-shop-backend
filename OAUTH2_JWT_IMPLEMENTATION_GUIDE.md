# OAuth 2 + JWT Authentication Implementation Guide

## 🎯 Overview

This guide covers the complete OAuth 2 + JWT authentication system implemented for the Clothes Shop backend.

## 📋 Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Backend Implementation](#backend-implementation)
3. [Frontend Integration](#frontend-integration)
4. [API Endpoints](#api-endpoints)
5. [Testing Guide](#testing-guide)
6. [Security Best Practices](#security-best-practices)

---

## 🏗️ Architecture Overview

### Flow Diagram

```
┌─────────────┐                ┌─────────────┐
│   Frontend  │                │   Backend   │
│             │                │             │
└──────┬──────┘                └──────┬──────┘
       │                              │
       │  1. Register/Login           │
       │  (email, password)           │
       │─────────────────────────────>│
       │                              │
       │  2. Validate & Generate JWT  │
       │     (Access + Refresh Token) │
       │<─────────────────────────────│
       │                              │
       │  3. Store tokens in          │
       │     localStorage             │
       │                              │
       │  4. API Request with         │
       │     Authorization Header     │
       │  Bearer <access_token>       │
       │─────────────────────────────>│
       │                              │
       │  5. Validate JWT & Process   │
       │<─────────────────────────────│
       │                              │
```

### Components

**Backend:**
- **JwtTokenProvider**: Generates and validates JWT tokens
- **JwtAuthenticationFilter**: Intercepts requests and validates tokens
- **SecurityConfig**: Configures Spring Security with WebFlux
- **AuthController**: Handles registration, login, and token refresh
- **UserService**: Business logic for user management

**Frontend:**
- **auth-api.js**: JavaScript API client for authentication
- **login.html**: Login page
- **register.html**: Registration page

---

## 🔧 Backend Implementation

### 1. Dependencies (pom.xml)

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- OAuth2 Resource Server -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
```

### 2. Configuration (application.properties)

```properties
# JWT Configuration
jwt.secret=${JWT_SECRET:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}
jwt.expiration=${JWT_EXPIRATION:86400000}         # 24 hours
jwt.refresh-expiration=${JWT_REFRESH_EXPIRATION:604800000}  # 7 days
```

### 3. Security Configuration

**SecurityConfig.java** configures:
- Disabled CSRF (for stateless JWT)
- Public endpoints: `/api/auth/**`, `/api/products/**` (GET)
- Protected endpoints: `/api/cart/**`, `/api/checkout/**`, `/api/orders/**`
- Admin endpoints: `/api/admin/**`
- JWT filter at authentication level

### 4. JWT Token Structure

```json
{
  "userId": 123,
  "email": "user@example.com",
  "role": "CUSTOMER",
  "sub": "user@example.com",
  "iat": 1706450400,
  "exp": 1706536800
}
```

---

## 🌐 Frontend Integration

### 1. JavaScript API Client (auth-api.js)

#### Key Functions:

```javascript
// Register new user
await register({
    email: "user@example.com",
    password: "password123",
    firstName: "John",
    lastName: "Doe",
    phone: "+254712345678"
});

// Login user
await login({
    email: "user@example.com",
    password: "password123"
});

// Make authenticated API call
await authenticatedFetch('http://localhost:8080/api/cart/1');

// Logout (removes tokens)
logout();
```

#### Token Storage:

Tokens are stored in `localStorage`:
- `accessToken`: Short-lived token for API requests
- `refreshToken`: Long-lived token to get new access tokens
- `user`: User information (userId, email, name, role)

### 2. HTML Pages

**register.html** - User registration form
**login.html** - User login form

Both pages:
- Check if user is already authenticated
- Handle form submission
- Display error/success messages
- Redirect on success

---

## 📡 API Endpoints

### Authentication Endpoints

#### 1. Register User

**POST** `/api/auth/register`

**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+254712345678"
}
```

**Response (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "CUSTOMER"
}
```

#### 2. Login User

**POST** `/api/auth/login`

**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "CUSTOMER"
}
```

#### 3. Refresh Token

**POST** `/api/auth/refresh`

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9..."
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "CUSTOMER"
}
```

#### 4. Get Current User

**GET** `/api/auth/me`

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

**Response (200 OK):**
```json
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+254712345678",
  "role": "CUSTOMER"
}
```

#### 5. Logout

**POST** `/api/auth/logout`

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Logged out successfully. Please remove tokens from client."
}
```

---

## 🧪 Testing Guide

### 1. Test Registration

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "phone": "+254712345678"
  }'
```

### 2. Test Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### 3. Test Protected Endpoint

```bash
# Save access token from login response
export TOKEN="eyJhbGciOiJIUzUxMiJ9..."

curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN"
```

### 4. Test Cart (Protected)

```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

---

## 🔒 Security Best Practices

### 1. Token Security

✅ **Do:**
- Use HTTPS in production
- Set appropriate token expiration times
- Store tokens securely (httpOnly cookies for production)
- Validate tokens on every request
- Use strong secret keys (256-bit minimum)

❌ **Don't:**
- Store tokens in URLs
- Share tokens between users
- Use default/weak secret keys
- Store sensitive data in JWT payload

### 2. Password Security

✅ **Implemented:**
- BCrypt password hashing
- Minimum password length validation
- Password confirmation on registration

### 3. CORS Configuration

Update `WebConfig.java` for production:

```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOrigins("https://your-domain.com")  // Specific domain
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true);
}
```

### 4. Rate Limiting

Consider adding rate limiting for authentication endpoints to prevent brute-force attacks.

### 5. Environment Variables

**Production deployment:**

```bash
# Generate strong secret key (at least 64 characters)
JWT_SECRET=your-super-secret-key-at-least-64-characters-long

# Set appropriate expiration times
JWT_EXPIRATION=3600000          # 1 hour for access token
JWT_REFRESH_EXPIRATION=86400000  # 24 hours for refresh token
```

---

## 🚀 Quick Start

### Backend

1. **Start the application:**
```bash
mvn spring-boot:run
```

2. **Verify it's running:**
```bash
curl http://localhost:8080/actuator/health
```

### Frontend

1. **Open in browser:**
```
frontend/register.html
```

2. **Register a new account**

3. **Login and start shopping!**

---

## 🔄 Token Refresh Flow

```
User makes API request
        ↓
Check if access token exists
        ↓
    Is token valid? ─── Yes ──> Make API request
        ↓ No
Try to refresh token
        ↓
Refresh token valid? ─── Yes ──> Get new access token → Retry request
        ↓ No
Redirect to login
```

---

## 📝 User Roles

### CUSTOMER (Default)
- View products
- Manage cart
- Place orders
- View own orders

### ADMIN
- All customer permissions
- Add/edit/delete products
- View all orders
- Manage users

---

## 🐛 Troubleshooting

### Issue: "Cannot resolve symbol 'security'"

**Solution:** Reload Maven dependencies
```bash
mvn clean install -DskipTests
```

### Issue: "401 Unauthorized"

**Possible causes:**
1. Token expired → Use refresh token
2. Invalid token → Login again
3. Token not sent → Check Authorization header

### Issue: "403 Forbidden"

**Cause:** User doesn't have required role for endpoint

**Solution:** Check user role and endpoint permissions in SecurityConfig

### Issue: CORS errors

**Solution:** Update `allowedOrigins` in WebConfig to include your frontend URL

---

## 📚 Additional Resources

- [JWT.io](https://jwt.io/) - JWT debugger
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JJWT Documentation](https://github.com/jwtk/jjwt)

---

## ✅ Next Steps

1. ✅ Registration & Login implemented
2. ✅ JWT token generation
3. ✅ Protected endpoints
4. ✅ Frontend integration
5. 🔄 Add role-based access control to existing endpoints
6. 🔄 Implement remember me functionality
7. 🔄 Add email verification
8. 🔄 Implement password reset

---

**Created:** January 28, 2026
**Version:** 1.0.0
**Status:** ✅ Complete
