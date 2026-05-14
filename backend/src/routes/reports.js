const express = require('express');
const router = express.Router();
const reportController = require('../controllers/ReportController');
const { authMiddleware } = require('../middleware/auth');

router.post('/generate', authMiddleware, reportController.generateReport);
router.get('/history', authMiddleware, reportController.getHistory);
router.get('/:id', authMiddleware, reportController.getReportDetail);
router.delete('/:id', authMiddleware, reportController.deleteReport);

module.exports = router;
