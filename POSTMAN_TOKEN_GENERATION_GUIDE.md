# 🔐 Postman JWT Token Generation Guide

## Quick Start - Generate Token for API Testing

### Step 1: Import Postman Collection
1. Open **Postman**
2. Click **Import**
3. Select `Admin_Analytics_API.postman_collection.json`
4. Collection imported! ✅

---

## 🔑 Authentication Endpoints Available

### 1️⃣ **Login - Get JWT Token** (Recommended)
**Endpoint**: `POST {{base_url}}/api/auth/login`

**Purpose**: Get JWT token for testing analytics endpoints

**Request Body**:
```json
{
  "email": "admin@example.com",
  "password": "password123"
}
```

**What it does**:
- ✅ Authenticates with email & password
- ✅ Returns JWT access token
- ✅ Automatically saves token to Postman environment
- ✅ Token usable for all subsequent requests

**Response**:
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "role": "ADMIN"
  }
}
```

---

### 2️⃣ **Register - Create Admin User**
**Endpoint**: `POST {{base_url}}/api/auth/register`

**Purpose**: Create a new admin user account for testing

**Request Body**:
```json
{
  "firstName": "Admin",
  "lastName": "User",
  "email": "admin@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```

**Use Case**: If user doesn't exist, create one first before logging in

---

## 📋 How to Get Token & Test Analytics

### Method 1: Automatic Token Saving (Easiest)

1. **Open Postman**
2. **Select Collection**: "Admin Analytics Dashboard API"
3. **Go to Authentication Tab**
4. **Run**: "Login - Get JWT Token"
   ```
   POST /api/auth/login
   Body: {"email": "admin@example.com", "password": "password123"}
   ```
5. ✅ **Token automatically saved to environment**
6. **Use token**: All other endpoints will use `{{jwt_token}}` automatically

### Method 2: Manual Token Setting

1. **Get token response** from login endpoint
2. **Copy** the `accessToken` value
3. **Environment Variables**:
   - Click "Edit" (environment icon)
   - Set `jwt_token` = your token value
   - Save
4. **Use in requests**: Authorization header already configured

---

## 🔗 All Analytics Endpoints Now Support Authentication

All these endpoints are pre-configured with JWT token:

| Endpoint | Method | Auth Header |
|----------|--------|------------|
| `/api/admin/analytics/dashboard` | GET | ✅ Bearer {{jwt_token}} |
| `/api/admin/analytics/revenue` | GET | ✅ Bearer {{jwt_token}} |
| `/api/admin/analytics/orders` | GET | ✅ Bearer {{jwt_token}} |
| `/api/admin/analytics/products` | GET | ✅ Bearer {{jwt_token}} |
| `/api/admin/analytics/customers` | GET | ✅ Bearer {{jwt_token}} |
| `/api/admin/analytics/payments` | GET | ✅ Bearer {{jwt_token}} |

---

## 🚀 Complete Testing Workflow

### Step 1: Get JWT Token
```
POST http://localhost:8080/api/auth/login

Headers:
  Content-Type: application/json

Body:
{
  "email": "admin@example.com",
  "password": "password123"
}
```

**Response**:
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### Step 2: Use Token in Analytics Requests
```
GET http://localhost:8080/api/admin/analytics/dashboard

Headers:
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
  Accept: application/json
```

**Response**:
```json
{
  "success": true,
  "data": {
    "totalRevenue": 5250000,
    "totalOrders": 245,
    "totalCustomers": 182,
    ...
  }
}
```

---

## 💡 Tips & Tricks

### Auto-Extract Token (Advanced)
The collection includes test scripts that automatically:
1. Parse JWT from response
2. Save to Postman environment as `jwt_token`
3. Add to Authorization header for next requests

### Token Expiration
- Default token expiry: Check your JWT configuration
- If token expires, re-run login endpoint to get new one
- Postman will automatically update environment variable

### Testing Multiple Credentials
1. Create environment variables:
   - `admin_email` = "admin@example.com"
   - `admin_password` = "password123"
2. Update login request to use variables
3. Quickly test with different credentials

### Debugging Token Issues
- **401 Unauthorized**: Token expired or invalid
  - Solution: Re-run login endpoint
- **403 Forbidden**: User doesn't have ADMIN role
  - Solution: Register with role="ADMIN"
- **400 Bad Request**: Invalid credentials
  - Solution: Check email/password in login body

---

## 📊 Complete API Testing Sequence

1. ✅ **Register User** (if new)
   - POST /api/auth/register
   
2. ✅ **Get Token**
   - POST /api/auth/login
   - Save token to environment

3. ✅ **Test Dashboard**
   - GET /api/admin/analytics/dashboard
   - (Token auto-added to header)

4. ✅ **Test Revenue Analytics**
   - GET /api/admin/analytics/revenue?period=daily
   - GET /api/admin/analytics/revenue?period=weekly
   - GET /api/admin/analytics/revenue?period=monthly

5. ✅ **Test Other Analytics**
   - GET /api/admin/analytics/orders
   - GET /api/admin/analytics/products
   - GET /api/admin/analytics/customers
   - GET /api/admin/analytics/payments

---

## 🎯 Environment Variables Reference

| Variable | Value | Purpose |
|----------|-------|---------|
| `base_url` | `http://localhost:8080` | API base URL |
| `jwt_token` | Auto-set by login | Bearer token for auth |
| `auth_header` | Auto-set by login | Full auth header |

---

## ✅ Status Check

**Collection Status**: ✅ Updated with authentication

**All Endpoints Have**:
- ✅ JWT token support
- ✅ Authorization header configured
- ✅ Test scripts included
- ✅ Auto-token extraction

---

## 🚀 Ready to Test!

1. Import collection
2. Run "Login - Get JWT Token"
3. Run any analytics endpoint
4. All requests will automatically include token ✅

**Enjoy testing!** 🎉
