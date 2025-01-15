const express = require('express');
const shcemaController = require('../controllers/schema');
var router = express.Router();

router.route('/:name')
	.get(shcemaController.getSchema);

module.exports = router;

