/**
 * SmartLoan AI+ — Report Routes
 */
const express = require('express');
const router = express.Router();
const { authMiddleware } = require('../middleware/auth');

router.post('/generate', authMiddleware, (req, res) => {
  const { type = 'financial_summary' } = req.body;
  const report = {
    id: Date.now().toString(),
    type,
    generated_at: new Date().toISOString(),
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
    ]
  };
  res.json({ success: true, data: report });
});

router.get('/history', authMiddleware, (req, res) => {
  res.json({ success: true, data: [
    { id: '1', type: 'financial_summary', title: 'May 2026 Summary', date: '2026-05-01' },
    { id: '2', type: 'loan_analysis', title: 'Loan Analysis', date: '2026-04-28' },
    { id: '3', type: 'risk_report', title: 'Q1 Risk Report', date: '2026-04-15' }
  ]});
});

module.exports = router;
