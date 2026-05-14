const express = require('express');
const router = express.Router();
const loanController = require('../controllers/LoanController');
const { authMiddleware } = require('../middleware/auth');

router.post('/predict', authMiddleware, loanController.predictLoan);
router.get('/history', authMiddleware, loanController.getHistory);
router.get('/stats', authMiddleware, loanController.getStats);
router.delete('/:id', authMiddleware, loanController.deletePrediction);

module.exports = router;
