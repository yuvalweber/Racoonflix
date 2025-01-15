const userService = require('./user');

// check if the header x-user is provided and if the user exists
const checkUserHeader = async (userHeader) => {
    if (!userHeader) {
        return { status: 400, message: 'x-user header is required' };
    }
    const user = await userService.getUserById(userHeader);
    if (!user) {
        return { status: 404, message: 'User provided does not exist' };
    }
    return { status: 200, user };
};

module.exports = {
    checkUserHeader
};