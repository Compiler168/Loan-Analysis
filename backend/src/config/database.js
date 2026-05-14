/**
 * SmartLoan AI+ — MongoDB Connection
 * Professional Database Configuration
 */
const mongoose = require('mongoose');

const connectDB = async () => {
  try {
    const uri = process.env.MONGODB_URI;
    if (!uri) {
      console.error('CRITICAL: MONGODB_URI is not defined in .env file');
      process.exit(1);
    }

    const conn = await mongoose.connect(uri, {
      serverSelectionTimeoutMS: 5000,
      socketTimeoutMS: 45000,
    });

    console.log(`\n📦 MongoDB Atlas Connected: ${conn.connection.host}`);
    console.log(`📂 Database Name: ${conn.connection.name}`);
    return true;
  } catch (err) {
    console.error(`\n❌ MongoDB Connection Error: ${err.message}`);
    // In production, we want to exit if DB fails
    if (process.env.NODE_ENV === 'production') {
      process.exit(1);
    }
    return false;
  }
};

// Handle connection events
mongoose.connection.on('error', err => {
  console.error(`MongoDB Runtime Error: ${err.message}`);
});

mongoose.connection.on('disconnected', () => {
  console.warn('MongoDB Disconnected. Attempting to reconnect...');
});

module.exports = connectDB;
