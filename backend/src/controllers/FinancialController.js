/**
 * SmartLoan AI+ — Financial Controller
 */
const axios = require('axios');
const Analysis = require('../models/Analysis');

const ML_URL = process.env.ML_SERVICE_URL || 'http://localhost:8000';

exports.analyzeHealth = async (req, res) => {
  try {
    const response = await axios.post(`${ML_URL}/health_score`, req.body);
    const analysisData = response.data.data;

    const analysis = await Analysis.create({
      userId: req.user.id,
      input: req.body,
      results: analysisData
    });

    res.status(201).json({ success: true, data: analysis });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Health scoring service unavailable' });
  }
};

exports.getLatestAnalysis = async (req, res) => {
  try {
    const latest = await Analysis.findOne({ userId: req.user.id }).sort({ createdAt: -1 });
    if (!latest) return res.status(404).json({ success: false, error: 'No analysis found' });
    res.json({ success: true, data: latest });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Fetch failed' });
  }
};

exports.getAnalysisHistory = async (req, res) => {
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
};
