/**
 * SmartLoan AI+ — Chat Routes (MongoDB)
 */
const express = require('express');
const router = express.Router();
const axios = require('axios');
const { authMiddleware } = require('../middleware/auth');
const ChatSession = require('../models/ChatSession');

const ML_URL = process.env.ML_SERVICE_URL || 'http://localhost:8000';

/**
 * @route   POST /api/chat/message
 * @desc    Send message to AI and persist conversation
 * @access  Private
 */
router.post('/message', authMiddleware, async (req, res) => {
  try {
    const { message, sessionId } = req.body;

    // 1. Get AI response from ML service
    const aiResponse = await axios.post(`${ML_URL}/chat`, { message });
    const reply = aiResponse.data.reply;

    // 2. Persist to MongoDB
    let session;
    if (sessionId) {
      session = await ChatSession.findOne({ _id: sessionId, userId: req.user.id });
    }

    if (!session) {
      session = new ChatSession({
        userId: req.user.id,
        messages: []
      });
    }

    session.messages.push({ role: 'user', content: message });
    session.messages.push({ role: 'assistant', content: reply });
    session.lastMessageAt = Date.now();
    
    await session.save();

    res.json({ 
      success: true, 
      data: {
        sessionId: session._id,
        reply: reply
      }
    });
  } catch (err) {
    console.error('Chat Error:', err.message);
    res.status(500).json({ success: false, error: 'AI Chat unavailable' });
  }
});

/**
 * @route   GET /api/chat/sessions
 * @desc    Get user chat history
 * @access  Private
 */
router.get('/sessions', authMiddleware, async (req, res) => {
  try {
    const sessions = await ChatSession.find({ userId: req.user.id })
      .sort({ lastMessageAt: -1 });
    
    res.json({ success: true, data: sessions });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Failed to fetch chats' });
  }
});

/**
 * @route   DELETE /api/chat/:id
 * @desc    Delete a chat session
 * @access  Private
 */
router.delete('/:id', authMiddleware, async (req, res) => {
  try {
    await ChatSession.findOneAndDelete({ _id: req.params.id, userId: req.user.id });
    res.json({ success: true, message: 'Chat deleted' });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Delete failed' });
  }
});

module.exports = router;
