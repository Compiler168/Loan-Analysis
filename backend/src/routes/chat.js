const express = require('express');
const router = express.Router();
const chatController = require('../controllers/ChatController');
const { authMiddleware } = require('../middleware/auth');

router.post('/message', authMiddleware, chatController.sendMessage);
router.get('/sessions', authMiddleware, chatController.getSessions);
router.delete('/:id', authMiddleware, chatController.deleteSession);

module.exports = router;
