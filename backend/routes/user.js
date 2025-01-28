const express = require('express');
const upload = require('../middleWare/uploadFile');

var router = express.Router();
const userController = require('../controllers/user');
router.route('/')
	.post(upload.single('profilePicture'),userController.createUser);

router.route('/:id')
	.get(userController.getUser);

module.exports = router;