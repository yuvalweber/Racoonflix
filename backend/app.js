const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const path = require('path');
const mongoose = require('mongoose');
const category = require('./routes/category');
const user = require('./routes/user');
const movie = require('./routes/movie');
const token = require('./routes/token');
const schema = require('./routes/schema');
const Counter = require('./models/counter');

require('custom-env').env(process.env.NODE_ENV, './config');
mongoose.connect(process.env.CONNECTION_STRING, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
});

var app = express();
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json());
app.use('/uploads', express.static(path.join(__dirname, 'uploads'))); // Serve static files
app.use('/api/categories', category);
app.use('/api/users', user);
app.use('/api/tokens', token);
app.use('/api/movies', movie);
app.use('/api/schemas', schema);


// do it with findOneAndUpdate
const initializeCounters = async () => {
    try {
        // Ensure unique counter initialization
        const userCounter = await Counter.findOneAndUpdate(
            { fieldName: 'UserId' },
            { $setOnInsert: { seq: 0 } },
            { upsert: true, new: true }
        );

        const movieCounter = await Counter.findOneAndUpdate(
            { fieldName: 'MovieId' },
            { $setOnInsert: { seq: 99 } },
            { upsert: true, new: true }
        );

        console.log('Counters initialized:', { userCounter, movieCounter });
    } catch (error) {
        console.error('Error initializing counters:', error.message);
    }
};


const startServer = async () => {
    await initializeCounters();
    app.listen(process.env.PORT);
};

startServer();