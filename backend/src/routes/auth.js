/**
 * SmartLoan AI+ — Auth Routes (MongoDB)
 */
const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');
const { authMiddleware } = require('../middleware/auth');
const User = require('../models/User');

const SECRET = process.env.JWT_SECRET || 'smartloan_ai_plus_jwt_secret_2026';

/**
 * Sign JWT Token Helper
 */
function signToken(user) {
  return jwt.sign(
    { id: user._id, email: user.email, name: user.name, role: user.role },
    SECRET, 
    { expiresIn: '30d' }
  );
}

/**
 * @route   POST /api/auth/register
 * @desc    Register a new user
 * @access  Public
 */
router.post('/register', async (req, res) => {
  try {
    const { name, email, password } = req.body;
    
    if (!name || !email || !password) {
      return res.status(400).json({ success: false, error: 'Name, email and password are required' });
    }

    const emailLower = email.toLowerCase();
    const existingUser = await User.findOne({ email: emailLower });
    if (existingUser) {
      return res.status(409).json({ success: false, error: 'Email already registered' });
    }

    const user = await User.create({ 
      name, 
      email: emailLower, 
      password 
    });

    const token = signToken(user);
    
    res.status(201).json({ 
      success: true, 
      message: 'User registered successfully',
      data: { 
        token, 
        user: { id: user._id, email: user.email, name: user.name, role: user.role } 
      } 
    });
  } catch (err) {
    console.error('Registration Error:', err.message);
    res.status(500).json({ success: false, error: 'Server error during registration' });
  }
});

/**
 * @route   POST /api/auth/login
 * @desc    Authenticate user & get token
 * @access  Public
 */
router.post('/login', async (req, res) => {
  try {
    const { email, password } = req.body;

    if (!email || !password) {
      return res.status(400).json({ success: false, error: 'Email and password are required' });
    }

    const user = await User.findOne({ email: email.toLowerCase() }).select('+password');
    if (!user) {
      return res.status(401).json({ success: false, error: 'Invalid email or password' });
    }

    const isMatch = await user.comparePassword(password);
    if (!isMatch) {
      return res.status(401).json({ success: false, error: 'Invalid email or password' });
    }

    // Update last login
    user.lastLogin = Date.now();
    await user.save();

    const token = signToken(user);
    
    res.json({ 
      success: true, 
      data: {
        token,
        user: { 
          id: user._id, 
          email: user.email, 
          name: user.name, 
          role: user.role, 
          profile: user.profile 
        }
      }
    });
  } catch (err) {
    console.error('Login Error:', err.message);
    res.status(500).json({ success: false, error: 'Server error during login' });
  }
});

/**
 * @route   GET /api/auth/me
 * @desc    Get current logged in user
 * @access  Private
 */
router.get('/me', authMiddleware, async (req, res) => {
  try {
    const user = await User.findById(req.user.id);
    if (!user) return res.status(404).json({ success: false, error: 'User not found' });
    
    res.json({ 
      success: true, 
      data: { 
        id: user._id, 
        email: user.email, 
        name: user.name, 
        role: user.role, 
        profile: user.profile 
      } 
    });
  } catch (err) {
    res.status(500).json({ success: false, error: 'Server error' });
  }
});

/**
 * @route   PUT /api/auth/profile
 * @desc    Update user profile data
 * @access  Private
 */
router.put('/profile', authMiddleware, async (req, res) => {
  try {
    const updateData = {};
    if (req.body.name) updateData.name = req.body.name;

    // Allowed profile fields
    const profileFields = [
      'monthly_income', 'monthly_expenses', 'credit_score', 'employment_status',
      'employment_years', 'existing_loans', 'existing_emi', 'savings_balance', 
      'dependents', 'age', 'property_value'
    ];

    profileFields.forEach(field => {
      if (req.body[field] !== undefined) {
        updateData[`profile.${field}`] = req.body[field];
      }
    });

    const user = await User.findByIdAndUpdate(
      req.user.id, 
      { $set: updateData }, 
      { new: true, runValidators: true }
    );

    if (!user) return res.status(404).json({ success: false, error: 'User not found' });
    
    res.json({ 
      success: true, 
      message: 'Profile updated successfully',
      data: { 
        id: user._id, 
        email: user.email, 
        name: user.name, 
        role: user.role, 
        profile: user.profile 
      } 
    });
  } catch (err) {
    console.error('Profile Update Error:', err.message);
    res.status(500).json({ success: false, error: 'Failed to update profile' });
  }
});

module.exports = router;
