# 🗄️ Adding PostgreSQL Database to Railway - Step by Step Guide
## Method 1: Add PostgreSQL to Existing Project (Recommended)
### Step 1: Open Your Railway Project
1. Go to **[railway.app](https://railway.app)**
2. Sign in to your account
3. Click on your **backend project** (the one that's failing)
### Step 2: Add PostgreSQL Service
1. In your project dashboard, click the **"+ New"** button (top right)
2. Select **"Database"**
3. Choose **"Add PostgreSQL"**
4. Railway will provision a new PostgreSQL database (takes ~30 seconds)
### Step 3: Verify Database is Running
1. You should now see **two services** in your project:
   - Your backend service (Java app)
   - PostgreSQL database
2. Click on the **PostgreSQL** service
3. Check that the status shows **"Running"** (green indicator)
### Step 4: Link Database to Backend (IMPORTANT!)
Railway should auto-link them, but verify:
1. Click on your **backend service** (not PostgreSQL)
2. Go to **"Variables"** tab
3. Look for these variables (should appear automatically):
   - `PGHOST`
   - `PGPORT` (usually 5432)
   - `PGDATABASE` (usually "railway")
   - `PGUSER` (usually "postgres")
   - `PGPASSWORD` (auto-generated)
   - `DATABASE_URL` (full connection string)
**If these variables are MISSING:**
Your services aren't linked. Follow Step 5.
### Step 5: Manually Link Services (If Needed)
If PGHOST and other variables don't appear:
1. Click your **backend service**
2. Go to **"Settings"** tab
3. Scroll to **"Service Variables"** or **"Referenced Services"**
4. Click **"Add Reference"** or **"Link Service"**
5. Select your **PostgreSQL** service
6. Click **"Add"** or **"Save"**
7. The PG* variables should now appear in the Variables tab
### Step 6: Verify Environment Variables
Go back to **Variables** tab and confirm you see:
```
PGHOST=containers-us-west-xxx.railway.app (or similar)
PGPORT=5432
PGDATABASE=railway
PGUSER=postgres
PGPASSWORD=xxx...xxx (long random string)
DATABASE_URL=postgresql://postgres:xxx@containers...
```
### Step 7: Add Your Custom Variables
While in the Variables tab, add these if not already present:
```
PAYSTACK_API_KEY=sk_test_3f4ed3ee01e4b3ea1dcf91b00caecdbadd0c8ade
PAYSTACK_CALLBACK_URL=http://localhost:8000/payment_callback.php
```
*(Update with production values when ready)*
### Step 8: Redeploy Your Backend
1. Go to **"Deployments"** tab
2. Click the **three dots (...)** on the latest deployment
3. Click **"Redeploy"**
4. Watch the logs for successful startup
---
## Method 2: Create New Project with Database
If you want to start fresh:
### Option A: Using Railway Dashboard
1. **Create New Project**
   - Click **"New Project"**
   - Select **"Empty Project"**
2. **Add PostgreSQL First**
   - Click **"+ New"**
   - Select **"Database"** → **"Add PostgreSQL"**
3. **Add Your Backend**
   - Click **"+ New"**
   - Select **"GitHub Repo"**
   - Choose your backend repository
   - Railway auto-detects Java/Maven
4. **Variables are Auto-Linked**
   - PG* variables automatically available to backend
### Option B: Using Railway CLI
```bash
# Install Railway CLI
npm install -g @railway/cli
# Login
railway login
# Initialize new project
railway init
# Add PostgreSQL
railway add postgresql
# Link your repo
railway link
# Deploy
railway up
```
---
## 🔍 Troubleshooting Database Connection
### Issue 1: Variables Not Showing Up
**Symptom:** PGHOST, PGPORT, etc. don't appear in Variables tab
**Solution:**
1. Verify PostgreSQL service is **running** (green status)
2. Manually link services (see Step 5 above)
3. Wait 10-20 seconds for Railway to inject variables
4. Refresh the Variables tab
### Issue 2: "Connection Refused" Error
**Symptom:** App logs show database connection errors
**Possible Causes:**
- PostgreSQL service not running → Restart it
- Wrong host/port → Verify PGHOST and PGPORT values
- Firewall/network issue → Check Railway status page
**Solution:**
```bash
# Test connection from Railway service logs
# You should see:
Successfully connected to database
Database pool initialized
```
### Issue 3: "Authentication Failed" Error
**Symptom:** Password authentication error in logs
**Solution:**
1. Verify `PGUSER` and `PGPASSWORD` exist in Variables
2. Don't manually set these - let Railway provide them
3. If you changed them, reset the PostgreSQL service
### Issue 4: Database Schema Not Created
**Symptom:** Tables don't exist errors
**Solution:**
Your `schema.sql` file should run automatically. Verify it's in:
```
src/main/resources/schema.sql
```
If tables aren't created, check logs for schema initialization errors.
---
## 📊 Verify Database Connection
### From Railway Dashboard:
1. Click **PostgreSQL** service
2. Go to **"Data"** tab
3. You should see SQL query interface
4. Run: `SELECT version();`
5. Should return PostgreSQL version
### From Your App Logs:
Look for these successful connection messages:
```
r2dbc.pool - Creating connection pool
r2dbc.postgresql - Connected to database
Netty started on port XXXX
```
### Test Health Endpoint:
Once deployed:
```bash
curl https://your-app.railway.app/actuator/health
```
Expected response:
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    }
  }
}
```
---
## 🎯 Visual Guide: Railway Dashboard Layout
```
┌─────────────────────────────────────────────────────┐
│  Your Project                                       │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌──────────────────┐     ┌──────────────────┐    │
│  │  Backend Service │     │   PostgreSQL     │    │
│  │  (Your Java App) │────→│   (Database)     │    │
│  │                  │     │                  │    │
│  │  Status: Running │     │  Status: Running │    │
│  │  Port: 8080      │     │  Port: 5432      │    │
│  └──────────────────┘     └──────────────────┘    │
│          │                                          │
│          └─ Variables (click to view):             │
│             • PGHOST                                │
│             • PGPORT                                │
│             • PGDATABASE                            │
│             • PGUSER                                │
│             • PGPASSWORD                            │
│             • PORT                                  │
│             • PAYSTACK_API_KEY (add manually)      │
│                                                     │
└─────────────────────────────────────────────────────┘
```
---
## ⚙️ Database Configuration in Your App
Your `application.properties` is already configured to use Railway variables:
```properties
# Uses Railway's PGHOST, PGPORT, PGDATABASE automatically
spring.r2dbc.url=r2dbc:postgresql://${PGHOST:localhost}:${PGPORT:5432}/${PGDATABASE:ecommerce_db}
spring.r2dbc.username=${PGUSER:postgres}
spring.r2dbc.password=${PGPASSWORD:1234}
```
**How it works:**
- `PGHOST` → Railway provides the database host
- `PGPORT` → Railway provides the port (5432)
- `PGDATABASE` → Railway provides database name
- `PGUSER` → Railway provides username
- `PGPASSWORD` → Railway provides password
If Railway variables are missing, it falls back to local defaults (localhost, postgres, 1234).
---
## 🚀 Quick Start Checklist
- [ ] Open Railway project
- [ ] Click **"+ New"** → **"Database"** → **"Add PostgreSQL"**
- [ ] Wait for PostgreSQL to provision (~30 sec)
- [ ] Verify PostgreSQL status is **"Running"**
- [ ] Click backend service → **"Variables"** tab
- [ ] Confirm PGHOST, PGPORT, PGDATABASE, PGUSER, PGPASSWORD exist
- [ ] If missing, manually link PostgreSQL service to backend
- [ ] Add custom variables: PAYSTACK_API_KEY, PAYSTACK_CALLBACK_URL
- [ ] Go to **"Deployments"** tab → Redeploy
- [ ] Watch logs for "Started ClothesShopApplication"
- [ ] Test: `curl https://your-app.railway.app/actuator/health`
---
## 💡 Pro Tips
1. **PostgreSQL in Same Project**: Keep database in same project as backend for automatic linking
2. **Don't Share Database**: Each environment (dev/staging/prod) should have its own database
3. **Backup Enabled**: Railway automatically backs up PostgreSQL databases
4. **Monitor Usage**: Check Railway dashboard for database size and connection usage
5. **Connection Pooling**: Your app already has pooling configured (5-10 connections)
---
## 📞 Need Help?
**Railway Discord**: discord.gg/railway
**Railway Docs**: docs.railway.app/databases/postgresql
**Common Questions:**
- How to view database data? → PostgreSQL service → Data tab
- How to run SQL queries? → Data tab has SQL query interface
- How to backup manually? → PostgreSQL service → Settings → Create Snapshot
- How to connect locally? → Use DATABASE_URL from Variables tab
---
**Next Steps After Adding Database:**
1. ✅ Verify all PG* variables exist
2. ✅ Redeploy your backend service
3. ✅ Check logs for successful database connection
4. ✅ Test health endpoint
5. ✅ Test API endpoints (`/api/products`)
**Your database is ready when you see:**
- PostgreSQL status: **Running** ✅
- Backend connects successfully ✅
- Health endpoint returns `{"status":"UP"}` ✅
- No connection errors in logs ✅
