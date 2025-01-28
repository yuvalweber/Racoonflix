const userService = require('../services/user');
const UserModel = require('../models/user');

// Create a new user with profile picture upload
const createUser = async (req, res) => {
    try {
        const requiredKeys = Object.keys(UserModel.schema.obj).filter(
            (key) => UserModel.schema.obj[key].required
        );
        const missingKeys = requiredKeys.filter((key) => !(key in req.body));
        if (missingKeys.length > 0) {
            return res.status(400).json({ errors: 'Missing required keys in the request body' });
        }

        // Add profile picture path if uploaded
        if (req.file) {
            req.body.profilePicture = `http://localhost:8080/uploads/images/${req.file.filename}`;
        }

        // Validate keys in the request body
	    const invalidKeys = Object.keys(req.body).filter(key => !UserModel.schema.obj.hasOwnProperty(key));
	    if (invalidKeys.length > 0) {
		    return res.status(400).json({ errors: 'Data provided contains invalid keys' });
	    }


        const user = await userService.createUser(req.body);
        if (!user) {
            return res.status(404).json({ errors: 'Unable to create user.' });
        }
        res.status(201).location(`/api/users/${user._id}`).json(user);
    } catch (err) {
        console.log(err);
        res.status(500).json({ errors: 'Server error occurred.' });
    }
};

const getUser = async (req, res) => {
    const user = await userService.getUserById(req.params.id);
    if (!user) {
        return res.status(404).json({
            errors: 'User not found',
        });
    }
    res.json(user);
};

module.exports = {
    createUser,
    getUser,
};