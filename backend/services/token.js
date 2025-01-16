const jwt = require('jsonwebtoken'); // Import the JWT library
const User = require('../models/user');	
require('custom-env').env(process.env.NODE_ENV, './config');

// Secret key for signing the JWT (you should store this in an environment variable)
const JWT_SECRET = process.env.JWT_SECRET;

// Check if the user is signed in and return a JWT token
const isSignedIn = async (userName, password) => {
	try {
		// Find the user by userName
		const user = await User.findOne({ userName: userName });
		if (!user) return null;

		// Check if the provided password matches the user's password
		if (user.password !== password) return null;

		// Generate a JWT token
		const token = jwt.sign(
			{ id: user._id, isAdmin: user.isAdmin}, // Payload
			JWT_SECRET, // Secret key
			{ expiresIn: '1h' } // Token expiration time
		);
		// Return the token
		return token;

	} catch (err) {
		return null;
	}
};

// get the user info from the token
const getTokenInfo = async (token) => {
	try {
		// Decode the token
		let tokenInfo;
		jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
			if (err) {
				return null;
			}
			tokenInfo = decoded;
		});
		// Return the decoded token
		return tokenInfo;
	} catch (err) {
		return null;
	}
}
module.exports = { isSignedIn, getTokenInfo };
