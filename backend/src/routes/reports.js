/**
 * SmartLoan AI+ — Report Routes (MongoDB)
 */
const express = require('express');
const router = express.Router();
const { authMiddleware } = require('../middleware/auth');
const Report = require('../models/Report');

/**
 * @route   POST /api/reports/generate
 * @desc    Generate and save a financial report
 * @access  Private
 */
router.post('/generate', authMiddleware, async (req, res) => {
  try {
    const { type = 'financial_summary' } = req.body;
    
    // In a real app, this would involve complex logic.
    // Here we generate the structure and save it.
    const reportData = {
      userId: req.user.id,
      type,
      title: type === 'loan_analysis' ? 'Loan Analysis Report' :
             type === 'risk_report' ? 'Risk Assessment Report' :
             type === 'ai_recommendations' ? 'AI Recommendations Report' : 'Financial Summary Report',
      sections: [
        { title: 'Executive Summary', content: 'This report provides a comprehensive analysis of your financial position based on AI-driven metrics.' },
        { title: 'Key Metrics', items: [
          { label: 'Financial Health Score', value: '72/100', status: 'good' },
          { label: 'Loan Approval Probability', value: '78.5%', status: 'good' },
          { label: 'Risk Level', value: 'Moderate', status: 'warning' },
          { label: 'DTI Ratio', value: '38%', status: 'warning' },
          { label: 'Credit Score', value: '720', status: 'good' }
        ]},
        { title: 'AI Recommendations', items: [
          { label: 'Savings', value: 'Increase emergency fund to 6 months of expenses' },
          { label: 'Debt', value: 'Pay off highest-interest loans first (avalanche method)' },
          { label: 'Credit', value: 'Keep credit utilization below 30%' },
          { label: 'Income', value: 'Explore additional income streams for financial resilience' }
        ]},
        { title: 'Conclusion', content: 'Your financial health is satisfactory with room for improvement. Focus on reducing DTI ratio and building emergency savings.' }
      ],
      metadata: { 
        health_score: 72, 
        loan_probability: 78.5, 
        risk_level: 'moderate', 
        credit_score: 720 
      }
    };

    const savedReport = await Report.create(reportData);

    res.status(201).json({ 
      success: true, 
      message: 'Report generated and saved to database',
      data: savedReport 
    });
  } catch (err) {
    console.error('Report Generation Error:', err.message);
    res.status(500).json({ success: false, error: 'Failed to generate report' });
  }
});

/**
 * @route   GET /api/reports/history
 * @desc    Get user report history
 * @access  Private
 */
router.get('/history', authMiddleware, async (req, res) => {
  try {
    const reports = await Report.find({ userId: req.user.id })
      .sort({ createdAt: -1 })
      .select('type title createdAt');

    const history = reports.map(r => ({
      id: r._id,
      type: r.type,
      title: r.title,
      date: r.createdAt.toISOString().split('T')[0],
    }));

    res.json({ success: true, count: history.length, data: history });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Failed to fetch report history' });
  }
});

/**
 * @route   GET /api/reports/:id
 * @desc    Get full report details
 * @access  Private
 */
router.get('/:id', authMiddleware, async (req, res) => {
  try {
    const report = await Report.findOne({ _id: req.params.id, userId: req.user.id });
    if (!report) return res.status(404).json({ success: false, error: 'Report not found' });
    
    res.json({ success: true, data: report });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Failed to fetch report' });
  }
});

/**
 * @route   DELETE /api/reports/:id
 * @desc    Delete a report
 * @access  Private
 */
router.delete('/:id', authMiddleware, async (req, res) => {
  try {
    const result = await Report.findOneAndDelete({ _id: req.params.id, userId: req.user.id });
    if (!result) return res.status(404).json({ success: false, error: 'Report not found' });
    
    res.json({ success: true, message: 'Report deleted' });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Delete failed' });
  }
});

module.exports = router;
