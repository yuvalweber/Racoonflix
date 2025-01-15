const tokenService = require('../services/token');

//check if the user is signed in
const isSignedIn = async (req, res) => {
	if (!req.body.userName || !req.body.password) {
		return res.status(400).json({
			errors: 'Missing required fields'
		});
	}
	const userId = await tokenService.isSignedIn(req.body.userName, req.body.password);
	if (!userId) {
		return res.status(404).json({
			errors: 'User not found'
		});
	}
	res.json({"x-user":userId});
};

module.exports = { isSignedIn };