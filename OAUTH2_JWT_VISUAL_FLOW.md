# OAuth 2 + JWT Visual Flow Diagram

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║                    CLOTHES SHOP - AUTHENTICATION FLOW                         ║
╚═══════════════════════════════════════════════════════════════════════════════╝

┌─────────────────────────────────────────────────────────────────────────────┐
│                          1. USER REGISTRATION                                │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────┐                                              ┌──────────┐
    │ Frontend │                                              │ Backend  │
    │ (HTML/JS)│                                              │ (Spring) │
    └────┬─────┘                                              └────┬─────┘
         │                                                          │
         │  1. User fills registration form                        │
         │     (email, password, firstName, lastName)              │
         │                                                          │
         │  2. POST /api/auth/register                             │
         │─────────────────────────────────────────────────────────>│
         │     {                                                    │
         │       "email": "user@example.com",                       │
         │       "password": "password123",                         │
         │       "firstName": "John",                               │
         │       "lastName": "Doe"                                  │
         │     }                                                    │
         │                                                          │
         │                          3. UserService.registerUser()  │
         │                              ├─ Validate email           │
         │                              ├─ Check if user exists     │
         │                              ├─ Hash password (BCrypt)   │
         │                              ├─ Save to database         │
         │                              └─ Generate JWT tokens      │
         │                                                          │
         │  4. Response with tokens                                │
         │<─────────────────────────────────────────────────────────│
         │     {                                                    │
         │       "accessToken": "eyJhbGc...",                       │
         │       "refreshToken": "eyJhbGc...",                      │
         │       "userId": 1,                                       │
         │       "email": "user@example.com",                       │
         │       "firstName": "John",                               │
         │       "lastName": "Doe",                                 │
         │       "role": "CUSTOMER"                                 │
         │     }                                                    │
         │                                                          │
         │  5. Store tokens in localStorage                        │
         │     localStorage.setItem('accessToken', ...)            │
         │     localStorage.setItem('refreshToken', ...)           │
         │                                                          │
         │  6. Redirect to dashboard/home                          │
         │                                                          │


┌─────────────────────────────────────────────────────────────────────────────┐
│                             2. USER LOGIN                                    │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────┐                                              ┌──────────┐
    │ Frontend │                                              │ Backend  │
    └────┬─────┘                                              └────┬─────┘
         │                                                          │
         │  1. User enters credentials                             │
         │                                                          │
         │  2. POST /api/auth/login                                │
         │─────────────────────────────────────────────────────────>│
         │     {                                                    │
         │       "email": "user@example.com",                       │
         │       "password": "password123"                          │
         │     }                                                    │
         │                                                          │
         │                          3. UserService.loginUser()     │
         │                              ├─ Find user by email       │
         │                              ├─ Verify password (BCrypt) │
         │                              ├─ Generate new JWT tokens  │
         │                              └─ Return tokens            │
         │                                                          │
         │  4. Response with tokens                                │
         │<─────────────────────────────────────────────────────────│
         │     (Same format as registration)                       │
         │                                                          │
         │  5. Store tokens                                        │
         │  6. Redirect to dashboard                               │
         │                                                          │


┌─────────────────────────────────────────────────────────────────────────────┐
│                       3. PROTECTED API REQUEST                               │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────┐                                              ┌──────────┐
    │ Frontend │                                              │ Backend  │
    └────┬─────┘                                              └────┬─────┘
         │                                                          │
         │  1. User clicks "Add to Cart"                           │
         │                                                          │
         │  2. Get token from localStorage                         │
         │     const token = localStorage.getItem('accessToken')   │
         │                                                          │
         │  3. POST /api/cart/add                                  │
         │     Authorization: Bearer eyJhbGc...                    │
         │─────────────────────────────────────────────────────────>│
         │     {                                                    │
         │       "productId": 1,                                    │
         │       "quantity": 2                                      │
         │     }                                                    │
         │                                                          │
         │                          ┌─────────────────────────┐    │
         │                          │ JwtAuthenticationFilter │    │
         │                          └───────────┬─────────────┘    │
         │                                      │                  │
         │                          4. Extract token from header   │
         │                          5. Validate token signature    │
         │                          6. Check expiration            │
         │                          7. Extract user info           │
         │                          8. Set authentication context  │
         │                                      │                  │
         │                          ┌───────────▼─────────────┐    │
         │                          │   SecurityConfig        │    │
         │                          │   Check permissions     │    │
         │                          └───────────┬─────────────┘    │
         │                                      │                  │
         │                          9. Process request             │
         │                             CartService.addToCart()     │
         │                                                          │
         │  10. Response                                           │
         │<─────────────────────────────────────────────────────────│
         │     {                                                    │
         │       "success": true,                                   │
         │       "message": "Item added to cart"                    │
         │     }                                                    │
         │                                                          │


┌─────────────────────────────────────────────────────────────────────────────┐
│                       4. TOKEN REFRESH FLOW                                  │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────┐                                              ┌──────────┐
    │ Frontend │                                              │ Backend  │
    └────┬─────┘                                              └────┬─────┘
         │                                                          │
         │  1. Access token expired (24 hours passed)              │
         │                                                          │
         │  2. API request returns 401 Unauthorized                │
         │<─────────────────────────────────────────────────────────│
         │                                                          │
         │  3. Get refresh token from localStorage                 │
         │                                                          │
         │  4. POST /api/auth/refresh                              │
         │─────────────────────────────────────────────────────────>│
         │     {                                                    │
         │       "refreshToken": "eyJhbGc..."                       │
         │     }                                                    │
         │                                                          │
         │                          5. Validate refresh token      │
         │                          6. Generate new access token   │
         │                          7. Generate new refresh token  │
         │                                                          │
         │  8. Response with new tokens                            │
         │<─────────────────────────────────────────────────────────│
         │     {                                                    │
         │       "accessToken": "eyJNew...",                        │
         │       "refreshToken": "eyJNew..."                        │
         │     }                                                    │
         │                                                          │
         │  9. Update localStorage                                 │
         │  10. Retry original request with new token              │
         │                                                          │


┌─────────────────────────────────────────────────────────────────────────────┐
│                           5. LOGOUT FLOW                                     │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────┐                                              ┌──────────┐
    │ Frontend │                                              │ Backend  │
    └────┬─────┘                                              └────┬─────┘
         │                                                          │
         │  1. User clicks "Logout"                                │
         │                                                          │
         │  2. POST /api/auth/logout (optional)                    │
         │─────────────────────────────────────────────────────────>│
         │     Authorization: Bearer eyJhbGc...                    │
         │                                                          │
         │  3. Response                                            │
         │<─────────────────────────────────────────────────────────│
         │                                                          │
         │  4. Remove tokens from localStorage                     │
         │     localStorage.removeItem('accessToken')              │
         │     localStorage.removeItem('refreshToken')             │
         │     localStorage.removeItem('user')                     │
         │                                                          │
         │  5. Redirect to login page                              │
         │                                                          │


╔═══════════════════════════════════════════════════════════════════════════════╗
║                           JWT TOKEN STRUCTURE                                 ║
╚═══════════════════════════════════════════════════════════════════════════════╝

┌─────────────────────────────────────────────────────────────────────────────┐
│                          ACCESS TOKEN (24 hours)                             │
└─────────────────────────────────────────────────────────────────────────────┘

    eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjEsImVtYWlsIjoidXNlckBleGFtcGxlLmNvbSIsInJvbGUiOiJDVVNUT01FUiIsInN1YiI6InVzZXJAZXhhbXBsZS5jb20iLCJpYXQiOjE3MDY0NTA0MDAsImV4cCI6MTcwNjUzNjgwMH0.signature

    ┌─────────────┐   ┌─────────────────────────────────┐   ┌─────────────┐
    │   HEADER    │ . │           PAYLOAD                │ . │  SIGNATURE  │
    └─────────────┘   └─────────────────────────────────┘   └─────────────┘

    HEADER:
    {
      "alg": "HS512"
    }

    PAYLOAD:
    {
      "userId": 1,
      "email": "user@example.com",
      "role": "CUSTOMER",
      "sub": "user@example.com",
      "iat": 1706450400,          # Issued at
      "exp": 1706536800           # Expires at (24 hours later)
    }

    SIGNATURE:
    HMACSHA512(
      base64UrlEncode(header) + "." + base64UrlEncode(payload),
      secret
    )


┌─────────────────────────────────────────────────────────────────────────────┐
│                         REFRESH TOKEN (7 days)                               │
└─────────────────────────────────────────────────────────────────────────────┘

    Similar structure to access token but with:
    - Longer expiration (7 days)
    - "type": "refresh" in payload
    - No role information


╔═══════════════════════════════════════════════════════════════════════════════╗
║                          SECURITY ARCHITECTURE                                ║
╚═══════════════════════════════════════════════════════════════════════════════╝

    ┌─────────────────────────────────────────────────────────────────┐
    │                         FRONTEND                                │
    │                                                                 │
    │  ┌───────────────┐         ┌──────────────┐                   │
    │  │  localStorage │         │  auth-api.js │                   │
    │  │               │◄────────┤              │                   │
    │  │  - accessToken│         │  - register()│                   │
    │  │  - refreshToken        │  - login()   │                   │
    │  │  - user       │         │  - logout()  │                   │
    │  └───────────────┘         └──────┬───────┘                   │
    │                                    │                            │
    └────────────────────────────────────┼────────────────────────────┘
                                         │
                                         │ HTTP Requests
                                         │ Authorization: Bearer <token>
                                         │
    ┌────────────────────────────────────┼────────────────────────────┐
    │                         BACKEND    │                            │
    │                                    ▼                            │
    │  ┌─────────────────────────────────────────────────────────┐  │
    │  │         JwtAuthenticationFilter                         │  │
    │  │  1. Extract token from Authorization header             │  │
    │  │  2. Validate token signature                            │  │
    │  │  3. Check expiration                                    │  │
    │  │  4. Extract user info                                   │  │
    │  │  5. Set security context                                │  │
    │  └───────────────────────┬─────────────────────────────────┘  │
    │                          │                                     │
    │  ┌───────────────────────▼─────────────────────────────────┐  │
    │  │              SecurityConfig                             │  │
    │  │  - Public: /api/auth/**, /api/products (GET)            │  │
    │  │  - Protected: /api/cart/**, /api/checkout/**            │  │
    │  │  - Admin: /api/admin/**                                 │  │
    │  └───────────────────────┬─────────────────────────────────┘  │
    │                          │                                     │
    │  ┌───────────────────────▼─────────────────────────────────┐  │
    │  │              Controllers                                │  │
    │  │  - AuthController: register, login, refresh             │  │
    │  │  - CartController: add, remove, get cart                │  │
    │  │  - OrderController: create, get orders                  │  │
    │  └───────────────────────┬─────────────────────────────────┘  │
    │                          │                                     │
    │  ┌───────────────────────▼─────────────────────────────────┐  │
    │  │              Services                                   │  │
    │  │  - UserService: register, login, refresh                │  │
    │  │  - CartService: cart operations                         │  │
    │  │  - OrderService: order operations                       │  │
    │  └───────────────────────┬─────────────────────────────────┘  │
    │                          │                                     │
    │  ┌───────────────────────▼─────────────────────────────────┐  │
    │  │              Database (PostgreSQL)                      │  │
    │  │  - users table                                          │  │
    │  │  - products table                                       │  │
    │  │  - orders table                                         │  │
    │  └─────────────────────────────────────────────────────────┘  │
    │                                                                 │
    └─────────────────────────────────────────────────────────────────┘


╔═══════════════════════════════════════════════════════════════════════════════╗
║                          ROLE-BASED ACCESS CONTROL                            ║
╚═══════════════════════════════════════════════════════════════════════════════╝

    ┌─────────────┐       ┌──────────────────────────────────────┐
    │  CUSTOMER   │──────>│  - View products                     │
    │             │       │  - Add to cart                       │
    │  (Default)  │       │  - Create orders                     │
    │             │       │  - View own orders                   │
    │             │       │  - Update profile                    │
    └─────────────┘       └──────────────────────────────────────┘

    ┌─────────────┐       ┌──────────────────────────────────────┐
    │    ADMIN    │──────>│  All CUSTOMER permissions +          │
    │             │       │  - Add products                      │
    │             │       │  - Update products                   │
    │             │       │  - Delete products                   │
    │             │       │  - View all orders                   │
    │             │       │  - Manage users                      │
    └─────────────┘       └──────────────────────────────────────┘


Created: January 28, 2026
Version: 1.0.0
```
