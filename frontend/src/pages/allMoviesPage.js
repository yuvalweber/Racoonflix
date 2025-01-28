import React, { useState, useEffect } from 'react';
import Navbar from '../components/navbar'; // Navbar component to display top navigation
import Category from '../components/category'; // Category component to display movies grouped by categories
import axios from 'axios'; // Used to fetch data from an API
import { ThemeContext } from '../components/themeContext'; // Import ThemeContext
import { useContext } from 'react'; // Import useContext hook
import { useAuth } from '../Authentication/AuthContext';
import MovieInfoPage from './movieInfoPage';
import '../components/category.css'; // CSS for the category component

// Set the base URL for the API
axios.defaults.baseURL = 'http://localhost:8080';

// AllMoviesPage component to display all movies grouped by categories
const AllMoviesPage = () => {
  const { token, userData } = useAuth();
  const { isDarkMode } = useContext(ThemeContext); // Access dark/light mode
  const [isAdmin, setIsAdmin] = useState(false);
  const [moviesByCategory, setMoviesByCategory] = useState({});
  const [selectedMovie, setSelectedMovie] = useState(null);

  // Function to handle click on a movie
  const handleClickMovie = (movieId) => {
    setSelectedMovie(movieId);
  };

  // Function to handle close popup
  const handleClosePopup = () => {
    setSelectedMovie(null);
  };

  // Set the Authorization header with the token and check if the user is an admin
  useEffect(() => {
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    if (userData) {
      setIsAdmin(userData.isAdmin);
    }
  }, [token, userData]);

  // Fetch all movies and categories from the API
  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const response = await axios.get('/api/movies/all'); // Fetch all movies from the API
        const categoriesResponse = await axios.get('/api/categories'); // Fetch all categories

        const categorizedMovies = {};
        // Group movies by category
        response.data.forEach((movie) => {
          movie.category.forEach((categoryId) => {
            const category = categoriesResponse.data.find(
              (cat) => cat._id === categoryId
            );
            if (category) {
              const categoryName = category.name;
              if (!categorizedMovies[categoryName]) categorizedMovies[categoryName] = [];
              categorizedMovies[categoryName].push(movie);
            }
          });
        });
        // Set movies grouped by category
        setMoviesByCategory(categorizedMovies); // Set movies grouped by category
      } catch (error) {
        console.error('Error fetching movies:', error);
      }
    };

    fetchMovies(); // Fetch movies on component mount
  }, []);

  return (
    // Display all movies grouped by categories
  <div className={isDarkMode ? 'connected-home-page-dark-mode' : 'connected-home-page-light-mode'}>
    {/* Navbar component for navigation */}
		 <Navbar isAdmin={isAdmin} />
    {/* Background */}
    <div id="mainContainer" className="bg-dark text-white vh-100">
      {/* Categories Section */}
      <div className="categories mt-5" style={{paddingTop: '200px'}}>
        {/* Display each category with movies */}
        {Object.entries(moviesByCategory).map(([categoryName, movies]) => (
          <Category key={categoryName} name={categoryName} movies={movies} onClickFunc={handleClickMovie} />
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

export default AllMoviesPage;
