# SmartLoan AI+

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/Compiler168/Loan-Analysis/ci-and-deploy.yml?branch=main)
![License](https://img.shields.io/github/license/Compiler168/Loan-Analysis)
![Last Commit](https://img.shields.io/github/last-commit/Compiler168/Loan-Analysis)

## Project Introduction

**Project Name:** SmartLoan AI+

**Tagline:** Intelligent loan decision automation and personal finance advisory for modern Android users.

**Executive Summary:**
SmartLoan AI+ is a production-ready fintech platform built around an **Android Application developed in Java** that connects to a secured Express.js backend and a dedicated FastAPI ML microservice. The system delivers loan probability scoring, credit health analytics, risk monitoring, AI-driven conversational advice, and report generation.

**Project Overview:**
The platform integrates:
- Android Application developed in Java for mobile financial experiences
- Express.js backend API with Firebase Firestore persistence
- FastAPI ML service for loan prediction, scoring, simulation, and chatbot reasoning
- Cloud-ready deployment pipelines with Docker and GitHub Actions

---

## Business Documentation

### Problem Statement
Many borrowers lack transparent, real-time decision support when evaluating loan eligibility and managing finance. Traditional apps provide generic guidance without ML-enabled risk analysis or tailored scenarios.

### Existing Challenges
- Fragmented loan advice across multiple services
- Limited visibility into approval probability and financial health
- Manual risk evaluation without predictive automation
- Poor mobile-first integration for Android users

### Proposed Solution
SmartLoan AI+ centralizes loan prediction, personal finance analytics, and conversational advisory into a single mobile experience backed by enterprise-grade backend services.

### Benefits
- Faster approval insight through AI models
- Personalized recommendations based on financial health
- Reduced risk through analytics and monitoring
- Secure, scalable cloud architecture
- Clear engineer-ready project structure

### Objectives
- Provide real-time loan approval scoring
- Enable health and risk analysis on customer data
- Maintain secure user authentication and session flow
- Build a reusable ML microservice for future feature expansion
- Deliver production-level documentation and cleanup

### Target Users
- Loan applicants seeking better approval insight
- Financial advisors requiring rapid analysis
- Mobile-first consumers in emerging markets
- Product teams validating AI-enabled fintech workflows

---

## Technical Documentation

### Technology Stack

| Layer | Technology | Purpose |
|---|---|---|
| Android | Java, Android Jetpack | Mobile application UI and data handling |
| Backend | Node.js, Express.js | REST API, auth, Firestore integration |
| Database | Firebase Firestore | User, prediction, analysis, chat and report persistence |
| ML Service | Python, FastAPI | Loan prediction, health scoring, risk analysis, chatbot |
| CI/CD | GitHub Actions | Build, smoke test, Docker image publish, deploy |
| Containerization | Docker | Standardized backend and ML service packaging |

### System Architecture

```
Android App (Java)
   ↕ HTTPS
Express Backend (Node.js)
   ↕ HTTP
FastAPI ML Service (Python)
   ↕ Firestore
Firebase Firestore Database
```

### Frontend Architecture
- Android Application developed in Java
- Uses event-driven ViewModel and Activity/Fragment flows
- Network access via REST API with JWT authentication
- Local UI state managed through Android Jetpack patterns

### Backend Architecture
- `backend/src/server.js` bootstraps Express and Firestore
- Route modules separate domain logic: auth, loans, financial, chat, reports
- Firestore integration through custom model classes
- Auth middleware validates JWT tokens and protects API routes
- ML interactions proxied to the dedicated ML service via `axios`

### Database Architecture
- Firebase Firestore collections store structured documents
- Collections:
  - `users`
  - `predictions`
  - `analyses`
  - `chatSessions`
  - `reports`
  - `dashboards`

### Machine Learning Architecture
- `ml-service/main.py` exposes clean FastAPI endpoints
- Prediction, health scoring, risk analysis, simulation, NLP, and document parsing engines
- Lazy-loaded service modules for resource efficiency
- Ensemble model stack: XGBoost, Random Forest, Logistic Regression

### Security Architecture
- JWT-based authentication for all protected API routes
- Password hashing with `bcryptjs`
- Helmet middleware for HTTP security headers
- CORS restrictions configurable via environment
- Rate limiting for general and AI-specific endpoints
- Local secret management via `.env` templates and ignored credential files

---

## Feature Documentation

### User Features

#### Registration and Login
- Purpose: Create secure user accounts and authenticate.
- Inputs: `name`, `email`, `password`
- Processing Logic: Saves user data to Firestore, hashes password, issues JWT.
- Outputs: Auth token, user profile, optional Firebase custom token.
- Benefits: Secure onboarding, session persistence.
- Implementation: `backend/src/controllers/AuthController.js`, `backend/src/routes/auth.js`

#### Profile Management
- Purpose: Update personal and financial profile data.
- Inputs: profile fields like income, expenses, credit score.
- Processing Logic: Updates Firestore user records and refreshes dashboard.
- Outputs: updated user profile and auth-safe response.
- Benefits: Better prediction accuracy and personalized analytics.
- Implementation: `AuthController.updateProfile`

#### Loan Prediction
- Purpose: Estimate loan approval probability using ensemble ML.
- Inputs: client financial and loan request data.
- Processing Logic: Proxy to ML service `/predict` endpoint and persist results.
- Outputs: probability score, approval recommendation, top risk factors.
- Benefits: Faster decision support and user transparency.
- Implementation: `LoanController.predictLoan`, `ml-service/services/prediction_engine.py`

#### Financial Dashboard
- Purpose: Present summary analytics and financial health metrics.
- Inputs: user ID from authenticated session.
- Processing Logic: Reads dashboard documents from Firestore, regenerates if absent.
- Outputs: dashboard metrics, approval rates, summary totals.
- Benefits: Real-time financial overview.
- Implementation: `FinancialController.getDashboard`

#### AI Chat Assistant
- Purpose: Provide conversational financial advice and loan explanations.
- Inputs: chat message, session ID, optional user data.
- Processing Logic: Intent classification and contextual response generation.
- Outputs: conversational reply and session logging.
- Benefits: Natural advice flow with fintech intelligence.
- Implementation: `ChatController.sendMessage`, `ml-service/services/nlp_engine.py`

### AI Features

#### Loan Prediction Engine
- Purpose: Ensemble-based loan approval scoring.
- Inputs: financial profile and requested loan terms.
- Processing Logic: feature engineering, scaling, ensemble aggregation, risk reason generation.
- Outputs: approval probability, confidence, top factors, derived metrics.
- Benefits: Explainable predictions and better loan transparency.
- Implementation: `PredictionEngine.predict`

#### Health Scoring
- Purpose: Evaluate user financial health.
- Inputs: income, expenses, savings, credit score, debts.
- Processing Logic: ML-based health scoring model.
- Outputs: health score and risk classification.
- Benefits: Quick financial wellness check.
- Implementation: `HealthScorer`

#### Risk Analysis
- Purpose: Assess loan risk relative to current profile.
- Inputs: loan exposure and credit metrics.
- Processing Logic: risk model produces risk assessment features.
- Outputs: risk rating and recommended mitigation.
- Benefits: Safer lending decisions.
- Implementation: `RiskAnalyzer`

#### Simulation Engine
- Purpose: Model future financial scenarios.
- Inputs: income/expense changes, loan variables.
- Processing Logic: calculates projected cash flow and savings impact.
- Outputs: scenario summary, projected balance, recommendations.
- Benefits: Better planning before committing to loans.
- Implementation: `SimulationEngine`

### Banking Features
- Purpose: Provide autonomous loan and credit decision support.
- Inputs: loan application and profile data.
- Processing Logic: ties backend requests to ML service inference.
- Outputs: approval recommendations and financial advice.
- Benefits: Faster bank-like loan analysis on mobile.
- Implementation: `backend/src/controllers/LoanController.js`

### Analytics Features
- Purpose: Store and retrieve prediction, analysis, and report history.
- Inputs: authenticated user actions.
- Processing Logic: Firestore persistence and history retrieval.
- Outputs: historical charts, campaign metrics, session records.
- Benefits: User retention through tracked analytics.
- Implementation: Firestore model classes under `backend/src/models`

### Security Features
- Purpose: Secure access and protect sensitive data.
- Inputs: JWT tokens, request headers.
- Processing Logic: token verification and request sanitization.
- Outputs: authorized access or rejection.
- Benefits: secure production-readiness.
- Implementation: `backend/src/middleware/auth.js`, `helmet`, `rate-limit`

### Administrative Features
- Purpose: Bootstrap demo data and service health checks.
- Inputs: server startup.
- Processing Logic: create demo user if missing, verify Firestore.
- Outputs: seeded admin user and health endpoints.
- Benefits: easier onboarding for testing and demos.
- Implementation: `backend/src/server.js`

---

## Project Structure

```
Loan/
├── android/                    # Android Application developed in Java
│   ├── app/
│   │   ├── build.gradle
│   │   └── src/main/java/      # Java source and UI logic
│   │   └── src/main/res/       # layouts, drawables, resources
│   ├── gradle/
│   ├── gradle.properties
│   ├── local.properties        # local SDK path (ignored)
│   └── settings.gradle
├── backend/                    # Express.js backend API
│   ├── Dockerfile              # Backend Docker container definition
│   ├── package.json
│   ├── package-lock.json
│   ├── .env.template           # Backend env template
│   └── src/
│       ├── server.js           # Express application
│       ├── config/firebase.js  # Firestore initialization
│       ├── controllers/        # Business logic for each domain
│       ├── middleware/         # Authentication and security
│       ├── models/             # Firestore document models
│       └── routes/             # API endpoint definitions
├── ml-service/                # Python ML microservice
│   ├── Dockerfile
│   ├── main.py                 # FastAPI entrypoint
│   ├── requirements.txt
│   ├── .env.template
│   ├── services/               # ML engine modules
│   ├── models/                 # serialized model artifacts
│   ├── tests/                  # unit tests
│   └── training/              # model training scripts
├── .github/                   # CI/CD workflows
│   └── workflows/ci-and-deploy.yml
├── DEPLOYMENT.md              # Deployment guidance
├── ARCHITECTURE.md            # System architecture documentation
├── LICENSE
└── README.md
```

### Folder Details
- `android/`: full Android application source in Java, resources, Gradle config.
- `backend/`: REST API server, Firestore integration, security, and ML service gateway.
- `ml-service/`: standalone AI/ML API service with FastAPI and model engines.
- `.github/workflows/`: CI pipeline validating backend and ML service, publishing Docker images, and Fly deployment.

---

## Database Documentation

This project uses **Firebase Firestore** rather than a SQL relational database.

### Collections and Primary Keys
- `users`: user profiles and auth data. Primary key is Firestore document ID.
- `predictions`: loan prediction records per user.
- `analyses`: financial health, risk analysis, and simulation history.
- `chatSessions`: AI chat session history.
- `reports`: generated report documents.
- `dashboards`: cached dashboard summaries by user.

### Relationships
- `predictions.userId` → `users` document ID
- `analyses.userId` → `users` document ID
- `chatSessions.userId` → `users` document ID
- `reports.userId` → `users` document ID
- `dashboards` keyed by `userId`

### ER Outline

```mermaid
flowchart LR
  users((users))
  predictions((predictions))
  analyses((analyses))
  chatSessions((chatSessions))
  reports((reports))
  dashboards((dashboards))

  users --> predictions
  users --> analyses
  users --> chatSessions
  users --> reports
  users --> dashboards
```

---

## Workflow Documentation

### Authentication Flow

```mermaid
sequenceDiagram
    participant App
    participant Backend
    participant Firestore
    App->>Backend: POST /api/auth/register
    Backend->>Firestore: create user document
    Firestore-->>Backend: user created
    Backend-->>App: auth token + profile
```

### Financial Analysis Flow

```mermaid
sequenceDiagram
    participant App
    participant Backend
    participant ML
    participant Firestore
    App->>Backend: POST /api/financial/health-score
    Backend->>ML: POST /health-score
    ML-->>Backend: score result
    Backend->>Firestore: save analysis
    Backend-->>App: health score response
```

### AI Processing Flow

```mermaid
sequenceDiagram
    participant App
    participant Backend
    participant ML
    App->>Backend: POST /api/chat/message
    Backend->>ML: POST /chat
    ML-->>Backend: AI response
    Backend->>Firestore: update chat session
    Backend-->>App: reply
```

### Loan Prediction Flow

```mermaid
sequenceDiagram
    participant App
    participant Backend
    participant ML
    participant Firestore
    App->>Backend: POST /api/loans/predict
    Backend->>ML: POST /predict
    ML-->>Backend: prediction result
    Backend->>Firestore: save prediction
    Backend-->>App: prediction output
```

---

## Installation Guide

### Prerequisites
- Java 11+ and Android SDK
- Android Studio
- Node.js 20+ and npm
- Python 3.11+
- Docker (for containerized deployment)
- Firebase service account JSON for Firestore access

### Clone Repository
```bash
git clone https://github.com/Compiler168/Loan-Analysis.git
cd Loan
```

### Backend Setup
```bash
cd backend
npm install
cp .env.template .env
# Update .env values, especially JWT_SECRET and ML_SERVICE_URL
```

### ML Service Setup
```bash
cd ../ml-service
python -m venv venv
venv\Scripts\Activate.ps1
pip install -r requirements.txt
cp .env.template .env
```

### Android Application Setup
- Open `Loan/android` in Android Studio
- Sync Gradle and install SDK components
- If needed, add `android/app/google-services.json` locally
- Run the app on emulator or device

---

## Running Guide

### Backend
```bash
cd backend
npm run dev
```
Backend listens on `http://localhost:5000` by default.

### ML Service
```bash
cd ml-service
venv\Scripts\Activate.ps1
python main.py
```
ML service listens on `http://localhost:8000`.

### Android Application
- Open Android Studio
- Run `app` module on device or emulator
- Ensure `ML_SERVICE_URL` and `MOBILE_ORIGINS` align with running services

---

## API Documentation

### Authentication
- `POST /api/auth/register`
  - Body: `{ name, email, password }`
  - Response: `{ token, firebaseCustomToken, user }`

- `POST /api/auth/login`
  - Body: `{ email, password }`
  - Response: `{ token, firebaseCustomToken, user }`

- `GET /api/auth/me`
  - Auth: Bearer token
  - Response: user profile

- `PUT /api/auth/profile`
  - Auth: Bearer token
  - Body: profile fields
  - Response: updated profile

### Loan Endpoints
- `POST /api/loans/predict`
  - Auth required
  - Body: loan and financial inputs
  - Response: ensemble probability, approval, risk factors

- `GET /api/loans/history`
  - Auth required
  - Response: prediction history

- `GET /api/loans/stats`
  - Auth required
  - Response: loan summary metrics

- `DELETE /api/loans/:id`
  - Auth required
  - Response: deletion status

### Financial Endpoints
- `GET /api/financial/dashboard`
  - Auth required
  - Response: dashboard summary

- `POST /api/financial/health-score`
  - Auth required
  - Body: financial profile
  - Response: computed health score

- `POST /api/financial/risk-analysis`
  - Auth required
  - Body: risk inputs
  - Response: risk analysis

- `POST /api/financial/simulate`
  - Auth required
  - Body: simulation variables
  - Response: projected scenario

- `GET /api/financial/latest`
  - Auth required
  - Response: latest analysis

- `GET /api/financial/history`
  - Auth required
  - Response: analysis history

### Chat Endpoints
- `POST /api/chat/message`
  - Auth required
  - Body: `{ message, sessionId }`
  - Response: AI chatbot reply

- `GET /api/chat/sessions`
  - Auth required
  - Response: saved chat sessions

- `DELETE /api/chat/:id`
  - Auth required
  - Response: deletion status

### Report Endpoints
- `POST /api/reports/generate`
  - Auth required
  - Body: `{ type }`
  - Response: report document

- `GET /api/reports/history`
  - Auth required
  - Response: report list

- `GET /api/reports/:id`
  - Auth required
  - Response: report details

- `DELETE /api/reports/:id`
  - Auth required
  - Response: delete status

---

## User Journey

1. User registers in the Android Application developed in Java.
2. The app sends credentials to `POST /api/auth/register`.
3. Backend creates the user in Firestore and returns JWT.
4. User logs in and receives a secured session.
5. User submits loan details to `POST /api/loans/predict`.
6. Backend forwards the request to the ML service and writes the result to Firestore.
7. User views predictions, health score, risk analysis, and report summaries.
8. User interacts with the AI chat assistant for financial guidance.
9. User can request reports and view history in-app.

---

## Future Enhancements

- Multi-bank integration with Open Banking APIs
- Fraud detection and anomaly monitoring
- AI chat assistant with voice support
- Predictive financial forecasting and budgeting
- Personalized loan marketplace recommendations
- Secure bank account linking and payment tracking
- Adaptive risk scoring using real-time market data

---

## Cleanup Report

### Completed cleanup actions
- Removed stale Vercel deployment wrapper and config files from the repository
- Removed generated local artifacts: `node_modules`, Python `venv`, Android build directories, IDE caches
- Removed orphaned serverless wrapper under `api/`
- Updated `.gitignore` to protect local secrets and credential files
- Preserved production-ready app code in `android/`, `backend/`, and `ml-service/`

### Notes
- Credentials such as `backend/firebase-key.json`, `android/app/google-services.json`, and the service account JSON remain local and ignored
- The repo now reflects a clean split between Android Java app, backend API, and ML microservice

---

## Optimization Report

### Key improvements
- Simplified deployment path by removing unused Vercel wrappers
- Centralized API route definitions and Firestore model access
- Preserved Docker-ready service definitions for backend and ML service
- Maintained GitHub Actions pipeline for CI, image publishing, and Fly.io deployment
- Improved security posture with explicit ignore rules for secrets

---

## Project Highlights

- Production-ready Android Application developed in Java
- FastAPI-based ML microservice with ensemble prediction
- Firestore-backed backend with secure JWT auth
- AI chatbot and financial simulation engine
- CI/CD with GitHub Actions and Docker

---

## Contributing Guidelines

Contributions should follow these rules:
- Keep changes small and focused
- Document new features in README or architecture docs
- Add tests for backend and ML service changes
- Avoid committing credentials or local environment files
- Maintain consistent code organization within `android/`, `backend/`, and `ml-service/`

---

## Developer Notes

- The Android Application is the primary user experience layer.
- The backend handles auth, persistence, and ML service orchestration.
- The ML service is a standalone FastAPI app designed to be deployed independently.
- Use `.env.template` files to configure local development.
- Run FastAPI and backend services locally before launching the Android app.

---

## License

This project is licensed under the terms of the [MIT License](LICENSE).
