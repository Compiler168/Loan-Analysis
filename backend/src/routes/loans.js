/**
 * SmartLoan AI+ — Loan Routes
 */
const express = require('express');
const router = express.Router();
const axios = require('axios');
const { authMiddleware } = require('../middleware/auth');

const ML_URL = process.env.ML_SERVICE_URL || 'http://localhost:8000';

// POST /api/loans/predict
router.post('/predict', authMiddleware, async (req, res) => {
  try {
    const response = await axios.post(`${ML_URL}/predict`, req.body);
    res.json({ success: true, data: response.data.data });
  } catch (err) {
    const msg = err.response?.data?.detail || 'ML service unavailable';
    res.status(502).json({ success: false, error: msg });
  }
});

// GET /api/loans/history
router.get('/history', authMiddleware, (req, res) => {
  res.json({ success: true, data: [
    { id: 1, date: '2026-05-01', amount: 50000, status: 'approved', probability: 0.82 },
    { id: 2, date: '2026-04-15', amount: 75000, status: 'review', probability: 0.65 },
    { id: 3, date: '2026-03-20', amount: 30000, status: 'approved', probability: 0.91 },
  ]});
});

module.exports = router;
