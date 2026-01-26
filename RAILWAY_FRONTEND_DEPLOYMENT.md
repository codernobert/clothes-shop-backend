# 🎨 Railway Deployment Guide - Frontend (PHP)
## Overview
Your frontend is a PHP application with MySQL dependency. This guide covers deploying it to Railway in a separate project from the backend.
## 📋 Prerequisites
1. **Railway Account** at [railway.app](https://railway.app)
2. **Separate GitHub Repository** for your frontend code
3. **Backend API URL** from your deployed backend (e.g., `https://your-backend.railway.app`)
## 🚀 Deployment Steps
### Step 1: Prepare Frontend Repository
Your frontend folder structure should be in its own repository:
```
frontend-repo/
├── index.php
├── products.php
├── product_detail.php
├── cart.php
├── checkout.php
├── orders.php
├── payment_callback.php
├── config.php
├── admin/
│   ├── index.php
│   ├── products.php
│   ├── add_product.php
│   ├── edit_product.php
│   └── orders.php
├── ajax/
│   ├── add_to_cart.php
│   ├── update_cart.php
│   ├── remove_from_cart.php
│   ├── get_cart_count.php
│   ├── create_payment.php
│   ├── verify_payment.php
│   └── confirm_payment.php
└── includes/
    ├── header.php
    └── footer.php
```
### Step 2: Update config.php
Update your `config.php` to use environment variables:
```php
<?php
// API Configuration
define('API_BASE_URL', getenv('BACKEND_API_URL') ?: 'http://localhost:8080/api');
// Paystack Configuration (if used directly in frontend)
define('PAYSTACK_PUBLIC_KEY', getenv('PAYSTACK_PUBLIC_KEY') ?: 'pk_test_your_test_key');
// Session Configuration
session_start();
// CORS Headers (if needed)
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization');
?>
```
### Step 3: Create Railway Configuration Files
#### Create `Procfile`:
```
web: vendor/bin/heroku-php-apache2
```
#### Create `composer.json` (if not exists):
```json
{
    "name": "your-name/clothes-shop-frontend",
    "description": "E-commerce Frontend",
    "require": {
        "php": "^8.1"
    }
}
```
#### Create `.htaccess` (for URL rewriting):
```apache
<IfModule mod_rewrite.c>
    RewriteEngine On
    RewriteBase /
    # Redirect to HTTPS
    RewriteCond %{HTTPS} off
    RewriteRule ^(.*)$ https://%{HTTP_HOST}%{REQUEST_URI} [L,R=301]
    # Handle PHP files
    RewriteCond %{REQUEST_FILENAME} !-f
    RewriteCond %{REQUEST_FILENAME} !-d
    RewriteRule ^(.*)$ index.php [L]
</IfModule>
# Enable CORS
<IfModule mod_headers.c>
    Header set Access-Control-Allow-Origin "*"
    Header set Access-Control-Allow-Methods "GET, POST, PUT, DELETE, OPTIONS"
    Header set Access-Control-Allow-Headers "Content-Type, Authorization"
</IfModule>
```
### Step 4: Deploy to Railway
1. **Create New Project**
   - Go to [Railway Dashboard](https://railway.app/new)
   - Click "Deploy from GitHub repo"
   - Select your **frontend repository**
   - Railway will detect it's a PHP project
2. **Configure Build Settings**
   - Railway should automatically detect PHP
   - Build Command: `composer install` (if you have dependencies)
   - Start Command: Usually auto-detected for PHP
### Step 5: Configure Environment Variables
In Railway project settings, add:
```
BACKEND_API_URL=https://your-backend.railway.app/api
PAYSTACK_PUBLIC_KEY=pk_live_your_production_public_key
```
### Step 6: Deploy & Test
1. Railway will automatically build and deploy
2. You'll get a URL like: `https://your-frontend.railway.app`
3. Test the application:
   - Browse products
   - Add to cart
   - Complete checkout
   - Verify payment flow
### Step 7: Update Backend CORS
In your backend `WebConfig.java`, update CORS to allow your frontend:
```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("https://your-frontend.railway.app")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
}
```
Redeploy backend after this change.
### Step 8: Update Paystack Callback URL
Update your backend environment variable:
```
PAYSTACK_CALLBACK_URL=https://your-frontend.railway.app/payment_callback.php
```
## 🔧 Alternative: Deploy Frontend Elsewhere
If Railway doesn't support PHP well, consider these alternatives:
### Option 1: Heroku (PHP-friendly)
- Better PHP support with Apache/Nginx
- Similar deployment process
- Free tier available
### Option 2: Vercel (Convert to Static + API)
- Deploy static HTML/CSS/JS
- Use backend API for all dynamic content
- Excellent performance
### Option 3: Traditional Hosting
- Shared hosting (e.g., Namecheap, Hostinger)
- Upload via FTP
- Update config.php with backend API URL
## 📋 Verification Checklist
- [ ] Frontend repository in GitHub
- [ ] `config.php` uses environment variables
- [ ] Railway project created for frontend
- [ ] Environment variables configured
- [ ] Frontend deployed successfully
- [ ] Can access frontend URL
- [ ] Products load from backend API
- [ ] Cart functionality works
- [ ] Checkout process completes
- [ ] Payment callback receives data
- [ ] Backend CORS allows frontend domain
- [ ] Paystack callback URL updated
## 🐛 Troubleshooting
### Frontend can't reach backend
- Check `BACKEND_API_URL` environment variable
- Verify backend is running and accessible
- Check backend CORS configuration
- Look at browser console for CORS errors
### PHP errors
- Check Railway logs for PHP errors
- Verify PHP version compatibility (8.1+)
- Ensure all required PHP extensions are available
### Payment callback fails
- Verify `PAYSTACK_CALLBACK_URL` in backend
- Check that callback URL is publicly accessible
- Review Paystack dashboard webhook logs
### Session issues
- Railway may reset sessions on redeploy
- Consider using database-backed sessions
- Or use backend API for session management
## 💡 Best Practices
1. **Use Backend API**: Let backend handle all business logic and database operations
2. **Secure API Keys**: Never expose secret keys in frontend code
3. **HTTPS Only**: Railway provides HTTPS by default - use it
4. **Error Handling**: Show user-friendly errors, log details server-side
5. **Caching**: Use Railway's CDN for static assets
6. **Monitoring**: Set up uptime monitoring (e.g., UptimeRobot)
## 🔄 Continuous Deployment
Railway automatically redeploys when you push to your main branch:
```bash
# Make changes to frontend
git add .
git commit -m "Update product page layout"
git push origin main
# Railway automatically detects and redeploys
```
## 💰 Cost Estimate
Railway Pricing for Frontend:
- **Hobby Plan**: `/month (includes ` credit)
- **Bandwidth**: Usually covered in credit for small apps
- **Build time**: Minimal for PHP apps
## 📚 Additional Resources
- [Railway PHP Docs](https://docs.railway.app/guides/php)
- [Railway Environment Variables](https://docs.railway.app/develop/variables)
- [Paystack Webhooks](https://paystack.com/docs/payments/webhooks/)
## ✅ Complete Setup Checklist
### Backend (Already Done)
- [x] Backend deployed to Railway
- [x] PostgreSQL database configured
- [x] Environment variables set
- [x] API endpoints working
### Frontend (Todo)
- [ ] Create separate GitHub repository for frontend
- [ ] Update `config.php` with environment variables
- [ ] Create `Procfile` and `composer.json`
- [ ] Push to GitHub
- [ ] Create Railway project from frontend repo
- [ ] Configure environment variables (`BACKEND_API_URL`)
- [ ] Verify deployment
- [ ] Test all features
### Integration (Todo)
- [ ] Update backend CORS for frontend domain
- [ ] Update Paystack callback URL
- [ ] Test complete customer journey
- [ ] Switch to production Paystack keys
- [ ] Set up monitoring
---
**Ready to deploy?** Start with Step 1: Create a separate GitHub repository for your frontend code!
