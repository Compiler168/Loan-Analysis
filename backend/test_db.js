const mongoose = require('mongoose');

async function test() {
  const uri = "mongodb+srv://majidiqbal1688_db_user:AXvvbXu3jqinuqB1@cluster0.zzyzsio.mongodb.net/smartloan_db?retryWrites=true&w=majority";
  console.log('Connecting to:', uri);
  try {
    await mongoose.connect(uri, { serverSelectionTimeoutMS: 10000 });
    console.log('✅ Success!');
    process.exit(0);
  } catch (err) {
    console.error('❌ Failed:', err.message);
    process.exit(1);
  }
}

test();
