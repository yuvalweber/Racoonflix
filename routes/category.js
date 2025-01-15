const express = require('express');

var router = express.Router();
const categoryController = require('../controllers/category');
router.route('/')
    .get(categoryController.getCategories)
	.post(categoryController.createCategory);
router.route('/:id')
	.get(categoryController.getCategory)
	.patch(categoryController.updateCategory)
	.delete(categoryController.deleteCategory);
	
module.exports = router;