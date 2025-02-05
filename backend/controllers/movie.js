const servicesFunctions = require('../services/servicesFunctions');
const MovieModel = require('../models/movie');
const movieService = require('../services/movie');
require('custom-env').env(process.env.NODE_ENV, './config');

// create a new movie
const createMovie = async (req, res) => {
    // check if provided all the required keys in the request body
	const requiredKeys = Object.keys(MovieModel.schema.obj).filter(key => MovieModel.schema.obj[key].required);

	// Add profile picture path if uploaded and add trailer path if uploaded
    let defaultImage = '';
    let defaultTrailer = '';
    if (typeof req.body.image === 'string' ) {
        defaultImage = req.body.image;
    }

    if (typeof req.body.trailer === 'string' ) {
        defaultTrailer = req.body.trailer;
    }

    req.body.image = req.files?.image ? `http://${process.env.API_SERVER}:${process.env.API_PORT}/${req.files.image[0].path}` : defaultImage;
    req.body.trailer = req.files?.trailer ? `http://${process.env.API_SERVER}:${process.env.API_PORT}/${req.files.trailer[0].path}` : defaultTrailer;
    // check if the request body contains all the required keys
	const missingKeys = requiredKeys.filter((key) => !(key in req.body));
    // return an informative error if the request body does not contain all the required keys
	if (missingKeys.length > 0) {
		return res.status(400).json({ errors: 'Missing required keys in the request body' });
	}
    // validate keys in the request body
    const invalidKeys = Object.keys(req.body).filter(key => !MovieModel.schema.obj.hasOwnProperty(key));
    if (invalidKeys.length > 0) {
        return res.status(400).json({ errors: 'Data provided contains invalid keys' });
    }
    // check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['authorization']);
	// print status and it's type
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }

    // create a new 
    const movie = await movieService.createMovie(req.body);
    // check if the movie was created
    if (!movie) {
        // return an error if the movie was not created and the reason
        return res.status(404).json({
            errors: 'Category not found or movie name not unique'
        });
    }
    // return the movie and the location of the movie
    res.status(201).location(`/api/movies/${movie._id}`).json();
};


// get all movies (according to hemi demands)
const getMovies = async (req, res) => {
    // check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['authorization']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }
    // get all movies
    const movies = await movieService.getMovies(headerValidation.message);
    if (!movies) {
        return res.status(404).json({
            errors: 'Movies not found'
        });
    }
    res.json(movies);
};

// get all movies (all existing movies)
const getAllMovies = async (req, res) => {
    // check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['authorization']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }
    // get all movies
    const movies = await movieService.getAllMovies();
    if (!movies) {
        return res.status(404).json({
            errors: 'There are no movies in the system'
        });
    }
    res.json(movies);
}


// get movie by id
const getMovie = async (req, res) => {
    // check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['authorization']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }
    const movie = await movieService.getMovieById(req.params.id);
    if (!movie) {
        return res.status(404).json({
            errors: 'Movie not found'
        });
    }
    res.json(movie);
};


// replace movie
const replaceMovie = async (req, res) => {
    // check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['authorization']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }

    req.body.image = req.files.image ? `http://${process.env.API_SERVER}:${process.env.API_PORT}/${req.files.image[0].path}` : '';
    req.body.trailer = req.files.trailer ? `http://${process.env.API_SERVER}:${process.env.API_PORT}/${req.files.trailer[0].path}` : '';
    // validate keys in the request body
    const invalidKeys = Object.keys(req.body).filter(key => !MovieModel.schema.obj.hasOwnProperty(key));
    if (invalidKeys.length > 0) {
        return res.status(400).json({ errors: 'Data provided contains invalid keys' });
    }
	// check if provided all the required keys in the request body
	const requiredKeys = Object.keys(MovieModel.schema.obj).filter(key => MovieModel.schema.obj[key].required);
	// check if the request body contains all the required keys
	const missingKeys = requiredKeys.filter(key => !req.body.hasOwnProperty(key));
	// return an informative error if the request body does not contain all the required keys
	if (missingKeys.length > 0) {
		return res.status(400).json({ errors: 'Missing required keys in the request body' });
	}
    // replace the movie
    const movie = await movieService.replaceMovie(req.params.id, req.body);
    if (!movie) {
        return res.status(404).json({
            errors: 'Movie not found or category not found'
        });
    }
    res.status(204).json();
};


// delete movie
const deleteMovie = async (req,res) => {
    // check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['authorization']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }
    const movie = await movieService.deleteMovie(req.params.id);
    if (!movie) {
        return res.status(404).json({
            errors: 'Movie not found'
        });
    }
    res.status(204).json();
};


//search movie by query
const searchQuery = async (req,res) => {
    // check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['authorization']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }
	const movie = await movieService.searchQuery(req.params.query);
	if (!movie) {
		return res.status(404).json({
			errors: 'Movie not found'
		});
	}
	res.json(movie);
};

// get recommendations
const getRecommendations = async (req, res) => {
    // check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['authorization']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }
    // get user id
    const userId = headerValidation.message;
    // check if there is an id in the params
    if (!req.params.id) {
        return res.status(400).json({
            errors: 'Movie id is required'
        });
    }
    // check if the movie exist
    const movie = await movieService.getMovieByMovieId(req.params.id);
    if (!movie) {
        return res.status(404).json({
            errors: 'Movie not found'
        });
    }
    // get recommendations
    const recommendations = await movieService.getRecommendations(userId, req.params.id);
    if (!recommendations) {
        return res.status(404).json({
            errors: 'Recommendations not found'
        });
    }
    res.json(recommendations);
};

// add movie to recommendations system
const addMovieToRecommendations = async (req, res) => {
    // check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['authorization']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }
    // check if there is an id in the params
    if (!req.params.id) {
        return res.status(400).json({
            errors: 'Movie id is required'
        });
    }
    // check if the movie exist
    const movie = await movieService.getMovieByMovieId(req.params.id);
    if (!movie) {
        return res.status(404).json({
            errors: 'Movie not found'
        });
    }
    // add movie to recommendations
    const message = await movieService.addMovieToRecommendations(req.params.id, headerValidation.message);
    if (!message) {
        return res.status(404).json({
            errors: 'Movie cant be added to recommendations'
        });
    }
    res.status(201).json();
};


module.exports = {
    createMovie,
    getMovies,
    getMovie,
    replaceMovie,
    deleteMovie,
	searchQuery,
    deleteMovie,
    getRecommendations,
    addMovieToRecommendations,
    getAllMovies
};