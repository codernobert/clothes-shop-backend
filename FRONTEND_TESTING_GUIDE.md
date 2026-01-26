# 🧪 FRONTEND TESTING GUIDE - After Backend Deployment
## Your Deployment URLs:
- **Backend:**  https://clothes-shop-backend-production.up.railway.app
- **Frontend:** https://clothes-shop-frontend-production.up.railway.app
---
## 🚀 QUICK START: 5-Minute Frontend Test
### Step 1: Test Backend is Running (1 min)
```bash
# Test backend health
curl https://clothes-shop-backend-production.up.railway.app/actuator/health
```
**Expected Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "livenessState": {"status": "UP"},
    "readinessState": {"status": "UP"}
  }
}
```
✅ If you see `"status":"UP"`, backend is ready!
---
### Step 2: Open Frontend & Check Browser Console (2 min)
1. **Open Frontend:**
   - URL: https://clothes-shop-frontend-production.up.railway.app
   - Wait for page to load
2. **Open Browser Console:**
   - **Windows/Linux:** Press `F12` or `Ctrl + Shift + I`
   - **Mac:** Press `Cmd + Option + I`
   - Click **"Console"** tab
3. **Check for Errors:**
   - ❌ **CORS Error?** `Access-Control-Allow-Origin`
     → Backend hasn't finished deploying or CORS not configured
   - ❌ **Network Error?** `Failed to fetch`
     → Backend not reachable or wrong API URL
   - ✅ **No Errors?** Frontend is connecting successfully!
---
### Step 3: Test API Calls in Network Tab (2 min)
1. **Open Network Tab:**
   - In Developer Tools (F12), click **"Network"** tab
   - Check "Preserve log" if available
2. **Navigate the Site:**
   - Click around (products, cart, etc.)
   - Watch Network tab for API calls
3. **Look for API Requests:**
   - Should see requests to `clothes-shop-backend-production.up.railway.app`
   - Status should be `200 OK` (green)
   - ❌ Status `0` or `CORS error` = CORS problem
   - ❌ Status `500` = Backend error
   - ❌ Status `404` = Wrong endpoint
---
## 📋 COMPLETE FRONTEND TESTING CHECKLIST
### ✅ Phase 1: Basic Connectivity Tests
#### Test 1: Frontend Homepage Loads
- [ ] Open: https://clothes-shop-frontend-production.up.railway.app
- [ ] Page loads without errors
- [ ] No console errors (F12 → Console)
- [ ] CSS/styling loads correctly
- [ ] Navigation menu visible
**If fails:** Check Railway frontend service status, review deployment logs
---
#### Test 2: Backend API Reachable
- [ ] Open browser console (F12)
- [ ] No CORS errors
- [ ] Network tab shows successful API calls
- [ ] Response status codes are 200
**If fails:** 
- Check backend is running (green status in Railway)
- Verify `BACKEND_API_URL` set in frontend variables
- Verify backend CORS allows frontend domain
---
### ✅ Phase 2: Product Catalog Tests
#### Test 3: Products Display
- [ ] Homepage shows products (if database has products)
- [ ] Product images load
- [ ] Product names, prices display correctly
- [ ] "Add to Cart" buttons visible
**Expected API Call:**
```
GET https://clothes-shop-backend-production.up.railway.app/api/products
Status: 200 OK
Response: [...array of products...]
```
**If no products show but no errors:**
- This is NORMAL if database is empty!
- You need to add products first (see "Adding Test Products" below)
---
#### Test 4: Product Search
- [ ] Search bar works
- [ ] Typing shows results
- [ ] Results filter correctly
**Expected API Call:**
```
GET https://clothes-shop-backend-production.up.railway.app/api/products/search?keyword=shirt
```
---
#### Test 5: Product Details
- [ ] Click on a product
- [ ] Detail page loads
- [ ] Shows product info, price, description
- [ ] Quantity selector works
**Expected API Call:**
```
GET https://clothes-shop-backend-production.up.railway.app/api/products/{id}
```
---
### ✅ Phase 3: Shopping Cart Tests
#### Test 6: Add to Cart
- [ ] Click "Add to Cart" on a product
- [ ] Cart count increases (in header/icon)
- [ ] Success message appears
- [ ] No console errors
**Expected API Call:**
```
POST https://clothes-shop-backend-production.up.railway.app/api/cart/add
Status: 200 OK
```
**Check in Network Tab:**
1. Click "Add to Cart"
2. Network tab → Find POST request to `/api/cart/add`
3. Click on request → "Preview" or "Response" tab
4. Should see success response
---
#### Test 7: View Cart
- [ ] Click cart icon or "View Cart"
- [ ] Cart page loads
- [ ] Added items display
- [ ] Quantities shown correctly
- [ ] Total amount calculated
**Expected API Call:**
```
GET https://clothes-shop-backend-production.up.railway.app/api/cart/{sessionId}
Status: 200 OK
```
---
#### Test 8: Update Cart Quantity
- [ ] Change quantity in cart
- [ ] Click update button
- [ ] Quantity updates
- [ ] Total recalculates
**Expected API Call:**
```
PUT https://clothes-shop-backend-production.up.railway.app/api/cart/update
```
---
#### Test 9: Remove from Cart
- [ ] Click remove button
- [ ] Item removed from cart
- [ ] Total updates
- [ ] Cart count decreases
**Expected API Call:**
```
DELETE https://clothes-shop-backend-production.up.railway.app/api/cart/remove/{itemId}
```
---
### ✅ Phase 4: Checkout & Payment Tests
#### Test 10: Proceed to Checkout
- [ ] Click "Checkout" or "Proceed to Checkout"
- [ ] Checkout page loads
- [ ] Order summary displays
- [ ] Customer info form shows
---
#### Test 11: Place Order
- [ ] Fill in customer details (name, email, phone, address)
- [ ] Click "Place Order" or similar
- [ ] Order creation succeeds
- [ ] Redirects to payment page
**Expected API Calls:**
```
POST https://clothes-shop-backend-production.up.railway.app/api/orders/checkout
Status: 200 OK
POST https://clothes-shop-backend-production.up.railway.app/api/payments/initialize
Status: 200 OK
```
---
#### Test 12: Payment Flow (TEST MODE)
- [ ] Payment page loads (Paystack popup or redirect)
- [ ] Test payment details form appears
- [ ] Fill in test card details (see below)
- [ ] Submit payment
- [ ] Payment processes
- [ ] Redirects back to your site
**Paystack Test Card:**
```
Card Number: 4084 0840 8408 4081
Expiry: 12/30 (any future date)
CVV: 408
PIN: 0000
OTP: 123456
```
---
#### Test 13: Payment Callback
- [ ] After payment, redirected to payment_callback.php
- [ ] Order status updates (Pending → Paid)
- [ ] Success message displays
- [ ] Order confirmation shown
**Expected API Call:**
```
POST https://clothes-shop-backend-production.up.railway.app/api/payments/verify
Status: 200 OK
```
---
### ✅ Phase 5: Admin Tests (If Applicable)
#### Test 14: Admin Login
- [ ] Navigate to /admin
- [ ] Admin login page loads
- [ ] Can log in (if auth implemented)
---
#### Test 15: View Orders
- [ ] Admin can view orders
- [ ] Order details display
- [ ] Customer info shown
---
## 🐛 COMMON ISSUES & FIXES
### Issue 1: CORS Errors in Console
**Symptom:**
```
Access to fetch at 'https://clothes-shop-backend-production.up.railway.app/api/products' 
from origin 'https://clothes-shop-frontend-production.up.railway.app' has been blocked by CORS policy
```
**Root Cause:** Backend CORS not configured for frontend domain
**Fix:**
1. Verify you pushed CORS changes:
   ```bash
   git log --oneline -1
   # Should show: "Configure CORS for production" or similar
   ```
2. Check backend is redeployed:
   - Railway Dashboard → Backend Service → Deployments
   - Latest deployment should be after your git push
   - Status should be "Running"
3. Wait 2-3 more minutes for full propagation
4. Hard refresh browser: `Ctrl + Shift + R`
5. If still failing, check backend `WebConfig.java`:
   ```java
   .allowedOrigins(
       "https://clothes-shop-frontend-production.up.railway.app",
       "http://localhost:8000"
   )
   ```
---
### Issue 2: Frontend Shows "Failed to Fetch"
**Symptom:** API calls fail with network errors
**Possible Causes:**
1. Backend not running
2. Wrong API URL in frontend
3. Backend crashed
**Fix:**
1. **Check Backend Status:**
   - Railway Dashboard → Backend Service
   - Status should be green "Running"
2. **Test Backend Directly:**
   ```bash
   curl https://clothes-shop-backend-production.up.railway.app/actuator/health
   ```
3. **Check Frontend Variables:**
   - Railway Dashboard → Frontend Service → Variables
   - Verify: `BACKEND_API_URL=https://clothes-shop-backend-production.up.railway.app/api`
4. **Check Frontend Code:**
   - Verify `config.php` uses `getenv('BACKEND_API_URL')`
---
### Issue 3: No Products Showing
**Symptom:** Frontend loads but shows "No products found" or empty list
**This is NORMAL if database is empty!**
**Fix: Add Test Products**
**Option A: Using curl (Quick)**
```bash
curl -X POST https://clothes-shop-backend-production.up.railway.app/api/admin/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Classic T-Shirt",
    "description": "Comfortable cotton t-shirt",
    "price": 29.99,
    "imageUrl": "https://via.placeholder.com/300/0000FF/FFFFFF?text=T-Shirt",
    "category": "Shirts",
    "brand": "TestBrand",
    "gender": "Unisex",
    "stockQuantity": 50
  }'
```
**Option B: Using Postman**
1. Import: `Ecommerce_Customer_Journey.postman_collection.json`
2. Update base URL to: `https://clothes-shop-backend-production.up.railway.app`
3. Run "Add Product" request
---
### Issue 4: Payment Not Working
**Symptom:** Payment initialization fails or callback doesn't update order
**Check:**
1. **Backend has Paystack key:**
   - Railway Backend → Variables → `PAYSTACK_API_KEY`
2. **Callback URL correct:**
   - Railway Backend → Variables
   - `PAYSTACK_CALLBACK_URL=https://clothes-shop-frontend-production.up.railway.app/payment_callback.php`
3. **Use test card details** (see above)
4. **Check Paystack Dashboard:**
   - https://dashboard.paystack.com
   - Go to Payments → Transactions
   - See if test payment appears
---
### Issue 5: Cart Not Persisting
**Symptom:** Cart resets on page refresh
**Possible Causes:**
1. Session not working
2. Using sessionStorage instead of backend
**Fix:**
- Verify cart uses backend API (`/api/cart/add`)
- Check session ID is consistent
- Use backend for cart persistence, not localStorage
---
## 🧪 TESTING WITH BROWSER DEVTOOLS
### Chrome/Edge DevTools Tips
#### Console Tab:
```javascript
// Check if API URL is set
console.log('API URL:', API_BASE_URL);
// Test API call manually
fetch('https://clothes-shop-backend-production.up.railway.app/api/products')
  .then(r => r.json())
  .then(data => console.log('Products:', data))
  .catch(err => console.error('Error:', err));
```
#### Network Tab Filters:
- Filter by: `backend-production` (shows only backend calls)
- Look for: Red rows = failed requests
- Check: Status codes (200 = OK, 500 = error, 0 = CORS)
#### Application Tab:
- Check Local Storage (if used)
- Check Session Storage (if used)
- Check Cookies
---
## 📱 MOBILE TESTING
### Test on Mobile Devices:
1. Open frontend URL on phone
2. Test all features work
3. Check responsive design
4. Test payment flow on mobile
### Mobile Debug:
- Android: Chrome remote debugging
- iOS: Safari Web Inspector
---
## ⚡ AUTOMATED TESTING SCRIPT
Save this as `test-frontend.ps1` (PowerShell):
```powershell
# Test Frontend After Backend Deployment
$backend = "https://clothes-shop-backend-production.up.railway.app"
$frontend = "https://clothes-shop-frontend-production.up.railway.app"
Write-Host "Testing Backend Health..." -ForegroundColor Yellow
$health = Invoke-RestMethod "$backend/actuator/health"
if ($health.status -eq "UP") {
    Write-Host "✓ Backend is UP" -ForegroundColor Green
} else {
    Write-Host "✗ Backend is DOWN" -ForegroundColor Red
    exit 1
}
Write-Host "
Testing Products API..." -ForegroundColor Yellow
try {
    $products = Invoke-RestMethod "$backend/api/products"
    Write-Host "✓ Products API works - Found $($products.Count) products" -ForegroundColor Green
} catch {
    Write-Host "✗ Products API failed: $_" -ForegroundColor Red
}
Write-Host "
Testing Frontend..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest $frontend -UseBasicParsing
    if ($response.StatusCode -eq 200) {
        Write-Host "✓ Frontend is accessible" -ForegroundColor Green
    }
} catch {
    Write-Host "✗ Frontend not accessible: $_" -ForegroundColor Red
}
Write-Host "
Manual Tests Required:" -ForegroundColor Cyan
Write-Host "1. Open: $frontend" -ForegroundColor White
Write-Host "2. Press F12 → Console" -ForegroundColor White
Write-Host "3. Check for CORS errors" -ForegroundColor White
Write-Host "4. Test add to cart" -ForegroundColor White
Write-Host "5. Test checkout flow" -ForegroundColor White
```
Run: `.\test-frontend.ps1`
---
## 📊 TESTING SUMMARY CHECKLIST
Quick checklist to verify everything works:
```
BACKEND TESTS:
☐ Health endpoint returns UP
☐ Products API returns data
☐ No errors in backend logs
FRONTEND TESTS:
☐ Homepage loads
☐ No console errors
☐ No CORS errors
☐ Products display (or empty if no products)
☐ Add to cart works
☐ Cart count updates
☐ View cart shows items
☐ Checkout page loads
☐ Payment initializes
☐ Payment callback works
☐ Order saves to database
INTEGRATION TESTS:
☐ Frontend can call backend APIs
☐ CORS configured correctly
☐ Environment variables set
☐ All URLs correct
☐ Test payment succeeds
```
---
## 🎯 SUCCESS CRITERIA
**Your deployment is successful when:**
✅ Frontend loads without errors  
✅ No CORS errors in console  
✅ Products can be added to cart  
✅ Cart operations work  
✅ Checkout creates order  
✅ Payment flow completes  
✅ Order appears in database  
✅ Payment callback updates status  
---
## 📞 NEXT STEPS
After successful testing:
1. **Add Real Products:**
   - Use admin API or Postman
   - Add product images
   - Set proper inventory
2. **Test Complete User Journey:**
   - Browse → Add to Cart → Checkout → Pay → Confirm
3. **Switch to Production Keys:**
   - Update `PAYSTACK_API_KEY` to `sk_live_...`
   - Test with small real payment
4. **Set Up Monitoring:**
   - Railway usage dashboard
   - Paystack transaction monitoring
   - Error tracking (optional)
---
## 📚 HELPFUL RESOURCES
**Your Project Docs:**
- `FINAL_DEPLOYMENT_CONFIG.md` - Complete deployment guide
- `RAILWAY_DEBUG_CHECKLIST.md` - Troubleshooting
- `POSTMAN_CUSTOMER_JOURNEY.md` - API testing
**External:**
- Railway Logs: Dashboard → Service → Logs
- Paystack Dashboard: https://dashboard.paystack.com
- Browser DevTools: F12
---
**Start Testing:** Open https://clothes-shop-frontend-production.up.railway.app and follow the checklist above!
