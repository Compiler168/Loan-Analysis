/**
 * SmartLoan AI+ — Express.js Backend Server
 */
require('dotenv').config();
const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');

const app = express();
const PORT = process.env.PORT || 5000;

// Security
app.use(helmet());
app.use(cors({ origin: ['http://localhost:3000', 'http://localhost:3001'], credentials: true }));
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true }));

// Rate Limiting
app.use('/api/', rateLimit({ windowMs: 15 * 60 * 1000, max: 200, message: { error: 'Too many requests' } }));
const aiLimiter = rateLimit({ windowMs: 15 * 60 * 1000, max: 50, message: { error: 'AI rate limit reached' } });

// Routes
app.use('/api/auth', require('./routes/auth'));
app.use('/api/loans', aiLimiter, require('./routes/loans'));
app.use('/api/financial', aiLimiter, require('./routes/financial'));
app.use('/api/chat', aiLimiter, require('./routes/chat'));
app.use('/api/reports', require('./routes/reports'));
app.use('/api/admin', require('./routes/admin'));

// Health
app.get('/api/health', (req, res) => {
  res.json({ status: 'healthy', service: 'SmartLoan AI+', timestamp: new Date().toISOString() });
});

// Error handler
app.use((err, req, res, next) => {
  console.error('Error:', err.message);
  res.status(err.status || 500).json({ success: false, error: process.env.NODE_ENV === 'production' ? 'Internal error' : err.message });
});

app.use((req, res) => res.status(404).json({ success: false, error: 'Not found' }));

app.listen(PORT, () => {
  console.log(`\n🚀 SmartLoan AI+ Backend on port ${PORT}`);
  console.log(`📡 ML Service: ${process.env.ML_SERVICE_URL || 'http://localhost:8000'}`);
  console.log(`🔗 API: http://localhost:${PORT}/api\n`);
});

module.exports = app;
