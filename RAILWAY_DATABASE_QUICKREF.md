# 🎯 RAILWAY DATABASE - QUICK REFERENCE CARD
## 🚀 ADDING POSTGRESQL IN 6 STEPS
```
┌─────────────────────────────────────────────────────────────┐
│  STEP 1: Open Railway Dashboard                            │
│  → https://railway.app → Sign in → Open your project       │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│  STEP 2: Add PostgreSQL                                    │
│  → Click [+ New] button                                    │
│  → Select [Database] → [Add PostgreSQL]                    │
│  → Wait ~30 seconds                                         │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│  STEP 3: Verify It's Running                               │
│  → Click PostgreSQL service                                │
│  → Status should show "Running" (green)                    │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│  STEP 4: Check Variables                                   │
│  → Click Backend service → [Variables] tab                 │
│  → Should see: PGHOST, PGPORT, PGDATABASE, PGUSER, PGPASSWORD │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│  STEP 5: Link Services (if variables missing)              │
│  → Backend → [Settings] → "Referenced Services"            │
│  → [Add Reference] → Select PostgreSQL → [Save]            │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│  STEP 6: Redeploy                                           │
│  → [Deployments] tab → [...] → [Redeploy]                  │
│  → Watch logs for "Started ClothesShopApplication"         │
└─────────────────────────────────────────────────────────────┘
```
---
## ✅ VARIABLES CHECKLIST
### Auto-Provided by Railway (after linking):
- [ ] `PGHOST` - Database host URL
- [ ] `PGPORT` - Database port (5432)
- [ ] `PGDATABASE` - Database name (railway)
- [ ] `PGUSER` - Database username (postgres)
- [ ] `PGPASSWORD` - Auto-generated password
- [ ] `PORT` - Your app port (auto-assigned)
### Add Manually:
- [ ] `PAYSTACK_API_KEY` - Your Paystack secret key
- [ ] `PAYSTACK_CALLBACK_URL` - Your callback URL
---
## 🎨 VISUAL: Railway Project Layout
After adding PostgreSQL, you should see:
```
┌──────────────────────────────────────────────┐
│         Your Railway Project                 │
├──────────────────────────────────────────────┤
│                                              │
│  ┌─────────────┐     ┌─────────────┐       │
│  │   Backend   │────→│ PostgreSQL  │       │
│  │  (Running)  │     │  (Running)  │       │
│  └─────────────┘     └─────────────┘       │
│                                              │
└──────────────────────────────────────────────┘
```
---
## 🐛 TROUBLESHOOTING
### Problem: Variables Not Showing Up
**What to check:**
1. Is PostgreSQL service running? (green status)
2. Are services linked? (Backend → Settings → Referenced Services)
3. Did you refresh the Variables tab?
**Fix:** Manually link services (Step 5 above)
---
### Problem: "Connection Refused" Error
**What to check:**
1. PostgreSQL status (should be green/running)
2. PGHOST value (should be railway.app domain)
3. PGPORT value (should be 5432)
**Fix:** Restart PostgreSQL service or redeploy backend
---
### Problem: "Authentication Failed" Error
**What to check:**
1. PGUSER exists and equals "postgres"
2. PGPASSWORD exists (long random string)
3. No manually-set conflicting variables
**Fix:** Let Railway manage PG* variables automatically
---
### Problem: Tables Don't Exist
**What to check:**
1. `schema.sql` file exists in `src/main/resources/`
2. App logs show "Executing SQL script"
3. Database initialized successfully
**Fix:** Check Railway logs for schema initialization errors
---
## ✅ SUCCESS INDICATORS
You'll know it's working when you see:
### In Railway Dashboard:
- ✅ Two services showing (Backend + PostgreSQL)
- ✅ Both services show "Running" status (green)
- ✅ Backend Variables tab shows all PG* variables
- ✅ No errors in deployment logs
### In Logs:
- ✅ `Started ClothesShopApplication in X.X seconds`
- ✅ `Netty started on port XXXX`
- ✅ `r2dbc.pool - Creating connection pool`
- ✅ No connection errors
### Test URLs:
```bash
# Health check
curl https://your-app.railway.app/actuator/health
# → {"status":"UP",...}
# Products API
curl https://your-app.railway.app/api/products
# → [] or [...]
```
---
## 📚 MORE HELP
**Full Guide:** `RAILWAY_ADD_DATABASE_GUIDE.md`  
**Debug Checklist:** `RAILWAY_DEBUG_CHECKLIST.md`  
**Healthcheck Issues:** `RAILWAY_HEALTHCHECK_FIX.md`
**Railway Support:**
- Discord: discord.gg/railway
- Docs: docs.railway.app/databases/postgresql
---
## 🔄 QUICK COMMANDS
### View database data:
`PostgreSQL service → Data tab → Run SQL queries`
### Restart PostgreSQL:
`PostgreSQL service → Settings → Restart Service`
### Manual backup:
`PostgreSQL service → Settings → Create Snapshot`
### View connection string:
`PostgreSQL service → Variables → DATABASE_URL`
---
**Your configuration is already Railway-ready!**  
Just follow the 6 steps above to add the database. 🚀
