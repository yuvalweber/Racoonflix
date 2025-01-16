const tokenService = require('../services/token');

//check if the user is signed in
const isSignedIn = async (req, res) => {
	if (!req.body.userName || !req.body.password) {
		return res.status(400).json({
			errors: 'Missing required fields'
		});
	}
	const userToken = await tokenService.isSignedIn(req.body.userName, req.body.password);
	if (!userToken) {
		return res.status(404).json({
			errors: 'User not found'
		});
	}
	res.json({"token":userToken});
};

const getTokenInfo = async (req, res) => {
	const token = req.headers.authorization.split(' ')[1];
	const tokenInfo = await tokenService.getTokenInfo(token);
	if (!tokenInfo) {
		return res.status(404).json({
			errors: 'Token not found'
		});
	}
	res.json(tokenInfo);
};

module.exports = { isSignedIn, getTokenInfo };