/**
 * SmartLoan AI+ — Admin Routes
 */
const express = require('express');
const router = express.Router();
const axios = require('axios');
const { authMiddleware, adminMiddleware } = require('../middleware/auth');

const ML_URL = process.env.ML_SERVICE_URL || 'http://localhost:8000';

router.get('/stats', authMiddleware, (req, res) => {
  res.json({ success: true, data: {
    total_users: 1247, active_sessions: 89, predictions_today: 156,
    total_predictions: 45230, avg_health_score: 68, chatbot_sessions: 342,
    system_uptime: '99.7%', ml_service_status: 'healthy',
    monthly_stats: [
      { month: 'Jan', users: 890, predictions: 3200 }, { month: 'Feb', users: 950, predictions: 3800 },
      { month: 'Mar', users: 1050, predictions: 4200 }, { month: 'Apr', users: 1150, predictions: 4800 },
      { month: 'May', users: 1247, predictions: 5100 }
    ]
  }});
});

router.get('/users', authMiddleware, (req, res) => {
  res.json({ success: true, data: [
    { id: '1', name: 'Demo User', email: 'demo@smartloan.ai', role: 'admin', status: 'active', lastLogin: '2026-05-07' },
    { id: '2', name: 'Jane Smith', email: 'jane@example.com', role: 'user', status: 'active', lastLogin: '2026-05-06' },
    { id: '3', name: 'Bob Wilson', email: 'bob@example.com', role: 'user', status: 'active', lastLogin: '2026-05-05' },
    { id: '4', name: 'Alice Brown', email: 'alice@example.com', role: 'user', status: 'inactive', lastLogin: '2026-04-20' }
  ]});
});

router.get('/model-info', authMiddleware, async (req, res) => {
  try {
    const r = await axios.get(`${ML_URL}/model-info`);
    res.json({ success: true, data: r.data.data });
  } catch (err) { res.status(502).json({ success: false, error: 'ML service unavailable' }); }
});

module.exports = router;
