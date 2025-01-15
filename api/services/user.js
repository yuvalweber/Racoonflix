const User = require('../models/user');
const MovieService = require('./movie');
const Movie = require('../models/movie');

// Define the User service
//create a new user
const createUser = async (data) => {
	try {
		const user = new User(data);
		// check if the movies exist
		if (data.seenMovies) {
			const movieIds = data.seenMovies.map(movie => movie.movieId);
			const moviesExist = await Movie.find({_id: {$in: movieIds}});
			if (moviesExist.length != data.seenMovies.length) {
				return null;
			}
		}
		
		let userInfo = await user.save();
		// add the movies to the user in the recommendations
		if (data.seenMovies) {
			const movieIds = data.seenMovies.map(movie => movie.movieId);
			const moviesExist = await Movie.find({_id: {$in: movieIds}});
			for (let i = 0; i < moviesExist.length; i++) {
				await MovieService.addMovieToRecommendations(moviesExist[i].movieId, userInfo.UserId);
			}
		}
		return userInfo;
	} 
	catch {
		return null;
	}
};

//get user by id
const getUserById = async (id) => {
	try {
		const user = await User.findById(id);
		// change passsword to be masked
		user.password = '********';
		return user;
	}
	catch {
		return null;
	}
};

module.exports = {
	createUser,
	getUserById
};


