const express = require('express');
const upload = require('../middleWare/uploadFile');

var router = express.Router();
const movieController = require('../controllers/movie');
router.route('/all')
    .get(movieController.getAllMovies);
router.route('/')
    .get(movieController.getMovies)
    .post(upload.fields([
        { name: 'image', maxCount: 1 },
        { name: 'trailer', maxCount: 1 }
    ]),movieController.createMovie);
router.route('/:id')
    .get(movieController.getMovie)
    .put(upload.fields([
        { name: 'image', maxCount: 1 },
        { name: 'trailer', maxCount: 1 }
    ]),movieController.replaceMovie)
    .delete(movieController.deleteMovie);
router.route('/search/:query')
	.get(movieController.searchQuery);
router.route('/:id/recommend')
    .get(movieController.getRecommendations)
    .post(movieController.addMovieToRecommendations);

    
module.exports = router;