# 🚨 Railway Deployment - Healthcheck Failure Fix
## Problem: Healthcheck Failure After 5 Minutes
Your deployment is failing with:
```
Network › Healthcheck (04:52) Healthcheck failure
```
This means Railway couldn't reach your health endpoint at `/actuator/health` within the timeout period.
---
## ✅ Fixes Applied
### 1. **Simplified railway.json**
Removed aggressive healthcheck settings. Railway will now use defaults.
### 2. **Improved Database Connection**
Updated `application.properties` with:
- Direct PGHOST/PGPORT/PGDATABASE env vars (Railway standard)
- Connection pooling settings
- Validation query for connection health
### 3. **Enhanced Actuator Health**
Added liveness and readiness probes for better Railway compatibility.
---
## 🔍 Debugging Steps
### Step 1: Check Railway Logs
In Railway dashboard → Your service → Logs tab
Look for these issues:
#### ❌ Database Connection Error
```
Error: Connection refused
Error: Unknown database
Error: Authentication failed
```
**Fix**: Verify PostgreSQL service is running and linked to your backend service.
#### ❌ Port Binding Error
```
Error: Port 8080 is already in use
Failed to bind to 0.0.0.0:8080
```
**Fix**: Ensure `PORT` environment variable is being used (it should be auto-set by Railway).
#### ❌ Missing JAR File
```
Error: Could not find or load main class
Unable to access jarfile target/clothes-shop-1.0.0.jar
```
**Fix**: Build may have failed. Check build logs.
#### ❌ Out of Memory
```
java.lang.OutOfMemoryError: Java heap space
```
**Fix**: Railway may need more memory. Upgrade plan or optimize app.
---
## 🛠️ Railway Configuration Checklist
### In Railway Dashboard:
#### ✅ Database Service
- [ ] PostgreSQL service exists in your project
- [ ] PostgreSQL is in "Running" state (green)
- [ ] Click PostgreSQL → Data tab → Verify it's accessible
#### ✅ Environment Variables (Auto-provided by Railway)
Check Variables tab for:
- [ ] `PGHOST` (e.g., containers-us-west-123.railway.app)
- [ ] `PGPORT` (usually 5432)
- [ ] `PGDATABASE` (e.g., railway)
- [ ] `PGUSER` (e.g., postgres)
- [ ] `PGPASSWORD` (auto-generated)
- [ ] `PORT` (auto-assigned by Railway)
#### ✅ Your Custom Variables
- [ ] `PAYSTACK_API_KEY` (your Paystack secret key)
- [ ] `PAYSTACK_CALLBACK_URL` (your frontend URL or temp URL)
---
## 🚀 Redeployment Steps
### Option 1: Quick Fix (Recommended)
1. **Commit the fixes**:
   ```bash
   git add .
   git commit -m "Fix Railway healthcheck configuration"
   git push origin main
   ```
2. **Railway auto-deploys** on push
3. **Watch logs** in Railway dashboard
### Option 2: Manual Redeploy
1. Go to Railway dashboard
2. Click your backend service
3. Click "Deployments" tab
4. Click three dots → "Redeploy"
---
## 🧪 Test Healthcheck Locally First
Before pushing, test that your app starts correctly:
```bash
# Build the app
mvn clean package -DskipTests
# Set environment variables (PowerShell)
$env:PGHOST="localhost"
$env:PGPORT="5432"
$env:PGDATABASE="ecommerce_db"
$env:PGUSER="postgres"
$env:PGPASSWORD="1234"
$env:PORT="8080"
# Run the app
java -jar target/clothes-shop-1.0.0.jar
# In another terminal, test health endpoint
curl http://localhost:8080/actuator/health
```
Expected output:
```json
{
  "status": "UP",
  "components": {
    "circuitBreakers": {"status": "UP"},
    "db": {"status": "UP"},
    "livenessState": {"status": "UP"},
    "readinessState": {"status": "UP"}
  }
}
```
---
## 🔧 Alternative: Disable Healthcheck Temporarily
If you need to get deployed quickly while debugging:
### In Railway Dashboard:
1. Go to your backend service
2. Settings → Health Check
3. **Disable health check** temporarily
4. Deploy and check logs to see the actual error
**⚠️ Warning**: Re-enable health checks once your app is stable!
---
## 📋 Common Solutions
### Solution 1: Database Not Linked
**Symptom**: App can't connect to database
**Fix**:
1. Railway dashboard → Your backend service
2. Settings → Add service reference
3. Link PostgreSQL service
4. Redeploy
### Solution 2: Wrong Database URL Format
**Symptom**: Connection string parse errors
**Fix**: Use simplified env vars (already done in the fix):
```properties
spring.r2dbc.url=r2dbc:postgresql://${PGHOST}:${PGPORT}/${PGDATABASE}
```
### Solution 3: App Takes Too Long to Start
**Symptom**: Healthcheck times out before app is ready
**Fix**: 
- Railway default timeout is generous (should be enough)
- If still failing, optimize startup time:
  - Use `-noverify` JVM flag
  - Reduce connection pool initial-size
  - Lazy-init beans
### Solution 4: Schema Not Created
**Symptom**: Tables don't exist errors
**Fix**: Ensure `schema.sql` runs on startup. Add to `application.properties`:
```properties
spring.sql.init.mode=always
```
---
## 🎯 Expected Deployment Timeline
1. **Build** (Maven): 2-4 minutes
2. **Docker image creation**: 30 seconds
3. **Deployment**: 30 seconds
4. **App startup**: 30-60 seconds
5. **Healthcheck**: 10-30 seconds
**Total**: ~5-7 minutes normally
If taking longer, check logs for the holdup.
---
## 📞 Still Failing?
### Share These from Railway Dashboard:
1. **Build logs** (Deployments → Latest → Build Logs)
2. **Deploy logs** (Deployments → Latest → Deploy Logs)
3. **Environment variables** (Variables tab - hide sensitive values)
### Quick Diagnostic Commands:
```bash
# Check if healthcheck endpoint exists locally
curl http://localhost:8080/actuator/health
# Check if app binds to PORT correctly
netstat -ano | findstr :8080
# Check JAR file exists
ls target/clothes-shop-1.0.0.jar
```
---
## ✅ Success Indicators
You'll know it's working when:
- ✅ Railway shows "Running" status (green)
- ✅ Logs show: "Started ClothesShopApplication in X seconds"
- ✅ Logs show: "Netty started on port 8080" (or assigned PORT)
- ✅ Health endpoint returns `{"status":"UP"}`
- ✅ You can access: `https://your-app.railway.app/actuator/health`
---
**Next**: After successful deployment, test `/api/products` endpoint to verify database connectivity.
**Updated Files**:
- ✅ `railway.json` - Simplified healthcheck
- ✅ `application.properties` - Better DB connection & health config
- ✅ This troubleshooting guide
**Push these changes and watch the deployment logs!**
