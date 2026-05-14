/**
 * SmartLoan AI+ — Chat Controller
 */
const axios = require('axios');
const ChatSession = require('../models/ChatSession');

const ML_URL = process.env.ML_SERVICE_URL || 'http://localhost:8000';

exports.sendMessage = async (req, res) => {
  try {
    const { message, sessionId } = req.body;
    const aiResponse = await axios.post(`${ML_URL}/chat`, { message });
    const reply = aiResponse.data.reply;

    let session;
    if (sessionId) {
      session = await ChatSession.findOne({ _id: sessionId, userId: req.user.id });
    }

    if (!session) {
      session = new ChatSession({ userId: req.user.id, messages: [] });
    }

    session.messages.push({ role: 'user', content: message });
    session.messages.push({ role: 'assistant', content: reply });
    session.lastMessageAt = Date.now();
    await session.save();

    res.json({ success: true, data: { sessionId: session._id, reply } });
  } catch (err) {
    res.status(500).json({ success: false, error: 'AI Chat unavailable' });
  }
};

exports.getSessions = async (req, res) => {
  try {
    const sessions = await ChatSession.find({ userId: req.user.id }).sort({ lastMessageAt: -1 });
    res.json({ success: true, data: sessions });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Failed to fetch chats' });
  }
};

exports.deleteSession = async (req, res) => {
  try {
    await ChatSession.findOneAndDelete({ _id: req.params.id, userId: req.user.id });
    res.json({ success: true, message: 'Chat deleted' });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Delete failed' });
  }
};
