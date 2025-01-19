import React, { useState, useEffect } from 'react';
import Navbar from '../components/navbar';  // Navbar component to display top navigation
import VideoPlayer from '../components/videoPlayer';  // The VideoPlayer component we discussed earlier
import Category from '../components/category';  // Category component to display movies grouped by categories
import axios from 'axios';  // Used to fetch data from an API

axios.defaults.baseURL = 'http://localhost:8080';

const ConnectedHomePage = () => {
  const [isAdmin, setIsAdmin] = useState(false);
  const [moviesByCategory, setMoviesByCategory] = useState({});  // State to store movies by category
  const [userId, setUserId] = useState('');

  useEffect(() => {
		const token = localStorage.getItem('token');
		if (token) {
		  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
		}
	  }, []); // Set the Authorization header once on mount

	  // Check if the user is an admin
	  useEffect(() => {
		const fetchUserData = async () => {
		  try {
			const response = await axios.get('/api/tokens');
      setUserId(response.data.id);
			if (response.data.isAdmin === true) {
			  setIsAdmin(true);
			}
		  } catch (err) {
			console.error('Error fetching user data:', err);
		  }
	  }; 
	  fetchUserData();
	}, []);

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const response = await axios.get('/api/movies');  // Fetch movies from the API
        if (userId) {
          const userResponse = await axios.get(`/api/users/${userId}`);
          const seenMovies = userResponse.data?.seenMovies || [];
          const seenMovieIds = new Set(seenMovies.map((userMovie) => userMovie.movieId));
          const tempLatestMovies = response.data
            .slice(-20) // Get the last 20 movies from the data
            .filter((movieObject) => seenMovieIds.has(movieObject._id)); // Only include seen movies
          var latestMovies = tempLatestMovies;
        }
        const translateCategories = async (categories) => {
          try {
            const response = await axios.get(`/api/categories`);
            const categoriesFetched = response.data;

            const translatedCategories = categories.map((category) => {
              const categoryFound = categoriesFetched.find((element) => element._id === category);
              return categoryFound ? categoryFound.name : null;
            }).filter((name) => name);  // Filter out null values
            return { type: "success", message: translatedCategories };  // Return object with type and message
          } catch (error) {
            return { type: "error", message: error.message };  // Return error message if request fails
          }
        };

        // Categorizing the movies by their category
        let categorizedMovies = {};
        const moviePromises = response.data.map(async (movie) => {
          const movieCategories = await translateCategories(movie.category);

          // Ensure movieCategories is an object with 'message' (an array of categories)
          if (movieCategories.type === "success") {
            movieCategories.message.forEach((category) => {
              const categoryName = category;
              if (!categorizedMovies[categoryName]) categorizedMovies[categoryName] = [];
              categorizedMovies[categoryName].push(movie);
            });
          }
        });

        // Wait for all category translations and movie additions to finish
        await Promise.all(moviePromises);
        // add the latest movies to the array and add the key 'latest'
        categorizedMovies['latest'] = latestMovies || [];
        setMoviesByCategory(categorizedMovies);  // Set the movies grouped by category
      } catch (error) {
        console.error('Error fetching movies:', error);  // Handle errors
      }
    };

    fetchMovies();  // Fetch movies on component mount
  }, [userId]);  // Empty dependency array, runs only once on mount

  return (
    <div id="mainContainer" className="bg-dark text-white vh-100">
      {/* Navbar component for navigation */}
      <Navbar isAdmin={isAdmin} />

      {/* Video Player Section */}
      <div className="d-flex flex-column align-items-center mt-5">
        <div className="video-box">
          <VideoPlayer />
        </div>
      </div>
        {/* Categories Section */}
      <div className="categories mt-5">
        {/* Display each category with movies */}
        {Object.entries(moviesByCategory).map(([categoryName, movies]) => (
          <Category key={categoryName} name={categoryName} movies={movies} />
        ))}
      </div>
      {/* <div className="categories mt-5"> */}
        {/* <Category key={'latest2132173821'} name={'Latest Movies'} movies={[latestMovies]} /> */}
      {/* </div>  */}
    </div>
  );
};

export default ConnectedHomePage;
