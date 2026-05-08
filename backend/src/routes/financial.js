/**
 * SmartLoan AI+ — Financial Analysis Routes
 */
const express = require('express');
const router = express.Router();
const axios = require('axios');
const { authMiddleware } = require('../middleware/auth');

const ML_URL = process.env.ML_SERVICE_URL || 'http://localhost:8000';

router.post('/health-score', authMiddleware, async (req, res) => {
  try {
    const r = await axios.post(`${ML_URL}/health-score`, req.body);
    res.json({ success: true, data: r.data.data });
  } catch (err) { res.status(502).json({ success: false, error: 'ML service unavailable' }); }
});

router.post('/risk-analysis', authMiddleware, async (req, res) => {
  try {
    const r = await axios.post(`${ML_URL}/risk-analysis`, req.body);
    res.json({ success: true, data: r.data.data });
  } catch (err) { res.status(502).json({ success: false, error: 'ML service unavailable' }); }
});

router.post('/simulate', authMiddleware, async (req, res) => {
  try {
    const r = await axios.post(`${ML_URL}/simulate`, req.body);
    res.json({ success: true, data: r.data.data });
  } catch (err) { res.status(502).json({ success: false, error: 'ML service unavailable' }); }
});

// GET /api/financial/dashboard
router.get('/dashboard', authMiddleware, async (req, res) => {
  try {
    const data = {
      loan_probability: 78.5, health_score: 72, risk_level: 'moderate',
      credit_score: 720, monthly_savings: 1850, dti_ratio: 0.38,
      insights: [
        { type: 'tip', icon: '💡', title: 'Savings Opportunity', message: 'Increasing savings by 10% could improve your health score by 5 points.' },
        { type: 'alert', icon: '⚠️', title: 'EMI Alert', message: 'Your EMI burden is approaching 40%. Consider paying off smaller loans first.' },
        { type: 'success', icon: '✅', title: 'Credit Improvement', message: 'Your credit score improved by 15 points this quarter. Keep it up!' },
        { type: 'info', icon: '📊', title: 'Market Update', message: 'Average loan rates dropped 0.25% this month. Good time to refinance.' }
      ],
      income_vs_expenses: [
        { month: 'Jan', income: 7500, expenses: 3200 }, { month: 'Feb', income: 7500, expenses: 3400 },
        { month: 'Mar', income: 7800, expenses: 3100 }, { month: 'Apr', income: 7800, expenses: 3500 },
        { month: 'May', income: 8000, expenses: 3300 }, { month: 'Jun', income: 8000, expenses: 3600 }
      ],
      financial_growth: [
        { month: 'Jan', savings: 22000, investments: 15000, net_worth: 287000 },
        { month: 'Feb', savings: 23500, investments: 15800, net_worth: 289300 },
        { month: 'Mar', savings: 25200, investments: 16200, net_worth: 291400 },
        { month: 'Apr', savings: 26000, investments: 17000, net_worth: 293000 },
        { month: 'May', savings: 27500, investments: 17500, net_worth: 295000 },
        { month: 'Jun', savings: 28500, investments: 18200, net_worth: 296700 }
      ],
      risk_radar: [
        { category: 'Credit', value: 82 }, { category: 'DTI', value: 65 },
        { category: 'Savings', value: 70 }, { category: 'Stability', value: 85 },
        { category: 'EMI Load', value: 60 }
      ],
      emi_forecast: [
        { month: 'Jul', emi: 1620, remaining: 48500 }, { month: 'Aug', emi: 1620, remaining: 46880 },
        { month: 'Sep', emi: 1620, remaining: 45260 }, { month: 'Oct', emi: 1620, remaining: 43640 },
        { month: 'Nov', emi: 1620, remaining: 42020 }, { month: 'Dec', emi: 1620, remaining: 40400 }
      ],
      recent_activity: [
        { type: 'prediction', message: 'Loan prediction completed', time: '2 hours ago', result: 'approved' },
        { type: 'score', message: 'Health score updated', time: '1 day ago', result: '72/100' },
        { type: 'chat', message: 'AI advisor session', time: '2 days ago', result: '5 insights' },
        { type: 'simulation', message: 'Salary increase sim', time: '3 days ago', result: '+$2.4k savings' }
      ]
    };
    res.json({ success: true, data });
  } catch (err) { res.status(500).json({ success: false, error: err.message }); }
});

module.exports = router;
