# 🔧 FRONTEND FIX - Files in /frontend/ Subdirectory
## ✅ ISSUE IDENTIFIED!
**Problem:** Your frontend files are being served from:
- ❌ Expected: `https://clothes-shop-frontend-production.up.railway.app/`
- ✅ Actually at: `https://clothes-shop-frontend-production.up.railway.app/frontend/`
**Root Cause:** Railway is deploying your entire repository structure, including the `frontend/` folder. Your PHP files are inside `frontend/` instead of at the root.
---
## 🚀 SOLUTION: Configure Railway Root Directory
### Option 1: Set Root Directory in Railway (RECOMMENDED - 2 minutes)
This tells Railway to serve files from the `/frontend` folder as the root.
**Steps:**
1. Go to Railway Dashboard
2. Click your **Frontend Service**
3. Go to **Settings** tab
4. Scroll to **Service Settings** section
5. Find **Root Directory** field
6. Set it to: `frontend`
7. Click **Save** or **Deploy**
8. Wait ~2 minutes for redeploy
**Result:** Files will now be served from root
- ✅ `https://clothes-shop-frontend-production.up.railway.app/` → Will show index.php
- ✅ `https://clothes-shop-frontend-production.up.railway.app/products.php` → Will work
---
### Option 2: Restructure Repository (Alternative)
Move all files from `frontend/` folder to repository root.
**In your frontend repository:**
```bash
# Move all files from frontend/ to root
mv frontend/* .
rmdir frontend
# Commit and push
git add .
git commit -m "Move frontend files to root directory"
git push origin main
```
**Repository structure should become:**
```
frontend-repo/
├── index.php          ← At root now
├── config.php
├── products.php
├── cart.php
├── checkout.php
└── admin/
    └── ...
```
---
### Option 3: Use Railway.toml Configuration File
Create a `railway.toml` in your frontend repository:
```toml
[build]
builder = "NIXPACKS"
[deploy]
startCommand = "php -S 0.0.0.0:\$PORT -t frontend"
```
This tells Railway to serve from the `frontend` directory.
---
## ⚡ QUICK FIX (Recommended)
**Use Option 1 - it's fastest and doesn't require code changes:**
1. Railway Dashboard → Frontend Service
2. Settings → Root Directory → `frontend`
3. Save
4. Wait for automatic redeploy (~2 minutes)
5. Test: `https://clothes-shop-frontend-production.up.railway.app`
---
## ✅ AFTER APPLYING FIX
### Test these URLs work:
```bash
# Homepage
curl https://clothes-shop-frontend-production.up.railway.app/
# Products page
curl https://clothes-shop-frontend-production.up.railway.app/products.php
# Admin
curl https://clothes-shop-frontend-production.up.railway.app/admin/
```
All should return HTML (not blank, not 404).
---
## 🎯 CURRENT URLS (Temporary Workaround)
**Until you fix Railway settings, your frontend IS accessible at:**
**Working URLs:**
- Homepage: `https://clothes-shop-frontend-production.up.railway.app/frontend/index.php`
- Products: `https://clothes-shop-frontend-production.up.railway.app/frontend/products.php`
- Cart: `https://clothes-shop-frontend-production.up.railway.app/frontend/cart.php`
- Checkout: `https://clothes-shop-frontend-production.up.railway.app/frontend/checkout.php`
- Admin: `https://clothes-shop-frontend-production.up.railway.app/frontend/admin/`
**Test it now:**
```bash
curl https://clothes-shop-frontend-production.up.railway.app/frontend/index.php
```
You should see HTML content!
---
## 🔧 ALSO UPDATE BACKEND CALLBACK URL
Since your frontend is at `/frontend/`, update your backend environment variable:
**Railway Backend Service → Variables:**
**Current (wrong):**
```
PAYSTACK_CALLBACK_URL=https://clothes-shop-frontend-production.up.railway.app/payment_callback.php
```
**Update to:**
```
PAYSTACK_CALLBACK_URL=https://clothes-shop-frontend-production.up.railway.app/frontend/payment_callback.php
```
**After fixing Railway root directory, change it back to:**
```
PAYSTACK_CALLBACK_URL=https://clothes-shop-frontend-production.up.railway.app/payment_callback.php
```
---
## 📋 COMPLETE FIX CHECKLIST
- [ ] Railway Frontend → Settings → Root Directory → Set to `frontend`
- [ ] Wait for redeploy (~2 minutes)
- [ ] Test: `https://clothes-shop-frontend-production.up.railway.app/` (should show homepage)
- [ ] Update backend callback URL (temporarily to `/frontend/payment_callback.php`)
- [ ] Test complete user journey
- [ ] Once working, can optionally restructure repo to move files to root
---
## 🎉 QUICK WIN
**You can test your frontend RIGHT NOW at:**
`https://clothes-shop-frontend-production.up.railway.app/frontend/index.php`
This should show your homepage! Then fix the root directory setting for clean URLs.
---
## 📞 NEXT STEPS
1. **Immediate:** Open `https://clothes-shop-frontend-production.up.railway.app/frontend/index.php`
   - Should show your homepage
   - Confirms frontend is working, just at wrong path
2. **Fix (2 min):** Railway → Frontend → Settings → Root Directory → `frontend`
3. **Wait (2 min):** Railway redeploys automatically
4. **Test:** `https://clothes-shop-frontend-production.up.railway.app/` should now work
5. **Update:** Backend callback URL to match new path
---
**Your frontend IS working - just needs the root directory configured!**
