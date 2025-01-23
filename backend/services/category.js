const Category = require('../models/category');
const Movie = require('../models/movie');

// create a new category
const createCategory = async (requestData) => {
	// name is required
    const category = new Category({
        "name" : requestData.name,
    });
    if (requestData.promoted) category.promoted = requestData.promoted;
	try{
		// check if the movies exist
		if (requestData.movies) {
			const moviesExist = await Movie.find({_id: {$in: requestData.movies}});
			if (moviesExist.length != requestData.movies.length) {
				return null;
			}
			// add the movies to the category
			category.movies = requestData.movies;
		}
		// save the category
    	const saveCategory = await category.save();
		// add category to the movies
		if (requestData.movies) {
			await Movie.updateMany({_id: {$in: requestData.movies}}, {$push: {category: saveCategory._id}});
		}
		return saveCategory;
	}
	catch {
		return null;
	}
};

// get category by id
const getCategoryById = async (id) => {
	try {
    	return await Category.findById(id);
	}
	catch {
		return null;
	}
};

// get all categories
const getCategories = async () => {
	try {
		return await Category.find();
	}
	catch {
		return null;
	}
};

//update category
const updateCategory = async (id, updateData) => {

	try {
		const category = await getCategoryById(id);
		if (!category) return null;
		Object.keys(updateData).forEach(key => {
			if (key != 'movies') {
				if (key == 'promoted') {
					category[key] = updateData[key];
				}
				else
				if (updateData[key] != "" && updateData[key] != undefined) {
					category[key] = updateData[key];
				}
			}
		});
		// check if the movies exist
		if (updateData.movies.length > 0) {
			const moviesExist = await Movie.find({_id: {$in: updateData.movies}});
			if (moviesExist.length != updateData.movies.length) {
				return null;
			}
			// remove the category from the movies
			await Movie.updateMany({category: id}, {$pull: {category: id}});
			// add the category to the movies
			await Movie.updateMany({_id: {$in: updateData.movies}}, {$push: {category: id}});
			
			category.movies = updateData.movies;
		}
		return await category.save();
	}
	catch {
		return null;
	}
};

// delete category
const deleteCategory = async (id) => {
    const category = await getCategoryById(id);
    if (!category) return null;
	try {
		// delete the category
		await category.deleteOne();
		// delete the category from the movies
		await Movie.updateMany({category: id}, {$pull: {category: id}});

		return category;
	}
	catch {
		return null;
	}
};
module.exports = {
    createCategory,
	getCategories,
	getCategoryById,
	deleteCategory,
	updateCategory
};