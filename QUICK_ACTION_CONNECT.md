# ⚡ QUICK ACTION CARD - Connect Frontend to Backend
## Frontend Deployed ✅
**URL:** https://clothes-shop-frontend-production.up.railway.app
---
## 🚀 3 STEPS TO COMPLETE INTEGRATION
### STEP 1: Push CORS Changes (2 minutes)
```bash
# In your backend project directory
git add .
git commit -m "Update CORS for frontend domain"
git push origin main
```
**Why:** Your backend now allows requests from your frontend domain.
---
### STEP 2: Update Railway Variables (3 minutes)
1. Open Railway Dashboard: https://railway.app/dashboard
2. Click your **Backend service**
3. Go to **Variables** tab
4. Add or update this variable:
```
PAYSTACK_CALLBACK_URL=https://clothes-shop-frontend-production.up.railway.app/payment_callback.php
```
5. Click **Add** or **Save**
**Why:** Paystack needs to know where to send payment confirmations.
---
### STEP 3: Test Integration (5 minutes)
1. **Wait for backend redeploy** (~5 min after git push)
2. **Test frontend:**
   - Open: https://clothes-shop-frontend-production.up.railway.app
   - Open browser console (F12 → Console tab)
   - Look for errors (should be none)
3. **Check CORS:**
   - If you see: `Access-Control-Allow-Origin` errors → Backend hasn't redeployed yet
   - No errors? ✅ You're good!
4. **Test API calls:**
   - Navigate through frontend
   - Check if products load
   - Try adding to cart
---
## ✅ SUCCESS INDICATORS
You'll know it's working when:
- ✅ Frontend loads without errors
- ✅ No CORS errors in browser console
- ✅ Products display (if you have products in database)
- ✅ Cart operations work
- ✅ Checkout page loads
---
## 🐛 QUICK TROUBLESHOOTING
### Problem: CORS errors still showing
**Solution:** 
- Backend may still be deploying
- Wait 2-3 more minutes
- Hard refresh: Ctrl+Shift+R (Windows) or Cmd+Shift+R (Mac)
### Problem: Frontend shows but no products
**Solution:**
- Normal if database is empty
- Add products through API or admin panel
- Check backend health: https://your-backend.railway.app/actuator/health
### Problem: "Failed to fetch" errors
**Solution:**
- Check if backend is running (Railway dashboard → green status)
- Verify BACKEND_API_URL in frontend Railway variables
- Check backend logs for errors
---
## 📋 VARIABLES YOU NEED
### Backend Service (Railway):
```
✓ PGHOST (auto)
✓ PGPORT (auto)
✓ PGDATABASE (auto)
✓ PGUSER (auto)
✓ PGPASSWORD (auto)
✓ PORT (auto)
☐ PAYSTACK_API_KEY=sk_test_...
☐ PAYSTACK_CALLBACK_URL=https://clothes-shop-frontend-production.up.railway.app/payment_callback.php
```
### Frontend Service (Railway):
```
☐ BACKEND_API_URL=https://your-backend.railway.app/api
☐ PAYSTACK_PUBLIC_KEY=pk_test_... (if used in frontend)
```
---
## 🎯 WHAT CHANGED IN YOUR CODE
### WebConfig.java
```java
// Old (allowed all origins)
.allowedOrigins("*")
// New (specific domains only)
.allowedOrigins(
    "https://clothes-shop-frontend-production.up.railway.app",
    "http://localhost:8000",
    "http://localhost:3000"
)
.allowCredentials(true)  // Added for secure cookies/sessions
```
**Why:** Security best practice - only allow your frontend domain.
---
## 📞 NEED HELP?
**Full Guide:** Open `DEPLOYMENT_COMPLETE.md` in this folder  
**Database Setup:** `RAILWAY_DATABASE_QUICKREF.md`  
**Troubleshooting:** `RAILWAY_DEBUG_CHECKLIST.md`
**Railway Support:** discord.gg/railway
---
## ⏱️ TOTAL TIME: ~10 MINUTES
1. Push changes: 2 min
2. Update variables: 3 min
3. Wait for redeploy: 5 min
4. Test: 5 min
**Then you're LIVE!** 🚀
---
**Current Status:** Ready to push changes  
**Next:** Run the git commands above
