const e = require('express');
const Movie = require('../models/movie');
const User = require('../models/user');
const Category = require('../models/category');
const MovieModel = require('../models/movie');
const net = require('net');

// Load environment variables
require('custom-env').env(process.env.NODE_ENV, './config');
const socket = net.connect(process.env.SERVER_PORT , process.env.SERVER_NAME, () => {
    console.log('connected to server');
});


// create a new movie
const createMovie = async (reqBody) => {
    const { title, year, category, director, duration, image, trailer } = reqBody;
    const movie = new Movie({
        title,
        year,
        category,
        director,
        duration,
        image,
        trailer
    });
    try {
		// check if the category exists
		if (category) {
			const categories = await Category.find({_id: {$in: category}});
			if (categories.length != category.length) {
				return null;
			}
		}
        //add the movie to the categories and save the movie
		const savedMovie = await movie.save();
		if (savedMovie.category) {
			await Category.updateMany({_id: {$in: savedMovie.category}}, {$push: {movies: savedMovie._id}});
		}
		return savedMovie;
	}
    catch {
        return null;
    }
};

// get movie by id
const getMovieById = async (id) => {
    try {
        return await Movie.findById(id);
    }
    catch {
        return null;
    }
};

// get movie by movieId
const getMovieByMovieId = async (movieId) => {
	try {
		return await Movie.findOne({movieId: movieId});
	}
	catch {
		return null;
	}
};

// get 20 movies from all promoted categories and check that the user has not seen them
const getMovies = async (userId) => {
	try {
		const user = await User.findById(userId);
		const seenMovies = user.seenMovies;
		const categories = await Category.find({promoted: true});
		let movies = [];
		for (let i = 0; i < categories.length; i++) {
			const category = categories[i];
			const categoryMovies = category.movies;
			for (let j = 0; j < categoryMovies.length; j++) {
				const movie = await Movie.findById(categoryMovies[j]);
				let counter = 0;
				if (!seenMovies.includes(movie._id)) {
					counter++;
					movies.push(movie);
					if (counter == 20) {
						break;
					}
				}
			}
		}
		// add the last 20 seen movies to the list 
		movies.push(...await Movie.find({_id: {$in: seenMovies}}).sort({createdAt: -1}).limit(20));
		return movies;
	}
	catch {
		return null;
	}
};

// get all movies from all categories
const getAllMovies = async () => {
	try {
		return await Movie.find();
	}
	catch {
		return null;
	}
};
		


// search for movies that contain the search query in their document
const searchQuery = async (query) => {
	try {
		// search if a field from type string contains the query
		// do it by finding which key is of type string based on the schema
		const keys = Object.keys(MovieModel.schema.obj);
		let stringKeys = [];
		// get all the keys that are of type string
		for (let i = 0; i < keys.length; i++) {
			if (MovieModel.schema.obj[keys[i]].type == String) {
				stringKeys.push(keys[i]);
			}
		}
		let movies = [];
		// search for the query in all the string keys
		for (let i = 0; i < stringKeys.length; i++) {
			const key = stringKeys[i];
			const movie = await Movie.find({ [key]: {$regex: query}});
			// check if title already exists in the movies array
			movie.forEach((m1) => {
				let exists = false;
				movies.forEach((m2) => {
					if (m1.title == m2.title) {
						exists = true;
					}
				});
				if (!exists) {
					movies.push(m1);
				}
			});
		}
		return movies;
	}
	catch {
		return null;
	}
};

// replace the movie with the given id to a new movie
const replaceMovie = async (id, newMovie) => {
    try {
		// check if the category exists
		if (newMovie.category.length > 0) {
			const categories = await Category.find({_id: {$in: newMovie.category}});
			if (categories.length != newMovie.category.length) {
				return null;
			}
		}
        const movie = await Movie.findById(id);
		const movieId = movie.movieId;
		// delete the movie from the categories
		await Category.updateMany({movies : id}, {$pull: {movies : id} });
		// loop over the keys of the new movie and update the movie
		Object.keys(MovieModel.schema.obj).forEach(key => {
			if (newMovie[key] != "" && newMovie[key] != undefined) {
				movie[key] = newMovie[key];
			} 
		});
		movie.movieId = movieId;
        const newMovieObject = await movie.save();
		// add the movie to the categories
		await Category.updateMany({_id: {$in: newMovieObject.category}}, {$push: {movies: newMovieObject._id}});
		return newMovieObject;
    }
    catch {
        return null;
    }
}

// delete movie by id
const deleteMovie = async (id) => {
    try {
		// delete movie from the database
        const movie = await Movie.findById(id);
        await movie.deleteOne();
		// delete movie from all users seen movies
		// seenMovies is array of objects 
		const UsersWhoSawTheMovie = await User.find({"seenMovies.movieId" : id});
		await User.updateMany({"seenMovies.movieId" : id}, { $pull: { seenMovies: { movieId: id } } } );
		// delete movie from the categories
		await Category.updateMany({movies : id}, {$pull: {movies : id} });
		// delete movie from the recommendations system for each user
		for (let i = 0; i < UsersWhoSawTheMovie.length; i++) {
			await deleteMovieFromRecommendations(movie.movieId, UsersWhoSawTheMovie[i].UserId);
		}
		return movie;
    }
    catch {
        return null;
    }
}

// Wait for the next event of the given type
const responseQueue = [];

// create a promise that resolves when the next event of the given type is received
socket.on('data', (data) => {
    if (responseQueue.length > 0) {
        const { resolve } = responseQueue.shift();
        resolve(data.toString());
    }
});

socket.on('error', (err) => {
    if (responseQueue.length > 0) {
        const { reject } = responseQueue.shift();
        reject(err);
    }
});

function sendRequestAndWait(command) {
    return new Promise((resolve, reject) => {
        responseQueue.push({ resolve, reject });
        socket.write(command);
    });
}

// delete movie from the recommendations system for specific user
const deleteMovieFromRecommendations = async (movieId, userId) => {
	try {
		let message = await sendRequestAndWait(`delete ${userId} ${movieId}\n`);
		return message;
	}
	catch {
		return null;
	}
}



const addMovieToRecommendations = async (movieId, userId) => {
    try {
        let message = await sendRequestAndWait(`post ${userId} ${movieId}\n`);
        // if user already exists in the recommendations system
        if (message.includes('404')) {
            message = await sendRequestAndWait(`patch ${userId} ${movieId}\n`);
        }
        return message;
    }
    catch {
        return null;
    }
}

const getRecommendations = async (userId, movieId) => {
    try {
        const user = await User.findById(userId);
        let id = user.UserId;
        let message = await sendRequestAndWait(`get ${id} ${movieId}\n`);
        if (message.includes('200')) {
            message = message.split("\n\n")[1];
            if (message == undefined) {
                return "";
            }
        }
        else {
            return undefined;
        }
        return message;
    }
    catch {
        return undefined;
    }
}

// export the functions
module.exports = {
    createMovie,
    getMovieById,
    getMovies,
    replaceMovie,
    deleteMovie,
	searchQuery,
    addMovieToRecommendations,
    getRecommendations,
	getMovieByMovieId,
	getAllMovies
};