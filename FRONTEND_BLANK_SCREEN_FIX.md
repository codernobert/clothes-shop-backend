# 🚨 FRONTEND BLANK WHITE SCREEN - TROUBLESHOOTING GUIDE
## Issue: Frontend Shows Blank White Page
**Your Status:**
- ✅ Backend: https://clothes-shop-backend-production.up.railway.app (HEALTHY - UP)
- ❌ Frontend: https://clothes-shop-frontend-production.up.railway.app (BLANK WHITE)
---
## 🔍 IMMEDIATE DIAGNOSIS STEPS
### Step 1: Check Browser Console (CRITICAL)
1. Open: https://clothes-shop-frontend-production.up.railway.app
2. Press **F12** (Developer Tools)
3. Click **Console** tab
4. Look for errors
**Common Errors You Might See:**
#### Error A: PHP Parse/Syntax Error
```
Parse error: syntax error, unexpected...
Fatal error: Uncaught Error...
```
**→ PHP code has syntax errors**
#### Error B: Database Connection Error
```
SQLSTATE[HY000] [2002] Connection refused
Failed to connect to MySQL
```
**→ Frontend trying to connect to database (shouldn't need one if using backend API)**
#### Error C: Missing config.php
```
Warning: include(config.php): failed to open stream
```
**→ Config file missing or path wrong**
#### Error D: 500 Internal Server Error
**→ Check Railway frontend logs**
---
### Step 2: Check Railway Frontend Logs (CRITICAL)
1. Go to Railway Dashboard
2. Click **Frontend Service**
3. Click **Deployments** → Latest deployment
4. Click **View Logs**
**Look for these errors:**
- PHP parse errors
- Missing files
- Permission errors
- Database connection attempts
- Module not found errors
**Copy the last 20 lines of logs and check for errors**
---
### Step 3: Check Network Tab
1. Open frontend URL
2. Press **F12** → **Network** tab
3. Refresh page
4. Check what's loading:
   - Is index.php being served?
   - Are there 404 errors?
   - Are there 500 errors?
---
## 🛠️ COMMON CAUSES & FIXES
### Cause 1: Frontend Not Using Backend API (Most Common)
**Symptom:** Frontend tries to connect to MySQL/database directly
**Why:** Your frontend might have hardcoded database connections instead of using backend API
**Fix:** Frontend should ONLY call backend API, not access database directly
**Check in your frontend code:**
```php
// ❌ WRONG - Direct database connection (shouldn't exist in frontend)
\$conn = new mysqli("localhost", "user", "pass", "db");
// ✅ CORRECT - Use backend API
\$apiUrl = getenv('BACKEND_API_URL') ?: 'http://localhost:8080/api';
\$response = file_get_contents("\$apiUrl/products");
```
**Action:** Verify frontend config.php uses backend API, not direct database
---
### Cause 2: Missing Environment Variables
**Symptom:** Blank page with no obvious errors
**Fix:** Add required environment variables in Railway
**Go to Railway Frontend Service → Variables:**
```
BACKEND_API_URL=https://clothes-shop-backend-production.up.railway.app/api
```
**Then redeploy frontend**
---
### Cause 3: PHP Version Mismatch
**Symptom:** PHP errors about unsupported syntax
**Fix:** Check PHP version in Railway
**In frontend repo, create/update** `nixpacks.toml`:
```toml
[phases.setup]
nixPkgs = ["php82"]
[phases.build]
cmds = ["composer install --no-dev --optimize-autoloader"]
[start]
cmd = "php -S 0.0.0.0:\$PORT -t ."
```
---
### Cause 4: Wrong Start Command
**Symptom:** Service running but not serving pages
**Fix:** Check Railway frontend start command
**Railway → Frontend → Settings → Start Command:**
Should be something like:
```
php -S 0.0.0.0:\$PORT
```
or
```
vendor/bin/heroku-php-apache2
```
---
### Cause 5: Index File Not Found
**Symptom:** 404 or blank page
**Fix:** Verify index.php exists in root
**Check your frontend structure:**
```
frontend-repo/
├── index.php          ← Must be in root
├── config.php
├── products.php
├── cart.php
└── ...
```
---
### Cause 6: CORS Preventing Page Load
**Symptom:** Blank page, CORS errors in console
**Already fixed in backend, but verify:**
1. Backend deployed with CORS changes?
2. Backend WebConfig.java has frontend domain?
---
## 🚀 QUICK FIX CHECKLIST
### Immediate Actions:
1. **Check Railway Frontend Logs**
   ```
   Railway → Frontend Service → Deployments → View Logs
   Look for errors (last 50 lines)
   ```
2. **Check Browser Console**
   ```
   F12 → Console tab
   Look for JavaScript/PHP errors
   ```
3. **Verify Environment Variables**
   ```
   Railway → Frontend → Variables
   BACKEND_API_URL should be set
   ```
4. **Test Backend API Directly**
   ```bash
   curl https://clothes-shop-backend-production.up.railway.app/api/products
   ```
   Should return JSON (empty array or products)
5. **Check Frontend Deployment Status**
   ```
   Railway → Frontend → Should show "Running" (green)
   ```
---
## 🔧 SPECIFIC FIXES FOR YOUR SETUP
### If Frontend Uses MySQL Directly (Common Issue):
Your frontend was likely built to work with MySQL. If deploying to Railway separately, it shouldn't need a database - it should only call the backend API.
**Options:**
**Option A: Frontend Calls Backend Only (Recommended)**
- Remove all database connections from frontend
- Use `file_get_contents()` or `curl` to call backend API
- Update all pages to use API endpoints
**Option B: Add MySQL to Frontend (Not Recommended)**
- Add MySQL service to frontend Railway project
- Set database environment variables
- But this defeats the purpose of having a backend API
**Recommended: Use Option A - Frontend as pure UI calling backend API**
---
### If It's a Path/Routing Issue:
**Check if Railway is serving from correct directory:**
Railway → Frontend → Settings → Root Directory:
- Should be empty or `/` if index.php is in root
- Should be `/frontend` if index.php is in a frontend subfolder
---
## 📊 DEBUGGING COMMANDS
### Test Frontend Endpoint:
```bash
# Test if frontend is responding at all
curl -I https://clothes-shop-frontend-production.up.railway.app
# Should return:
# HTTP/2 200 OK (if working)
# HTTP/2 500 (if PHP error)
# HTTP/2 404 (if file not found)
```
### Test Specific PHP File:
```bash
curl https://clothes-shop-frontend-production.up.railway.app/index.php
```
### Check if It's PHP or Static:
```bash
curl https://clothes-shop-frontend-production.up.railway.app
# Look at response - is it HTML? Empty? Error message?
```
---
## 🎯 MOST LIKELY CAUSES (In Order):
1. **Frontend trying to connect to database directly** (80% probability)
   - Fix: Update frontend to use backend API only
2. **Missing BACKEND_API_URL environment variable** (10% probability)
   - Fix: Add variable in Railway frontend service
3. **PHP errors in code** (5% probability)
   - Fix: Check Railway logs for PHP errors
4. **Wrong start command or PHP version** (3% probability)
   - Fix: Update start command or PHP version
5. **Index.php not in correct location** (2% probability)
   - Fix: Check file structure and root directory setting
---
## ✅ WHAT TO DO NOW
### Immediate Steps:
1. **Check Railway Frontend Logs:**
   - Railway Dashboard → Frontend Service → Deployments → View Logs
   - Look at last 50 lines
   - Copy any errors
2. **Check Browser Console:**
   - Open frontend URL
   - Press F12 → Console
   - Look for errors
   - Screenshot if needed
3. **Share Findings:**
   - What errors do you see in Railway logs?
   - What errors in browser console?
   - What does `curl -I https://clothes-shop-frontend-production.up.railway.app` return?
---
## 📞 NEXT STEPS BASED ON LOGS
**Once you check the logs, the fix will be one of these:**
1. **Database connection error** → Update frontend to use backend API
2. **Missing env vars** → Add BACKEND_API_URL
3. **PHP error** → Fix syntax error in code
4. **404 error** → Fix file paths or root directory
5. **500 error** → Check specific error in logs
---
## 🔍 QUICK DIAGNOSTIC
**Run this in PowerShell to get diagnostic info:**
```powershell
# Quick Frontend Diagnostic
\$frontend = "https://clothes-shop-frontend-production.up.railway.app"
Write-Host "Testing Frontend..." -ForegroundColor Yellow
try {
    \$response = Invoke-WebRequest \$frontend -UseBasicParsing
    Write-Host "Status Code: \$(\$response.StatusCode)" -ForegroundColor Green
    Write-Host "Content Length: \$(\$response.Content.Length) bytes" -ForegroundColor Green
    if (\$response.Content.Length -eq 0) {
        Write-Host "⚠️  WARNING: Response is empty!" -ForegroundColor Red
    }
    # Check content type
    \$contentType = \$response.Headers['Content-Type']
    Write-Host "Content-Type: \$contentType" -ForegroundColor Cyan
    # Show first 500 chars of response
    Write-Host "
First 500 characters of response:" -ForegroundColor Yellow
    Write-Host \$response.Content.Substring(0, [Math]::Min(500, \$response.Content.Length))
} catch {
    Write-Host "❌ Error accessing frontend: \$_" -ForegroundColor Red
}
Write-Host "

NEXT: Check Railway Frontend Logs" -ForegroundColor Cyan
Write-Host "Railway Dashboard → Frontend Service → Deployments → View Logs" -ForegroundColor White
```
---
**ACTION REQUIRED:** 
1. Check Railway frontend logs (most important!)
2. Check browser console (F12)
3. Run diagnostic script above
4. Share what errors you see
The blank white screen means something is failing - the logs will tell us exactly what!
