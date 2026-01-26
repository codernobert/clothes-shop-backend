# ⚡ Railway Quick Debug Checklist
## Before Redeploying - Verify These in Railway Dashboard
### 1️⃣ PostgreSQL Service Status
- [ ] PostgreSQL service exists in your project
- [ ] Status shows **"Running"** with green indicator
- [ ] Click PostgreSQL → **Data** tab → Database is accessible
**If PostgreSQL is not running:**
- Stop and restart the PostgreSQL service
- Check Railway status page for outages
---
### 2️⃣ Environment Variables
Click your backend service → **Variables** tab
**Auto-provided by Railway (should exist):**
- [ ] `PGHOST` 
- [ ] `PGPORT` 
- [ ] `PGDATABASE` 
- [ ] `PGUSER` 
- [ ] `PGPASSWORD` 
- [ ] `PORT`
**Your custom variables (add if missing):**
- [ ] `PAYSTACK_API_KEY=sk_test_...` or `sk_live_...`
- [ ] `PAYSTACK_CALLBACK_URL=https://...`
**❌ If PGHOST/PGPORT etc. are missing:**
Your backend service may not be linked to PostgreSQL.
**Fix:**
1. Backend service → Settings
2. Look for "Service Variables" or "Referenced Services"
3. Add PostgreSQL as a referenced service
4. This will inject PG* environment variables
---
### 3️⃣ Build Logs Check
Click **Deployments** → Latest deployment → **Build Logs**
**Look for:**
- ✅ `[INFO] BUILD SUCCESS`
- ✅ `[INFO] Building jar: target/clothes-shop-1.0.0.jar`
**❌ If build fails:**
- Maven errors → Check pom.xml
- Dependency download issues → Retry build
- Out of memory → Upgrade Railway plan
---
### 4️⃣ Deploy Logs Check
Click **Deployments** → Latest deployment → **Deploy Logs**
**Look for errors:**
#### Database Connection Issues:
```
Connection refused to PGHOST:5432
FATAL: password authentication failed
Unknown database "railway"
```
**→ Fix:** Check PostgreSQL service is running and variables are correct
#### Port Binding Issues:
```
Failed to bind to 0.0.0.0:8080
Port already in use
```
**→ Fix:** Should use `PORT` env var (Railway auto-assigns)
#### JAR File Missing:
```
Error: Unable to access jarfile target/clothes-shop-1.0.0.jar
```
**→ Fix:** Build may have failed. Check build logs.
#### App Startup Success:
```
Started ClothesShopApplication in X.XXX seconds
Netty started on port XXXX
```
**→ Good!** App started successfully.
---
### 5️⃣ Service Settings
Backend service → **Settings**
**Check:**
- [ ] **Start Command:** Should be auto-detected or use Procfile
- [ ] **Health Check:** Can be temporarily disabled for debugging
- [ ] **Region:** Choose closest to your database
- [ ] **Environment:** Should be `production`
---
## 🚀 Deployment Workflow
### Step 1: Push Fixed Code
```bash
git add .
git commit -m "Fix Railway healthcheck and database configuration"
git push origin main
```
### Step 2: Monitor Deployment
1. Railway detects push (usually within 10 seconds)
2. Build starts (watch Build Logs - takes 2-4 min)
3. Deploy starts (watch Deploy Logs)
4. App starts up (watch for startup messages)
5. Health check runs (Railway pings /actuator/health)
### Step 3: Verify Success
Once deployed, test these URLs (replace with your Railway URL):
```bash
# Health check
curl https://your-app.railway.app/actuator/health
# Expected: {"status":"UP",...}
```
```bash
# Products API
curl https://your-app.railway.app/api/products
# Expected: [] or [{"id":1,"name":"...",...}]
```
---
## 🐛 Still Failing? Try This
### Option A: Disable Health Check Temporarily
1. Railway Dashboard → Backend Service → Settings
2. Find **"Health Check"** section
3. Toggle **OFF**
4. Redeploy
This lets Railway deploy without waiting for health check.
**Warning:** You won't get automatic restart if app crashes.
### Option B: Check Service Logs Live
1. Railway Dashboard → Backend Service
2. Click **"View Logs"** (top right)
3. Keep this open during deployment
4. Watch for errors in real-time
### Option C: Use Railway CLI
```bash
# Install Railway CLI
npm i -g @railway/cli
# Login
railway login
# Link to your project
railway link
# View logs in terminal
railway logs
```
---
## 📊 Expected Timeline
| Stage | Duration | What's Happening |
|-------|----------|------------------|
| Build | 2-4 min | Maven downloads dependencies, compiles, packages JAR |
| Deploy | 30 sec | Railway creates container, injects env vars |
| Startup | 30-60 sec | Spring Boot initializes, connects to DB |
| Health Check | 10 sec | Railway checks /actuator/health |
| **Total** | **~5 min** | Should complete within this time |
If taking longer than 7 minutes, something is wrong.
---
## ✅ Success Checklist
You'll know it works when you see ALL of these:
- [ ] Build logs show: `BUILD SUCCESS`
- [ ] Deploy logs show: `Started ClothesShopApplication`
- [ ] Railway status shows: **"Running"** (green)
- [ ] Health endpoint returns: `{"status":"UP"}`
- [ ] No errors in logs for 1 minute
- [ ] Can access public URL: `https://your-app.railway.app`
---
## 🆘 Need More Help?
**Share in Railway Discord or support:**
1. Screenshot of your Railway project structure (show services)
2. Last 50 lines of deploy logs
3. Environment variables list (hide sensitive values)
4. Database service status
**Railway Discord:** discord.gg/railway
**Railway Docs:** docs.railway.app
---
**Files Modified:**
- ✅ `railway.json` - Simplified configuration
- ✅ `application.properties` - Better DB connection & health
- ✅ `RAILWAY_HEALTHCHECK_FIX.md` - Full troubleshooting guide
- ✅ This quick checklist
**Now:** Commit these changes and push to trigger a new deployment!
