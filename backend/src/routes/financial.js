/**
 * SmartLoan AI+ — Financial Analysis Routes (MongoDB)
 */
const express = require('express');
const router = express.Router();
const axios = require('axios');
const { authMiddleware } = require('../middleware/auth');
const Analysis = require('../models/Analysis');

const ML_URL = process.env.ML_SERVICE_URL || 'http://localhost:8000';

/**
 * @route   POST /api/financial/analyze
 * @desc    Analyze financial health and save to database
 * @access  Private
 */
router.post('/analyze', authMiddleware, async (req, res) => {
  try {
    // 1. Get health score from ML service
    const response = await axios.post(`${ML_URL}/health_score`, req.body);
    const analysisData = response.data.data;

    // 2. Persist to MongoDB
    const analysis = await Analysis.create({
      userId: req.user.id,
      input: req.body,
      results: analysisData
    });

    res.status(201).json({ 
      success: true, 
      message: 'Analysis complete and saved',
      data: analysis 
    });
  } catch (err) {
    console.error('Analysis Error:', err.message);
    res.status(500).json({ success: false, error: 'Health scoring service unavailable' });
  }
});

/**
 * @route   GET /api/financial/latest
 * @desc    Get latest health analysis
 * @access  Private
 */
router.get('/latest', authMiddleware, async (req, res) => {
  try {
    const latest = await Analysis.findOne({ userId: req.user.id }).sort({ createdAt: -1 });
    if (!latest) return res.status(404).json({ success: false, error: 'No analysis found' });

    res.json({ success: true, data: latest });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Fetch failed' });
  }
});

/**
 * @route   GET /api/financial/history
 * @desc    Get health score history for charts
 * @access  Private
 */
router.get('/history', authMiddleware, async (req, res) => {
  try {
    const history = await Analysis.find({ userId: req.user.id })
      .sort({ createdAt: 1 })
      .limit(10)
      .select('createdAt results.score');

    const formattedHistory = history.map(h => ({
      date: h.createdAt.toISOString().split('T')[0],
      score: h.results.score
    }));

    res.json({ success: true, data: formattedHistory });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Fetch failed' });
  }
});

module.exports = router;
