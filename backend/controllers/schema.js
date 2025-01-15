const { getSchemaFields } = require('../services/schema');

// the function to get the schema of a model
const getSchema = (req, res) => {
	const model = req.params.name; 
	// get the fields of the model
	const fields = getSchemaFields(model);
	if (!fields) {
		return res.status(404).json({ error: 'schema not exist' });
	}
	res.json({ fields }); 
};

module.exports = {
  getSchema,
};
