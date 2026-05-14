# Deta Space Deployment Guide

This project is configured for deployment on **Deta Space** — a free serverless platform for Node.js and Python apps.

## What is Deta Space?

- **Free** — no credit card required
- **Serverless** — automatic scaling
- **Built-in database** — space data available for free
- **Easy deployment** — single command deployment

## Prerequisites

1. **Deta CLI** installed locally
2. **Deta Space account** (free, no payment method required)

## Installation

### 1. Install Deta CLI

#### Windows (PowerShell)
```powershell
iwr https://get.deta.dev/cli.ps1 -useb | iex
```

#### macOS/Linux
```bash
curl https://get.deta.dev/cli.sh | sh
```

### 2. Sign Up for Deta Space

```bash
deta space login
```

This will open your browser to create a free account on Deta Space.

## Deployment

### Option A: Deploy Both Services (Recommended)

From the project root:

```bash
deta space deploy
```

This deploys:
- `backend` → https://backend-<your-space>.deta.dev
- `ml-service` → https://ml-service-<your-space>.deta.dev

### Option B: Deploy Individual Services

**Backend only:**
```bash
cd backend
deta space deploy
```

**ML Service only:**
```bash
cd ml-service
deta space deploy
```

## Post-Deployment

### 1. Get Backend URL
After deployment, you'll see output like:
```
Deployed as https://backend-xyz.deta.dev
```

### 2. Configure Backend with ML Service URL

Set the `ML_SERVICE_URL` environment variable in Deta Space dashboard:

```
ML_SERVICE_URL=https://ml-service-xyz.deta.dev
```

### 3. Health Checks

**Backend:**
```bash
curl https://backend-xyz.deta.dev/api/health
```

**ML Service:**
```bash
curl https://ml-service-xyz.deta.dev/health
```

## Environment Variables

Both services read environment variables from `.env` files (local) or from the Deta Space dashboard.

### Backend (`.env` or Deta dashboard)
```
PORT=5000
MONGODB_URI=<your-mongodb-connection>
ML_SERVICE_URL=https://ml-service-xyz.deta.dev
MOBILE_ORIGINS=https://backend-xyz.deta.dev,http://localhost:5000
```

### ML Service (`.env` or Deta dashboard)
No required environment variables (uses pre-trained models).

## Logs & Debugging

View deployment logs:
```bash
deta space logs
```

## Updating Deployment

After pushing code to GitHub:
1. Pull latest changes locally
2. Run `deta space deploy` again

Or set up auto-deploy from GitHub (requires Deta GitHub integration).

## Limitations

- ML service may have limited compute on free tier
- No custom domains (but Deta provides free subdomains)
- Database storage limited to Deta Space quota

## Support

- Deta Docs: https://deta.space
- Deta Community: https://discord.gg/deta
