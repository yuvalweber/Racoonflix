const mongoose = require('mongoose');
const Category = require('./category');
const Counter = require('./counter');
const Schema = mongoose.Schema;

// Define the Movie schema
const Movie = new Schema({
    title: {
        type: String,
        required: true,
		unique: true
    },
    year: {
        type: Number,
        default: Date.now().getFullYear
    },
    category: [{
        type: Schema.Types.ObjectId,
        ref: 'Category',
        default: []
    }],
    director: {
        type: String,
        required: true
    },
    duration: {
        type: Number,
        required: true
    },
    image: {
        type: String,
        required: true
    },
    trailer: {
        type: String,
        required: true
    },
    movieId : {
        type: Number,
        unique: true
    },
});

// Increment the MovieId field on each new Movie document
Movie.pre('save', async function (next) {
    if (!this.isNew) return next(); // Only increment on new documents

    try {
        const counter = await Counter.findOneAndUpdate(
            { fieldName: 'MovieId' },
            { $inc: { seq: 1 } },
            { new: true, upsert: true } // Create if doesn't exist
        );
        this.movieId = counter.seq; // Assign the incremented value
        next();
    } catch (err) {
        return next(err);
    }
});
module.exports = mongoose.model('Movie', Movie);