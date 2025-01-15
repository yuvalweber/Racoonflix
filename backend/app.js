const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const mongoose = require('mongoose');
const category = require('./routes/category');
const user = require('./routes/user');
const movie = require('./routes/movie');
const token = require('./routes/token');
const schema = require('./routes/schema');
const Counter = require('./models/counter');

require('custom-env').env(process.env.NODE_ENV, './config');
mongoose.connect(process.env.CONNECTION_STRING,
{ useNewUrlParser: true,
useUnifiedTopology: true });

var app = express();
app.use(cors());
app.use(bodyParser.urlencoded({extended : true}));
app.use(express.json());
app.use('/api/categories', category);
app.use('/api/users', user);
app.use('/api/tokens', token);
app.use('/api/movies', movie);
app.use('/api/schemas', schema);

const initializeCounters = async () => {
    // creating the counters for userId and movieId
    const userCounter = new Counter({fieldName: 'UserId', seq: 0});
    const movieCounter = new Counter({fieldName: 'MovieId', seq: 99});
    await userCounter.save();
    await movieCounter.save();
};


const startServer = async () => {
    await initializeCounters(); // Initialize counters before starting the server
    app.listen(process.env.PORT);
};

startServer();