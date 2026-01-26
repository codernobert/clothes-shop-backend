# ✅ Railway Deployment Checklist
## 📦 BACKEND DEPLOYMENT
### Phase 1: Preparation (Completed ✅)
- [x] Create `Procfile` for Railway
- [x] Create `system.properties` (Java 17)
- [x] Create `railway.json` configuration
- [x] Create `.env.example` template
- [x] Create `.gitignore`
- [x] Update `application.properties` with env vars
- [x] Create deployment documentation
### Phase 2: Repository Setup (Todo 📝)
- [ ] Initialize Git repository (if not done)
  ```bash
  git init
  git add .
  git commit -m "Initial commit - prepared for Railway deployment"
  ```
- [ ] Create GitHub repository
- [ ] Push code to GitHub
  ```bash
  git remote add origin https://github.com/your-username/your-repo.git
  git branch -M main
  git push -u origin main
  ```
### Phase 3: Railway Backend Setup (Todo 📝)
- [ ] Sign up at [railway.app](https://railway.app)
- [ ] Click "New Project"
- [ ] Select "Deploy from GitHub repo"
- [ ] Authorize Railway to access GitHub
- [ ] Select your backend repository
- [ ] Wait for Railway to detect Java/Maven project
### Phase 4: Database Setup (Todo 📝)
- [ ] In Railway project, click "+ New"
- [ ] Select "Database" → "PostgreSQL"
- [ ] Wait for database to provision
- [ ] Verify database environment variables are available:
  - `PGHOST`
  - `PGPORT`
  - `PGDATABASE`
  - `PGUSER`
  - `PGPASSWORD`
### Phase 5: Environment Variables (Todo 📝)
- [ ] Go to Railway project settings
- [ ] Click "Variables" tab
- [ ] Add required variables:
  ```
  PAYSTACK_API_KEY=sk_test_your_test_key_here
  PAYSTACK_CALLBACK_URL=http://localhost:8000/payment_callback.php
  ```
  (Update later with production keys and frontend URL)
### Phase 6: Deploy & Test (Todo 📝)
- [ ] Railway automatically deploys
- [ ] Wait for build to complete (check logs)
- [ ] Note your Railway URL: `https://your-app.railway.app`
- [ ] Test health endpoint:
  ```bash
  curl https://your-app.railway.app/actuator/health
  ```
- [ ] Test products endpoint:
  ```bash
  curl https://your-app.railway.app/api/products
  ```
- [ ] Check Railway logs for any errors
### Phase 7: Database Verification (Todo 📝)
- [ ] Go to Railway → PostgreSQL service
- [ ] Click "Data" tab
- [ ] Verify tables were created:
  - `products`
  - `cart_items`
  - `orders`
  - `order_items`
  - `payments`
---
## 🎨 FRONTEND DEPLOYMENT
### Phase 1: Repository Preparation (Todo 📝)
- [ ] Create **separate** GitHub repository for frontend
- [ ] Copy frontend files to new repository:
  - `frontend/` folder contents
  - Include all PHP files
  - Include `ajax/` and `admin/` folders
  - Include `includes/` folder
### Phase 2: Frontend Configuration (Todo 📝)
- [ ] Update `config.php` to use environment variables:
  ```php
  define('API_BASE_URL', getenv('BACKEND_API_URL') ?: 'http://localhost:8080/api');
  ```
- [ ] Create `composer.json`:
  ```json
  {
    "require": {
      "php": "^8.1"
    }
  }
  ```
- [ ] Create `Procfile`:
  ```
  web: vendor/bin/heroku-php-apache2
  ```
### Phase 3: Deploy Frontend to Railway (Todo 📝)
- [ ] Create new Railway project
- [ ] Select "Deploy from GitHub repo"
- [ ] Choose frontend repository
- [ ] Wait for deployment
### Phase 4: Configure Frontend Environment (Todo 📝)
- [ ] Add environment variable:
  ```
  BACKEND_API_URL=https://your-backend.railway.app/api
  ```
- [ ] Note frontend URL: `https://your-frontend.railway.app`
### Phase 5: Update Backend for Frontend (Todo 📝)
- [ ] Update backend `WebConfig.java`:
  ```java
  .allowedOrigins("https://your-frontend.railway.app")
  ```
- [ ] Update backend environment variable:
  ```
  PAYSTACK_CALLBACK_URL=https://your-frontend.railway.app/payment_callback.php
  ```
- [ ] Push changes to trigger redeploy
---
## 🧪 TESTING PHASE
### Backend API Tests (Todo 📝)
- [ ] Health check responds
- [ ] Get all products works
- [ ] Get product by ID works
- [ ] Search products works
- [ ] Filter products works
- [ ] Cart operations work
- [ ] Order creation works
- [ ] Payment initialization works
### Frontend Tests (Todo 📝)
- [ ] Homepage loads
- [ ] Products display correctly
- [ ] Product details page works
- [ ] Add to cart works
- [ ] Cart page displays items
- [ ] Checkout page loads
- [ ] Payment form appears
- [ ] Payment flow completes
### Integration Tests (Todo 📝)
- [ ] Frontend can call backend APIs
- [ ] CORS works correctly
- [ ] Payment callback receives data
- [ ] Orders save to database
- [ ] Payment status updates correctly
---
## 🔐 PRODUCTION READINESS
### Security (Todo 📝)
- [ ] Switch to production Paystack keys
  - [ ] Backend: `PAYSTACK_API_KEY=sk_live_...`
  - [ ] Frontend: `PAYSTACK_PUBLIC_KEY=pk_live_...`
- [ ] Restrict CORS to frontend domain only
- [ ] Enable Railway database backups
- [ ] Set up database snapshots
- [ ] Review all environment variables
### Monitoring (Todo 📝)
- [ ] Set up Railway deployment notifications
- [ ] Configure uptime monitoring (e.g., UptimeRobot)
- [ ] Set up error tracking (optional)
- [ ] Monitor Railway usage and costs
### Documentation (Todo 📝)
- [ ] Document backend URL
- [ ] Document frontend URL
- [ ] Document admin credentials
- [ ] Document Paystack webhook endpoints
- [ ] Create runbook for common issues
---
## 📊 SUCCESS METRICS
You're ready to go live when:
- ✅ Backend health endpoint responds
- ✅ All API endpoints work
- ✅ Frontend loads and displays products
- ✅ Complete customer journey works end-to-end
- ✅ Payment flow completes successfully
- ✅ Orders appear in database
- ✅ No CORS errors
- ✅ Production Paystack keys configured
- ✅ Monitoring is active
---
## 📚 QUICK REFERENCE
### Backend URL Pattern
`https://your-backend-name.railway.app`
### Frontend URL Pattern
`https://your-frontend-name.railway.app`
### Important Endpoints
- Health: `/actuator/health`
- Products: `/api/products`
- Cart: `/api/cart`
- Orders: `/api/orders`
- Payments: `/api/payments`
### Cost Tracking
- Railway Hobby: `/month (` credit)
- PostgreSQL: ~`-2/month
- Total for both services: ~`-10/month
---
## 🆘 TROUBLESHOOTING GUIDE
### Backend won't start
1. Check Railway logs for errors
2. Verify Java version is 17
3. Ensure PostgreSQL is running
4. Check environment variables
### Frontend can't reach backend
1. Verify `BACKEND_API_URL` is set
2. Check backend CORS configuration
3. Verify backend is running
4. Check browser console for CORS errors
### Payment issues
1. Verify Paystack keys are correct
2. Check callback URL is accessible
3. Review Paystack dashboard logs
4. Ensure HTTPS is being used
---
**Current Status**: Phase 1 Complete ✅  
**Next Step**: Phase 2 - Push to GitHub 📝  
**Time Estimate**: 30-60 minutes total
**Start Here**: `RAILWAY_QUICK_DEPLOY.md`
