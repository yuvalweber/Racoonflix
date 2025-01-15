const mongoose = require('mongoose');
const Counter = require('./counter');
const Schema = mongoose.Schema;

// Define the Category schema
const User = new Schema({
	firstName: {
		type: String,
		required: true
	},
	lastName: {
		type: String,
		required: true
	},
	userName: {
		type: String,
		required: true,
		unique: true
	},
	UserId : {
		type: Number,
		unique: true
	},
	email: {
		type: String,
		required: true,
		unique: true
	},
	password: {
		type: String,
		required: true
	},
	profilePicture: {
		type: String,
		default: ""
	},
	created_at: {
		type: Date,
		default: Date.now
	},
	seenMovies: [{
		movieId: { type: mongoose.Schema.Types.ObjectId, ref: 'Movie' },
		watchedAt: { type: Date, required: true }
	}],
	updateAt: {
		type: Date,
		default: Date.now
	}
});

// Increment the UserId field on each new User document
User.pre('save', async function (next) {
    if (!this.isNew) return next(); // Only increment on new documents

    try {
        const counter = await Counter.findOneAndUpdate(
            { fieldName: 'UserId' },
            { $inc: { seq: 1 } },
            { new: true, upsert: true } // Create if doesn't exist
        );
        this.UserId = counter.seq; // Assign the incremented value
        next();
    } catch (err) {
        next(err);
    }
});

module.exports = mongoose.model('User', User);