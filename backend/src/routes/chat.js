/**
 * SmartLoan AI+ — Chat Routes
 */
const express = require('express');
const router = express.Router();
const axios = require('axios');
const multer = require('multer');
const { authMiddleware } = require('../middleware/auth');

const ML_URL = process.env.ML_SERVICE_URL || 'http://localhost:8000';
const upload = multer({ storage: multer.memoryStorage(), limits: { fileSize: 10 * 1024 * 1024 } });

router.post('/message', authMiddleware, async (req, res) => {
  try {
    const r = await axios.post(`${ML_URL}/chat`, req.body);
    res.json({ success: true, data: r.data.data });
  } catch (err) { res.status(502).json({ success: false, error: 'AI service unavailable' }); }
});

router.post('/analyze-document', authMiddleware, upload.single('file'), async (req, res) => {
  try {
    if (!req.file) return res.status(400).json({ success: false, error: 'No file uploaded' });
    const FormData = (await import('form-data')).default || require('form-data');
    const form = new FormData();
    form.append('file', req.file.buffer, { filename: req.file.originalname, contentType: req.file.mimetype });
    const r = await axios.post(`${ML_URL}/analyze-document`, form, { headers: form.getHeaders() });
    res.json({ success: true, data: r.data.data });
  } catch (err) { res.status(502).json({ success: false, error: 'Document analysis failed' }); }
});

module.exports = router;
