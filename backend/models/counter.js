// this is for auto incrementing the id field in the database (like user id, movie id, etc.)
const mongoose = require('mongoose');

const CounterSchema = new mongoose.Schema({
    fieldName: { type: String, required: true, unique: true },
    seq: { type: Number, default: 1 },
});

module.exports = mongoose.model('Counter', CounterSchema);