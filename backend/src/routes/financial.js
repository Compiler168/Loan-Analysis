const express = require('express');
const router = express.Router();
const financialController = require('../controllers/FinancialController');
const { authMiddleware } = require('../middleware/auth');

router.post('/analyze', authMiddleware, financialController.analyzeHealth);
router.get('/latest', authMiddleware, financialController.getLatestAnalysis);
router.get('/history', authMiddleware, financialController.getAnalysisHistory);

module.exports = router;
