const express = require('express');

var router = express.Router();
const movieController = require('../controllers/movie');
router.route('/all')
    .get(movieController.getAllMovies);
router.route('/')
    .get(movieController.getMovies)
    .post(movieController.createMovie);
router.route('/:id')
    .get(movieController.getMovie)
    .put(movieController.replaceMovie)
    .delete(movieController.deleteMovie);
router.route('/search/:query')
	.get(movieController.searchQuery);
router.route('/:id/recommend')
    .get(movieController.getRecommendations)
    .post(movieController.addMovieToRecommendations);

    
module.exports = router;