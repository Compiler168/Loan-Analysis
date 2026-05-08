/**
 * SmartLoan AI+ — Auth Routes
 */
const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { v4: uuidv4 } = require('uuid');
const { authMiddleware } = require('../middleware/auth');

const SECRET = process.env.JWT_SECRET || 'smartloan_ai_plus_jwt_secret_2026';

// In-memory user store (replace with Firebase in production)
const users = new Map();

// Seed demo user
const demoId = uuidv4();
users.set('demo@smartloan.ai', {
  id: demoId, email: 'demo@smartloan.ai', name: 'Demo User',
  password: bcrypt.hashSync('demo123', 10), role: 'admin',
  profile: { monthly_income: 7500, monthly_expenses: 3000, credit_score: 720,
    employment_status: 'salaried', employment_years: 5, existing_loans: 1,
    existing_emi: 500, savings_balance: 25000, dependents: 1, age: 35 },
  createdAt: new Date().toISOString()
});

function signToken(user) {
  return jwt.sign({ id: user.id, email: user.email, name: user.name, role: user.role }, SECRET, { expiresIn: '7d' });
}

// POST /api/auth/register
router.post('/register', async (req, res) => {
  try {
    const { name, email, password } = req.body;
    if (!name || !email || !password) return res.status(400).json({ success: false, error: 'All fields required' });
    if (users.has(email)) return res.status(409).json({ success: false, error: 'Email already registered' });
    const hashed = await bcrypt.hash(password, 10);
    const user = { id: uuidv4(), email, name, password: hashed, role: 'user', profile: {}, createdAt: new Date().toISOString() };
    users.set(email, user);
    const token = signToken(user);
    res.status(201).json({ success: true, data: { token, user: { id: user.id, email, name, role: 'user' } } });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

// POST /api/auth/login
router.post('/login', async (req, res) => {
  try {
    const { email, password } = req.body;
    const user = users.get(email);
    if (!user) return res.status(401).json({ success: false, error: 'Invalid credentials' });
    const valid = await bcrypt.compare(password, user.password);
    if (!valid) return res.status(401).json({ success: false, error: 'Invalid credentials' });
    const token = signToken(user);
    res.json({ success: true, data: { token, user: { id: user.id, email: user.email, name: user.name, role: user.role, profile: user.profile } } });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

// GET /api/auth/me
router.get('/me', authMiddleware, (req, res) => {
  const user = [...users.values()].find(u => u.id === req.user.id);
  if (!user) return res.status(404).json({ success: false, error: 'User not found' });
  res.json({ success: true, data: { id: user.id, email: user.email, name: user.name, role: user.role, profile: user.profile } });
});

// PUT /api/auth/profile
router.put('/profile', authMiddleware, (req, res) => {
  const user = [...users.values()].find(u => u.id === req.user.id);
  if (!user) return res.status(404).json({ success: false, error: 'User not found' });
  user.profile = { ...user.profile, ...req.body };
  if (req.body.name) user.name = req.body.name;
  res.json({ success: true, data: { id: user.id, email: user.email, name: user.name, role: user.role, profile: user.profile } });
});

module.exports = router;
