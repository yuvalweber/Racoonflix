const userService = require('../services/user');
const UserModel = require('../models/user');

// create a new user
const createUser = async (req, res) => {
	// check if provided all the required keys in the request body
	const requiredKeys = Object.keys(UserModel.schema.obj).filter(key => UserModel.schema.obj[key].required);
	const missingKeys = requiredKeys.filter(key => !req.body.hasOwnProperty(key));
	if (missingKeys.length > 0) {
		return res.status(400).json({ errors: 'Missing required keys in the request body' });
	}
	// Validate keys in the request body
	const invalidKeys = Object.keys(req.body).filter(
		key => !UserModel.schema.obj.hasOwnProperty(key)
	);
	if (invalidKeys.length > 0) {
		return res.status(400).json({ errors: 'Data provided contains invalid keys' });
	}
			
	const user =  await userService.createUser(req.body);
	if (!user) {
		return res.status(404).json({
			errors: 'one of the movies does not exist or userName is not unique'
		});
	}
	res.status(201).location(`/api/users/${user._id}`).json();
};

//get user by id
const getUser = async (req, res) => {
	const user = await userService.getUserById(req.params.id);
	if (!user) {
		return res.status(404).json({
			errors: 'User not found'
		});
	}
	res.json(user);
};

module.exports = {
	createUser,
	getUser
};