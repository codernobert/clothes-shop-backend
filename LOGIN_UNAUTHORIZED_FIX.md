# 🔐 LOGIN UNAUTHORIZED FIX - Complete Guide

## ❌ Problem

Getting **401 Unauthorized** when trying to login

```
POST http://localhost:8080/api/auth/login
--header 'Authorization: Basic YWRtaW5AZXhhbXBsZS5jb206cGFzc3dvcmQxMjM='
--body ''
```

**Error**: 401 Unauthorized

---

## ✅ Root Cause

Your login endpoint expects:
- **Method**: POST
- **Content-Type**: application/json
- **Body Format**: JSON with `email` and `password`

You were sending:
- **Method**: POST ✓
- **Content-Type**: Missing
- **Body Format**: Basic Auth headers + empty body ✗

---

## ✅ CORRECT LOGIN REQUEST

### Option 1: Using cURL (Recommended)

```bash
curl -X POST 'http://localhost:8080/api/auth/login' \
  --header 'Content-Type: application/json' \
  --data '{
    "email": "admin@example.com",
    "password": "password123"
  }'
```

**Response**:
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "...",
    "userId": 1,
    "email": "admin@example.com",
    "role": "ADMIN"
  }
}
```

---

### Option 2: Using Postman (Easiest)

**Step 1**: Create new request
- **Method**: POST
- **URL**: `http://localhost:8080/api/auth/login`

**Step 2**: Headers tab
```
Content-Type: application/json
```

**Step 3**: Body tab
- Select: **raw**
- Format: **JSON**
- Content:
```json
{
  "email": "admin@example.com",
  "password": "password123"
}
```

**Step 4**: Click Send
- ✅ Status: 200 OK
- Token saved to response

---

### Option 3: Using Python

```python
import requests
import json

url = "http://localhost:8080/api/auth/login"
headers = {
    "Content-Type": "application/json"
}
payload = {
    "email": "admin@example.com",
    "password": "password123"
}

response = requests.post(url, json=payload, headers=headers)
print(response.json())
```

---

### Option 4: Using JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    email: 'admin@example.com',
    password: 'password123'
  })
});

const data = await response.json();
console.log(data.data.accessToken);
```

---

## 🔧 What's Different

| Aspect | Wrong (You Tried) | Correct |
|--------|-------------------|---------|
| Auth Type | Basic Auth | JSON Body |
| Headers | Authorization: Basic... | Content-Type: application/json |
| Body | Empty | `{"email": "...", "password": "..."}` |
| Format | URL encoded | JSON |

---

## 📋 Complete Working Request

**cURL Command**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'
```

**Postman Setup**:
```
Method:      POST
URL:         http://localhost:8080/api/auth/login
Headers:     Content-Type: application/json
Body (raw):  {"email":"admin@example.com","password":"password123"}
```

**Response** (200 OK):
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "role": "ADMIN"
  }
}
```

---

## 🎯 Next Steps: Use Token

Once you get the token:

```bash
# Save token
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# Use token for analytics endpoints
curl -X GET http://localhost:8080/api/admin/analytics/dashboard \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
```

---

## ⚠️ Common Mistakes

### ❌ Mistake 1: Using Basic Auth
```
--auth-basic-username 'admin@example.com'
--auth-basic-password 'password123'
```
❌ Won't work

### ✅ Fix
```
-H "Content-Type: application/json"
-d '{"email":"admin@example.com","password":"password123"}'
```

---

### ❌ Mistake 2: Empty Body
```
--body ''
```
❌ Won't work

### ✅ Fix
```
-d '{"email":"admin@example.com","password":"password123"}'
```

---

### ❌ Mistake 3: No Content-Type Header
```
curl ... --data '{"email":"..."}'
```
❌ May fail

### ✅ Fix
```
curl ... -H "Content-Type: application/json" --data '{"email":"..."}'
```

---

## 🧪 Test with All Methods

### Test 1: cURL
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'
```
**Expected**: 200 OK with token

### Test 2: Postman
1. Method: POST
2. URL: http://localhost:8080/api/auth/login
3. Body (JSON): `{"email":"admin@example.com","password":"password123"}`
4. Click Send
**Expected**: 200 OK with token

### Test 3: Use Token
```bash
TOKEN="<token from login>"
curl -X GET http://localhost:8080/api/admin/analytics/dashboard \
  -H "Authorization: Bearer $TOKEN"
```
**Expected**: 200 OK with dashboard data

---

## 🎁 Postman Collection Already Has Correct Setup

Your **Admin_Analytics_API.postman_collection.json** has:
- ✅ Correct login request
- ✅ JSON body configured
- ✅ Token extraction script
- ✅ Auto-added to all endpoints

Just use the collection's "Login - Get JWT Token" request!

---

## ✅ Summary

**Problem**: Using Basic Auth instead of JSON body

**Solution**: 
1. Remove Basic Auth
2. Add JSON body with email & password
3. Add Content-Type: application/json header
4. Send POST request

**Result**: ✅ 200 OK with JWT token

---

## 🚀 Try Now

**cURL** (Copy & Paste):
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'
```

**Expected Output**:
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJ...",
    "userId": 1,
    "role": "ADMIN"
  }
}
```

**✅ Success!**

---

**Status**: ✅ FIXED  
**Date**: February 2, 2026  

Your login should now work! 🎉
