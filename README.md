# SmartLoan AI+

**Intelligent Financial Advisory & Loan Prediction System**

A professional AI-driven fintech platform featuring custom Machine Learning models (Logistic Regression, Random Forest, XGBoost), a custom NLP financial assistant, Express.js API gateway, and Next.js ShadCN UI frontend.

## Architecture

3-tier microservice architecture:

1. **Frontend**: Next.js 14 (App Router), TypeScript, Tailwind CSS, ShadCN UI, Recharts, Framer Motion
2. **Backend**: Express.js, JWT Authentication, Helmet, Rate Limiting
3. **AI/ML Service**: Python FastAPI, Scikit-Learn, XGBoost, Pandas, NumPy

## Quick Start

### 1. ML Service (Python 3.9+)
```bash
cd ml-service
python -m venv venv
venv\Scripts\activate        # Windows
# source venv/bin/activate   # Mac/Linux
pip install -r requirements.txt

# Train ML models (auto-generates synthetic data)
python training/train_models.py

# Start FastAPI
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

### 2. Backend (Node.js 18+)
```bash
cd backend
npm install
npm run dev
# Runs on port 5000
```

### 3. Frontend (Node.js 18+)
```bash
cd frontend
npm install
npm run dev
# Runs on port 3000
```

## Demo Credentials
- Email: `demo@smartloan.ai`
- Password: `demo123`

## Features

| Feature | Description |
|---------|-------------|
| **AI Dashboard** | Real-time stats, AI insights, financial charts (Area, Bar, Radar) |
| **Loan Prediction** | Ensemble ML (LR + RF + XGB) with explainability & risk factors |
| **Health Score** | Custom 0-100 algorithm across 5 financial dimensions |
| **Risk Analyzer** | 6-dimension risk assessment with severity classification |
| **AI Chatbot** | NLP intent classification, context memory, financial reasoning |
| **Simulator** | What-if projections: income/expense/loan scenarios |
| **Reports** | Downloadable financial analysis reports |
| **Admin Panel** | User management, ML model metrics, platform analytics |
| **Auth System** | JWT, bcrypt, protected routes |
| **Dark/Light Mode** | Full theme support |

## ML Models

| Model | Purpose | Features |
|-------|---------|----------|
| Logistic Regression | Baseline, scaled features | 21 features |
| Random Forest | Primary, feature importance | 200 trees |
| XGBoost | Highest accuracy, gradient boosting | 200 estimators |

Ensemble weights: LR (20%) + RF (40%) + XGB (40%)

## Project Structure

```
├── frontend/           # Next.js 14 + TypeScript
│   ├── src/app/        # App Router pages
│   ├── src/components/ # UI components
│   ├── src/contexts/   # React contexts
│   └── src/lib/        # Utilities
├── backend/            # Express.js API
│   └── src/
│       ├── routes/     # API endpoints
│       └── middleware/  # Auth, etc.
└── ml-service/         # FastAPI + ML
    ├── services/       # ML engines
    ├── training/       # Model pipeline
    ├── models/         # Saved models
    └── tests/          # Pytest suite
```
