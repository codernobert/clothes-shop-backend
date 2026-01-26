# Railway Deployment Guide - Backend

This guide will walk you through deploying your Spring Boot backend to Railway.

## 📋 Prerequisites

1. **Railway Account**: Sign up at [railway.app](https://railway.app)
2. **GitHub Repository**: Your backend code should be in a Git repository
3. **Paystack Account**: For payment processing (get your production API key)

## 🚀 Quick Deployment Steps

### Step 1: Prepare Your Backend

Your backend is already configured for Railway! The following files are ready:
- `Procfile` - Tells Railway how to run your app
- `system.properties` - Specifies Java version
- `application.properties` - Uses environment variables for production

### Step 2: Create Railway Project

1. Go to [Railway Dashboard](https://railway.app/dashboard)
2. Click **"New Project"**
3. Select **"Deploy from GitHub repo"**
4. Authorize Railway to access your GitHub account
5. Select your **backend repository**
6. Railway will automatically detect it's a Java/Maven project

### Step 3: Add PostgreSQL Database

1. In your Railway project, click **"New"** → **"Database"** → **"Add PostgreSQL"**
2. Railway will automatically create a PostgreSQL database
3. The database credentials will be available as environment variables:
   - `DATABASE_URL` (in Postgres format)
   - `PGHOST`, `PGPORT`, `PGUSER`, `PGPASSWORD`, `PGDATABASE`

### Step 4: Configure Environment Variables

In your Railway project settings, add these environment variables:

#### Required Variables:
```properties
# Spring Profile (use production settings)
SPRING_PROFILES_ACTIVE=prod

# Database Configuration (Railway auto-injects these)
DB_HOST=${PGHOST}
DB_PORT=${PGPORT}
DB_NAME=${PGDATABASE}
DB_USER=${PGUSER}
DB_PASSWORD=${PGPASSWORD}

# Paystack Configuration
PAYSTACK_API_KEY=sk_live_your_production_key_here
PAYSTACK_API_URL=https://api.paystack.co
PAYSTACK_CALLBACK_URL=https://your-frontend-url.railway.app/payment_callback.php

# Server Configuration
SERVER_PORT=${PORT}
```

#### Optional Variables (already have defaults):
```properties
# Logging Level
LOGGING_LEVEL=INFO

# Resilience4j settings are already configured in application.properties
# Override only if you need different values in production
```

### Step 5: Deploy

1. Railway will automatically deploy when you push to your main branch
2. Watch the deployment logs in Railway dashboard
3. Once deployed, Railway will provide you with a public URL like:
   `https://your-app-name.railway.app`

### Step 6: Initialize Database Schema

Your application will automatically run the `schema.sql` file on startup to create the necessary tables.

To verify:
1. Go to Railway dashboard → Your PostgreSQL database
2. Click "Data" tab to view tables
3. You should see: `products`, `cart_items`, `orders`, `order_items`, `payments`

### Step 7: Test Your Deployment

1. Test the health endpoint:
   ```
   GET https://your-app-name.railway.app/api/health
   ```

2. Test the products endpoint:
   ```
   GET https://your-app-name.railway.app/api/products
   ```

3. Import the Postman collection and update the base URL

## 🔧 Configuration Files Explained

### Procfile
Tells Railway how to start your application:
```
web: java -jar target/clothes-shop-1.0.0.jar --server.port=$PORT
```

### system.properties
Specifies the Java runtime version:
```
java.runtime.version=17
```

### application.properties
Your existing file already uses environment variables, which is perfect for Railway!

## 🌐 Connecting Frontend to Backend

Once your backend is deployed:

1. Note your Railway backend URL: `https://your-backend.railway.app`
2. Update your frontend's `config.php` with this URL:
   ```php
   define('API_BASE_URL', 'https://your-backend.railway.app/api');
   ```

3. Update CORS in WebConfig (if needed) to allow your frontend domain:
   ```java
   .allowedOrigins("https://your-frontend.railway.app")
   ```

## 📊 Monitoring & Logs

### View Application Logs
1. Go to Railway dashboard
2. Click on your backend service
3. Click "Deployments" → Latest deployment → "View Logs"

### Health Checks
Your application exposes these endpoints:
- `/actuator/health` - Application health status
- `/actuator/metrics` - Application metrics
- `/actuator/circuitbreakers` - Circuit breaker status

## 🔄 Continuous Deployment

Railway automatically redeploys when you push to your main branch:
1. Make changes locally
2. Commit and push to GitHub
3. Railway detects the change and redeploys automatically

## 🛡️ Security Best Practices

1. **Never commit secrets**: Keep API keys in Railway environment variables
2. **Use production API keys**: Replace test keys with live keys
3. **Enable HTTPS**: Railway provides HTTPS by default
4. **Update CORS**: Restrict allowed origins to your frontend domain only
5. **Database backups**: Enable Railway's automatic backup feature

## 🐛 Troubleshooting

### Application won't start
- Check Railway logs for error messages
- Verify all environment variables are set correctly
- Ensure Java version matches (17)

### Database connection issues
- Verify PostgreSQL service is running in Railway
- Check that DB_HOST, DB_PORT, etc. are correctly set
- Review connection string format in application.properties

### Payment issues
- Verify PAYSTACK_API_KEY is the production key (starts with `sk_live_`)
- Check PAYSTACK_CALLBACK_URL points to your frontend
- Review Paystack dashboard for webhook logs

### CORS errors from frontend
- Update WebConfig.java to allow your frontend domain
- Redeploy after making CORS changes

## 💰 Cost Estimate

Railway Pricing:
- **Hobby Plan**: $5/month (includes $5 credit)
- **PostgreSQL**: ~$1-2/month for small database
- **Backend service**: Based on resource usage

Estimated total: ~$5-10/month for small to medium traffic

## 📚 Additional Resources

- [Railway Documentation](https://docs.railway.app/)
- [Railway Java Guide](https://docs.railway.app/guides/java)
- [Spring Boot on Railway](https://docs.railway.app/guides/spring-boot)
- [Paystack Documentation](https://paystack.com/docs/api/)

## ✅ Deployment Checklist

- [ ] Create Railway account
- [ ] Push backend code to GitHub
- [ ] Create new Railway project from GitHub repo
- [ ] Add PostgreSQL database to project
- [ ] Configure all environment variables
- [ ] Wait for initial deployment to complete
- [ ] Verify database schema was created
- [ ] Test API endpoints (health, products)
- [ ] Add products through admin API
- [ ] Test complete customer journey with Postman
- [ ] Update frontend config with backend URL
- [ ] Deploy frontend to separate Railway project
- [ ] Test end-to-end flow with real frontend
- [ ] Switch to production Paystack keys
- [ ] Test payment flow with small amount
- [ ] Update CORS to allow only frontend domain
- [ ] Set up monitoring and alerts
- [ ] Enable database backups

---

**Need Help?**
- Railway Discord: [discord.gg/railway](https://discord.gg/railway)
- Railway Support: support@railway.app
