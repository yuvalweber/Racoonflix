const userService = require('./user');
const jwt = require('jsonwebtoken');
require('custom-env').env(process.env.NODE_ENV, './config');

// check if the header x-user is provided and if the user exists
const checkUserHeader = async (userHeader) => {
    if (!userHeader) {
        return { status: 400, message: 'Authorization header is required' };
    }
	const userHeaderVal = userHeader.split(' ')[1];
	let userId;
	jwt.verify(userHeaderVal, process.env.JWT_SECRET, (err, decoded) => {
		if (err) {
			return { status: 401, message: 'Invalid token' };
		}
		userId = decoded.id;
	});

	const user = userService.getUserById(userId);
	if (!user) {
		return { status: 404, message: 'User not found' };
	}
	return { status: 200, message: userId };
};

module.exports = {
    checkUserHeader
};