/**
 * SmartLoan AI+ — Loan Controller
 */
const axios = require('axios');
const Prediction = require('../models/Prediction');

const ML_URL = process.env.ML_SERVICE_URL || 'http://localhost:8000';

exports.predictLoan = async (req, res) => {
  try {
    const response = await axios.post(`${ML_URL}/predict`, req.body);
    const resultData = response.data.data;

    const prediction = await Prediction.create({
      userId: req.user.id,
      input: req.body,
      result: resultData,
      status: resultData.ensemble.approved ? 'approved' : 'rejected',
    });

    res.status(201).json({ 
      success: true, 
      data: { id: prediction._id, ...resultData }
    });
  } catch (err) {
    const msg = err.response?.data?.detail || 'ML service unavailable';
    res.status(502).json({ success: false, error: msg });
  }
};

exports.getHistory = async (req, res) => {
  try {
    const predictions = await Prediction.find({ userId: req.user.id })
      .sort({ createdAt: -1 })
      .limit(50);

    const history = predictions.map(p => ({
      id: p._id,
      date: p.createdAt.toISOString().split('T')[0],
      amount: p.input.loan_amount,
      status: p.status,
      probability: p.result?.ensemble?.probability || 0,
    }));

    res.json({ success: true, count: history.length, data: history });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Failed to fetch history' });
  }
};

exports.getStats = async (req, res) => {
  try {
    const total = await Prediction.countDocuments({ userId: req.user.id });
    const approved = await Prediction.countDocuments({ userId: req.user.id, status: 'approved' });
    const latest = await Prediction.findOne({ userId: req.user.id }).sort({ createdAt: -1 });

    res.json({ 
      success: true, 
      data: {
        total_predictions: total,
        approved_count: approved,
        rejection_count: total - approved,
        approval_rate: total > 0 ? ((approved / total) * 100).toFixed(1) : "0.0",
        latest_probability: latest?.result?.ensemble?.probability || 0
      }
    });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Failed to fetch stats' });
  }
};

exports.deletePrediction = async (req, res) => {
  try {
    const result = await Prediction.findOneAndDelete({ _id: req.params.id, userId: req.user.id });
    if (!result) return res.status(404).json({ success: false, error: 'Record not found' });
    res.json({ success: true, message: 'Record deleted successfully' });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Delete failed' });
  }
};
