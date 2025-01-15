const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// Define the Category schema
const Category = new Schema({
    name: {
        type: String,
        required: true,
		unique: true
    },
	promoted: {
		type: Boolean,
		default: false
	},
	movies: [{
		type: Schema.Types.ObjectId,
		ref: 'Movie',
		default: []
	}],
});
module.exports = mongoose.model('Category', Category);