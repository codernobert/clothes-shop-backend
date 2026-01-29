# 🚀 QUICK START - PHP Frontend with JWT

## ⚡ Start in 3 Steps

### 1️⃣ Start Backend
```bash
cd "C:\Users\LENOVO\OneDrive\PERSONAL PROJECTS 2026\personal 2026\e_commerce_V2\backend\clothes-shop-backend"
mvn spring-boot:run
```

### 2️⃣ Start PHP Server
```bash
cd "C:\Users\LENOVO\OneDrive\PERSONAL PROJECTS 2026\personal 2026\e_commerce_V2\backend\clothes-shop-backend\frontend"
php -S localhost:8000
```

### 3️⃣ Open Browser
```
http://localhost:8000
```

---

## 📁 PHP Files (No HTML!)

### Public Pages
```
✅ index.php          - Home page
✅ products.php       - Product listing
✅ login.php          - Login
✅ register.php       - Registration
```

### Protected Pages
```
🔒 cart.php           - Shopping cart
🔒 orders.php         - Order history
🔒 checkout.php       - Checkout
```

---

## 🔐 Authentication Functions (config.php)

```php
// Check if logged in
if (isAuthenticated()) { ... }

// Get current user
$user = getCurrentUser();

// Get user ID
$userId = getUserId();

// Require authentication (redirect if not logged in)
requireAuth();

// Login user after API response
loginUser($authResponse);

// Logout user
logoutUser();

// API request with JWT
makeApiRequest($endpoint, $method, $data, $requireAuth);
```

---

## 🧪 Test Flow

### 1. Register New User
```
1. Open http://localhost:8000/register.php
2. Fill form and submit
3. Auto-login and redirect to home
4. Header shows "Welcome back, [Name]!"
```

### 2. Test Protected Page
```
1. Click "Cart" in navigation
2. Should load (you're logged in)
3. Try adding product - should work
```

### 3. Test Authentication Guard
```
1. Click "Logout"
2. Try accessing http://localhost:8000/cart.php
3. Should redirect to login.php
4. After login, should access cart
```

---

## 📊 Session Storage

After login, these are stored in `$_SESSION`:
```php
$_SESSION['access_token']   // JWT access token
$_SESSION['refresh_token']  // JWT refresh token
$_SESSION['user'] = [
    'userId' => 1,
    'email' => 'user@example.com',
    'firstName' => 'John',
    'lastName' => 'Doe',
    'role' => 'CUSTOMER'
]
```

---

## 🎯 Access Control

| Page | Authentication | Status |
|------|----------------|--------|
| index.php | No | ✅ Public |
| products.php | No | ✅ Public |
| login.php | No | ✅ Public |
| register.php | No | ✅ Public |
| cart.php | Yes | 🔒 Protected |
| orders.php | Yes | 🔒 Protected |
| checkout.php | Yes | 🔒 Protected |

---

## 🔍 Quick Troubleshooting

### "Session not working"
→ Add `session_start()` at top of page

### "Still redirecting after login"
→ Check if `loginUser()` is called after API response

### "Cart empty"
→ Verify `getUserId()` returns correct ID

### "CORS errors"
→ Add `http://localhost:8000` to backend WebConfig.java

---

## ✅ Success Checklist

- [x] Backend running on port 8080
- [x] PHP server running on port 8000
- [x] Can register new user
- [x] Auto-login after registration
- [x] Can login with credentials
- [x] Can view products without login
- [x] Cannot access cart without login
- [x] Can access cart after login
- [x] Header shows user info when logged in
- [x] Logout works and clears session

**All checked?** ✅ **You're ready!**

---

## 📖 Full Documentation

- **PHP_FRONTEND_COMPLETE.md** - Complete guide
- **OAUTH2_JWT_IMPLEMENTATION_GUIDE.md** - JWT details
- **COMPLETE_TESTING_GUIDE.md** - Testing guide

---

## 🎉 Ready to Go!

**Status:** ✅ All PHP files working with JWT authentication

**No HTML files** - Pure PHP implementation

**Start testing now!** 🚀
