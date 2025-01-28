import React, { useState, useEffect } from "react";
import axios from "axios";
import Navbar from "../components/navbar"; // Navbar component to display top navigation
import "./searchPage.css"; // Import the CSS file
import { ThemeContext } from '../components/themeContext'; // Import ThemeContext
import { useContext } from 'react'; // Import useContext hook
import MovieCard from "../components/movieCard"; // Import the MovieCard component
import MovieInfoPage from "./movieInfoPage"; // Import the MovieInfoPage component
import { useAuth } from '../Authentication/AuthContext'; // Import useAuth hook for authentication

axios.defaults.baseURL = 'http://localhost:8080'; // Set default base URL for axios

const SearchPage = () => {
  const [query, setQuery] = useState(""); // State to store the search query
  const { isDarkMode } = useContext(ThemeContext); // Access dark/light mode from ThemeContext
  const { token, userData } = useAuth(); // Get token and userData from AuthContext
  const [isAdmin, setIsAdmin] = useState(false); // State to check if user is admin
  const [results, setResults] = useState([]); // State to store search results
  const [loading, setLoading] = useState(false); // State to manage loading state
  const [hasFetched, setHasFetched] = useState(false); // Track if API has fetched at least once
  const [selectedMovie, setSelectedMovie] = useState(null); // State to store the selected movie

  // Handle click event on a movie
  const handleClickMovie = (movie) => {
    setSelectedMovie(movie._id); // Set the selected movie ID
  };
  
  // Handle closing of the movie info popup
  const handleClosePopup = () => {
    setSelectedMovie(null); // Reset the selected movie
  };
  
  // Effect to set authorization header and check if user is admin
  useEffect(() => {
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`; // Set authorization header
    }
    if (userData) {
      setIsAdmin(userData.isAdmin); // Set isAdmin state based on userData
    }
  }, [token, userData]);

  // Effect to fetch search results based on query
  useEffect(() => {
    const fetchResults = async () => {
      if (!query.trim()) {
        setResults([]); // Clear results if query is empty
        setHasFetched(false); // Reset hasFetched state
        return;
      }
  
      setLoading(true); // Set loading state to true
      setHasFetched(true); // Set hasFetched state to true
      try {
        const response = await axios.get(`/api/movies/search/${query}`); // Fetch search results
        setResults(response.data || []); // Set results state with fetched data
      } catch (error) {
        console.error("Error fetching results:", error); // Log error
        setResults([]); // Clear results on error
      } finally {
        setLoading(false); // Set loading state to false
      }
    };
  
    // Debounce API calls for better performance
    const debounceTimeout = setTimeout(() => {
      fetchResults(); // Fetch results after debounce timeout
    }, 300); // Adjust debounce timing as needed (300ms)
  
    return () => clearTimeout(debounceTimeout); // Cleanup timeout
  }, [query]);

  return (
    <div className={isDarkMode ? 'search-page-dark-mode' : 'search-page-light-mode'}>
      <Navbar isAdmin={isAdmin} /> {/* Navbar component with isAdmin prop */}
      <div id="mainContainer" className="bg-dark text-white vh-100">
        {/* Transparent Box */}
        <div className="transparent-box"></div>
        <div className="header">
          <h1 className="title">Find Your Next Movie</h1>
          <p className="subtitle">Search by title, director, image and more</p>
        </div>
        {/* Search box */}
        <div className="searchBarContainer">
          <input
            type="text"
            placeholder="Search for a movie or series..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="searchInput"
          />
        </div>

        {/* Results Section */}
        <div className="resultsContainer">
          {loading ? (
            <div className="loading">Loading...</div> // Show loading indicator
          ) : results.length > 0 ? (
            <div className="grid">
              {results.map((movie, index) => (
                <MovieCard
                  key={index}
                  movie={movie} // Passing the whole movie object to the MovieCard
                  onClickFuncMovie={() => handleClickMovie(movie)} // Handle click event on movie
                />
              ))}
            </div>
          ) : (
            hasFetched && query.trim() && (
              <p className="noResults">No results found for "{query}".</p> // Show no results message
            )
          )}
        </div>
        {selectedMovie && (
          <div
            className={`popup-overlay ${selectedMovie ? 'show' : ''}`}
            onClick={handleClosePopup} // Handle closing of the popup
          >
            <MovieInfoPage movieId={selectedMovie} /> {/* Show MovieInfoPage for selected movie */}
          </div>
        )}
      </div>
    </div>
  );
};

export default SearchPage;
