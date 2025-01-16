const express = require('express');

var router = express.Router();
const tokenController = require('../controllers/token');
router.route('/')
	.post(tokenController.isSignedIn);
router.route('/')
	.get(tokenController.getTokenInfo);
	
module.exports = router;