const userModel = require('../models/user');
const movieModel = require('../models/movie');
const categoryModel = require('../models/category');

// map of models
const models = {
  "user": userModel,
  "movie": movieModel,
  "category": categoryModel,
};

// get the schema fields of a model
const getSchemaFields = (modelName) => {
  const model = models[modelName];
  if (!model) {
    return null;
  }
  return Object.keys(model.schema.obj); 
};

module.exports = {
  getSchemaFields,
};
