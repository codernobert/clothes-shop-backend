# 🎯 START HERE - OAuth 2 + JWT Authentication

## Welcome! 👋

This is your **complete OAuth 2 + JWT authentication system** for the Clothes Shop backend.

---

## 📖 Documentation Guide

Choose the right document for your needs:

### 🚀 Getting Started (You are here!)
**File:** `OAUTH2_JWT_START_HERE.md`
- Quick overview
- Where to find what
- First steps

### 📘 Complete Implementation Guide
**File:** `OAUTH2_JWT_IMPLEMENTATION_GUIDE.md`
- Detailed architecture overview
- Complete API documentation
- Security best practices
- Frontend integration guide
- Testing instructions

### ⚡ Quick Reference Card
**File:** `OAUTH2_JWT_QUICK_REF.md`
- Quick commands
- Common code snippets
- Configuration examples
- Troubleshooting tips

### ✅ Summary & Checklist
**File:** `OAUTH2_JWT_COMPLETE_SUMMARY.md`
- What was implemented
- Project structure
- Testing checklist
- Next steps

---

## 🎯 What to Do First

### 1️⃣ Start the Backend (2 minutes)

```bash
cd backend/clothes-shop-backend
mvn spring-boot:run
```

Wait for: `Started ClothesShopApplication`

### 2️⃣ Test It Works (1 minute)

Open browser to: `http://localhost:8080/actuator/health`

Should see:
```json
{"status":"UP"}
```

### 3️⃣ Try the Frontend (2 minutes)

Open in browser:
```
frontend/register.html
```

Register a new account and you're done! ✅

---

## 📁 Key Files You'll Use

### Backend Files

```
src/main/java/com/ecommerce/clothesshop/
├── config/SecurityConfig.java        # ⚙️ Security setup
├── security/JwtTokenProvider.java    # 🔑 Token management
├── controller/AuthController.java    # 🌐 Auth endpoints
└── service/UserService.java          # 👤 User logic
```

### Frontend Files

```
frontend/
├── js/auth-api.js                    # 📡 API client
├── login.html                        # 🔐 Login page
└── register.html                     # ✍️ Registration page
```

### Documentation

```
📄 OAUTH2_JWT_IMPLEMENTATION_GUIDE.md  # Full docs
⚡ OAUTH2_JWT_QUICK_REF.md             # Quick reference
✅ OAUTH2_JWT_COMPLETE_SUMMARY.md      # Summary
📮 Clothes_Shop_Authentication.postman_collection.json
```

---

## 🔗 Quick Links

### Test Endpoints

| What | URL | Method |
|------|-----|--------|
| **Register** | http://localhost:8080/api/auth/register | POST |
| **Login** | http://localhost:8080/api/auth/login | POST |
| **Get Profile** | http://localhost:8080/api/auth/me | GET (+ token) |
| **Products** | http://localhost:8080/api/products | GET |

### Frontend Pages

| Page | File |
|------|------|
| **Registration** | `frontend/register.html` |
| **Login** | `frontend/login.html` |

---

## 🧪 Quick Test (cURL)

### Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

Copy the `accessToken` from the response!

### Use Token
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

---

## 🎓 Learn the Flow

### Registration Flow
```
User fills form → Frontend sends to backend → 
Backend validates → Creates user → Hashes password →
Generates tokens → Returns to frontend → Stores tokens
```

### Login Flow
```
User enters credentials → Frontend sends to backend →
Backend validates → Generates tokens → 
Returns to frontend → Stores tokens
```

### Protected Request Flow
```
Frontend reads token → Adds to Authorization header →
Sends request → Backend validates token →
Processes request → Returns response
```

---

## 📊 What's Protected?

### ❌ Public (No Token Needed)
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/products`
- `GET /api/products/{id}`

### ✅ Protected (Token Required)
- `GET /api/auth/me`
- `POST /api/cart/add`
- `GET /api/cart/{userId}`
- `POST /api/checkout/create`
- `GET /api/orders/user/{id}`

### 👑 Admin Only
- `POST /api/admin/products`
- `PUT /api/admin/products`
- `DELETE /api/admin/products/{id}`

---

## 🔧 Configuration

### JWT Settings (application.properties)

```properties
# JWT secret key (change in production!)
jwt.secret=YOUR_SECRET_KEY_HERE

# Token expiration times
jwt.expiration=86400000              # 24 hours
jwt.refresh-expiration=604800000     # 7 days
```

### Generate New Secret Key

**PowerShell:**
```powershell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
```

**Bash:**
```bash
openssl rand -base64 64
```

---

## 🐛 Troubleshooting

### Backend won't start?
```bash
# Clean and rebuild
mvn clean install -DskipTests
```

### Token errors (401)?
- Token expired → Use refresh token
- Invalid token → Login again
- No token → Check Authorization header

### CORS errors?
- Update `WebConfig.java` with your frontend URL

---

## 📚 Next Steps

### Beginner Track
1. ✅ Read this file (you're here!)
2. 📖 Open `OAUTH2_JWT_QUICK_REF.md`
3. 🧪 Test with Postman collection
4. 🌐 Try the HTML pages

### Advanced Track
1. 📘 Read `OAUTH2_JWT_IMPLEMENTATION_GUIDE.md`
2. 🔍 Study the backend code
3. ⚛️ Check React example in `frontend/js/auth-react-example.jsx`
4. 🎨 Customize for your needs

---

## 🎯 Common Tasks

### How do I...

**...register a new user?**
→ Use `POST /api/auth/register` or open `frontend/register.html`

**...login?**
→ Use `POST /api/auth/login` or open `frontend/login.html`

**...add authentication to my React app?**
→ Check `frontend/js/auth-react-example.jsx`

**...test the API?**
→ Import `Clothes_Shop_Authentication.postman_collection.json` into Postman

**...change token expiration time?**
→ Update `jwt.expiration` in `application.properties`

**...add a new protected endpoint?**
→ Update `SecurityConfig.java` and add `.authenticated()` rule

---

## 💡 Tips

### Security
- ✅ Always use HTTPS in production
- ✅ Change the default JWT secret
- ✅ Don't commit secrets to Git
- ✅ Set appropriate token expiration times

### Development
- ✅ Use Postman collection for testing
- ✅ Check browser console for frontend errors
- ✅ Check backend logs for server errors
- ✅ Test with both valid and invalid credentials

### Production
- ✅ Use environment variables for secrets
- ✅ Enable CORS only for your domain
- ✅ Add rate limiting on auth endpoints
- ✅ Monitor failed login attempts

---

## 📞 Need Help?

1. **Quick fix needed?**
   → Check `OAUTH2_JWT_QUICK_REF.md`

2. **Want to understand how it works?**
   → Read `OAUTH2_JWT_IMPLEMENTATION_GUIDE.md`

3. **Need to test something?**
   → Use Postman collection

4. **Building a React app?**
   → Copy from `frontend/js/auth-react-example.jsx`

---

## ✅ Success Checklist

- [ ] Backend is running
- [ ] Can access http://localhost:8080/actuator/health
- [ ] Registered a test user
- [ ] Logged in successfully
- [ ] Token is stored in localStorage
- [ ] Can access protected endpoint with token
- [ ] Postman collection works

All checked? **You're ready to go! 🚀**

---

## 🎉 You're All Set!

Your authentication system is:
- ✅ **Secure** - JWT tokens, BCrypt hashing
- ✅ **Scalable** - Stateless authentication
- ✅ **Modern** - OAuth 2 + JWT standard
- ✅ **Complete** - Frontend & backend ready
- ✅ **Documented** - Full guides included
- ✅ **Tested** - Postman collection ready

**Happy coding! 🎊**

---

**Quick Navigation:**
- 📘 [Full Guide](OAUTH2_JWT_IMPLEMENTATION_GUIDE.md)
- ⚡ [Quick Reference](OAUTH2_JWT_QUICK_REF.md)
- ✅ [Summary](OAUTH2_JWT_COMPLETE_SUMMARY.md)

**Created:** January 28, 2026 | **Version:** 1.0.0
