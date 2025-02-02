import React, { useState, useEffect } from 'react';
import Navbar from '../components/navbar';  // Navbar component to display top navigation
import VideoPlayer from '../components/videoPlayer';  // The VideoPlayer component we discussed earlier
import Category from '../components/category';  // Category component to display movies grouped by categories
import axios from 'axios';  // Used to fetch data from an API
import { useAuth } from '../Authentication/AuthContext';
import { ThemeContext } from '../components/themeContext'; // Import ThemeContext
import { useContext } from 'react';  // Import useContext hook
import MovieInfoPage from './movieInfoPage';
import './connectedHomePage.css';  // Styles for the home page
import '../components/category.css';  // Styles for the category component

axios.defaults.baseURL = `http://${process.env.REACT_APP_API_SERVER}:${process.env.REACT_APP_API_PORT}`;

const ConnectedHomePage = () => {
  const { token , userData } = useAuth();
  const { isDarkMode } = useContext(ThemeContext); // Access dark/light mode
  const [isAdmin, setIsAdmin] = useState(false);
  const [moviesByCategory, setMoviesByCategory] = useState({});  // State to store movies by category
  const [userId, setUserId] = useState('');
  const [selectedMovie, setSelectedMovie] = useState(null);  // State to store the selected movie

  // Function to handle when a movie is clicked
  const handleClickMovie = (movieId) => {
    setSelectedMovie(movieId);
  };

  // Function to handle when the popup is closed
  const handleClosePopup = () => {
    setSelectedMovie(null);
  };

  // UseEffect to check if the user is an admin and set the Authorization header
  useEffect(() => {
		if (token) {
		  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
		}
		if (userData) {
			setIsAdmin(userData.isAdmin);
      setUserId(userData.id);
		}
	  }, [token, userData]); // Set the Authorization header once on mount

  // UseEffect to fetch movies and categories from the API
  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const response = await axios.get('/api/movies');  // Fetch movies from the API
        if (userId) {
          // Fetch the user's seen movies
          const userResponse = await axios.get(`/api/users/${userId}`);
          const seenMovies = userResponse.data?.seenMovies || [];
          const seenMovieIds = new Set(seenMovies.map((userMovie) => userMovie.movieId));
          const tempLatestMovies = response.data
            .slice(-20) // Get the last 20 movies from the data, these are the seen movies (possibly)
            .filter((movieObject) => seenMovieIds.has(movieObject._id)); // Only include seen movies and if not in array yet
         // remove duplicates using loop 
          var latestMovies = [];
          for (let i = 0; i < tempLatestMovies.length; i++) {
            let j;
            for (j = 0; j < latestMovies.length; j++) {
              if (tempLatestMovies[i]._id === latestMovies[j]._id) {
                break;
              }
            }
            if (j === latestMovies.length) {
              latestMovies.push(tempLatestMovies[i]);
            }
          }
        }
        // Function to translate the category IDs to category names
        const translateCategories = async (categories) => {
          try {
            const categoryRes = await axios.get(`/api/categories`);
            const categoriesFetched = categoryRes.data;

            const translatedCategories = categories.map((category) => {
              const categoryFound = categoriesFetched.find((element) => element._id === category);
              return categoryFound && categoryFound.promoted ? categoryFound.name : null;
            }).filter((name) => name);  // Filter out null values
            // remove all non promoted categories
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
              // add movie only if it is not in the list
              let i;
              for (i = 0; i < categorizedMovies[categoryName].length; i++) {
                if (movie._id === categorizedMovies[categoryName][i]._id) {
                  break;
                }
              }
              if (i === categorizedMovies[categoryName].length) {
                categorizedMovies[categoryName].push(movie);
              }
            });
          }
        });

        // Wait for all category translations and movie additions to finish
        await Promise.all(moviePromises);
        // add the latest movies to the array and add the key 'latest'
        categorizedMovies['seen movies'] = latestMovies || [];
        setMoviesByCategory(categorizedMovies);  // Set the movies grouped by category
      } catch (error) {
        console.error('Error fetching movies:', error);  // Handle errors
      }
    };

    fetchMovies();  // Fetch movies on component mount
  }, [userId]);  // Empty dependency array, runs only once on mount

  return (
   <div className={isDarkMode ? 'connected-home-page-dark-mode' : 'connected-home-page-light-mode'}>
    {/* Navbar component for navigation */}
		 <Navbar isAdmin={isAdmin} />
    {/* Background */}
    <div id="mainContainer" className="bg-dark text-white vh-100">
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
          <Category key={categoryName} name={categoryName} movies={movies} onClickFunc={handleClickMovie}/>
        ))}
      </div>
      {selectedMovie && (
        <div
          className={`popup-overlay ${selectedMovie ? 'show' : ''}`}
          onClick={handleClosePopup}
        >
            <MovieInfoPage movieId={selectedMovie} />
        </div>
      )}
    </div>
	</div>
  );
};

export default ConnectedHomePage;
