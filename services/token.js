const User = require('../models/user');	

//check if the user is signed in
const isSignedIn = async (userName, password) => {
	try{
		const user = await User.findOne({userName: userName});
		if (!user) return null;
		if (user.password !== password) return null;
		return user._id;
	}
	catch {
		return null;
	}
};

module.exports = { isSignedIn };