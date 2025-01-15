const categoryService = require('../services/category');
const categoryModel = require('../models/category');
const servicesFunctions = require('../services/servicesFunctions');

// create a new category
const createCategory = async (req, res) => {
	// if title not provided or promoted is not boolean or movie is not array of object ids
	if (!req.body.name || (req.body.promoted && typeof req.body.promoted !== 'boolean') || 
		(req.body.movies && !Array.isArray(req.body.movies))) {
		return res.status(400).json({
			errors: 'data provided is not valid'
		});
	}
    // Validate keys in the request body
    const invalidKeys = Object.keys(req.body).filter(
		key => !categoryModel.schema.obj.hasOwnProperty(key)
	);
	if (invalidKeys.length > 0) {
		return res.status(400).json({ errors: 'Data provided contains invalid keys' });
	}
	// check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['x-user']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }
	// create a new category
    const category = await categoryService.createCategory(req.body);
	if (!category) {
		 return res.status(404).json({
			errors: 'one of the movies does not exist or title is not unique'
		});
	}
	res.status(201).location(`/api/categories/${category._id}`).json();
};
// get all categories
const getCategories = async (req, res) => {
	// check if user provide x-user header and if the user exists
	const headerValidation = await servicesFunctions.checkUserHeader(req.headers['x-user']);
	if (headerValidation.status !== 200) {
		return res.status(headerValidation.status).json({ errors: headerValidation.message });
	}
    const categories = await categoryService.getCategories();
	if (!categories) {
		return res.status(404).json({
			errors: ['Categories not found']
		});
	}
	res.json(categories);
};
// get category by id
const getCategory = async (req, res) => {
	// check if user provide x-user header and if the user exists
	const headerValidation = await servicesFunctions.checkUserHeader(req.headers['x-user']);
	if (headerValidation.status !== 200) {
		return res.status(headerValidation.status).json({ errors: headerValidation.message });
	}
    const category = await categoryService.getCategoryById(req.params.id);
    if (!category) {
        return res.status(404).json({
            errors: 'Category not found'
        });
    }
    res.json(category);
};
// update category info
const updateCategory = async (req, res) => {
	// check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['x-user']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }

    // Validate keys in the request body
    const invalidKeys = Object.keys(req.body).filter(
		key => !categoryModel.schema.obj.hasOwnProperty(key)
	);
	if (invalidKeys.length > 0) {
		return res.status(400).json({ errors: 'Data provided contains invalid keys' });
	}

	const category = await categoryService.updateCategory(req.params.id, req.body);
	if (!category) {
		return res.status(404).json({
			errors: 'Category not found'
		});
	}
	res.status(204).json();
};

// delete category
const deleteCategory = async (req,res) => {
	// check if user provide x-user header and if the user exists
    const headerValidation = await servicesFunctions.checkUserHeader(req.headers['x-user']);
    if (headerValidation.status !== 200) {
        return res.status(headerValidation.status).json({ errors: headerValidation.message });
    }
	const category = await categoryService.deleteCategory(req.params.id);
	if (!category) {
		return res.status(404).json({
			errors: 'Category not found'
		});
	}
	res.status(204).json();
}
module.exports = {
	createCategory,
	getCategories,
	getCategory,
	updateCategory,
	deleteCategory
};