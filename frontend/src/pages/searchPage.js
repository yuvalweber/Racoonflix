import React, { useState, useEffect } from "react";
import axios from "axios";
import Navbar from "../components/navbar"; // Navbar component to display top navigation
import "./searchPage.css"; // Import the CSS file
import { ThemeContext } from '../components/themeContext'; // Import ThemeContext
import { useContext } from 'react'; // Import useContext hook
import MovieCard from "../components/movieCard"; // Import the MovieCard component
import MovieInfoPage from "./movieInfoPage"; // Import the MovieInfoPage component
import { useAuth } from '../Authentication/AuthContext';

axios.defaults.baseURL = 'http://localhost:8080';

const SearchPage = () => {
    const [query, setQuery] = useState("");
    const { isDarkMode } = useContext(ThemeContext); // Access dark/light mode
    const { token, userData } = useAuth();
    const [isAdmin, setIsAdmin] = useState(false);
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [hasFetched, setHasFetched] = useState(false); // Track if API has fetched at least once
    const [selectedMovie, setSelectedMovie] = useState(null);  // State to store the selected movie

    const handleClickMovie = (movie) => {
        setSelectedMovie(movie._id);
      };
    
      const handleClosePopup = () => {
        setSelectedMovie(null);
      };
    
    useEffect(() => {
      if (token) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`; 
      }
      if (userData) {
        setIsAdmin(userData.isAdmin);
      }
    }, [token, userData]);

    useEffect(() => {
      const fetchResults = async () => {
        if (!query.trim()) {
          setResults([]);
          setHasFetched(false);
          return;
        }
  
        setLoading(true);
        setHasFetched(true); // Set this when a query begins
        try {
          const response = await axios.get(`/api/movies/search/${query}`);
          setResults(response.data || []);
        } catch (error) {
          console.error("Error fetching results:", error);
          setResults([]);
        } finally {
          setLoading(false);
        }
      };
  
      // Debounce API calls for better performance
      const debounceTimeout = setTimeout(() => {
        fetchResults();
      }, 300); // Adjust debounce timing as needed (300ms)
  
      return () => clearTimeout(debounceTimeout); // Cleanup timeout
    }, [query]);

    return (
      <div className={isDarkMode ? 'search-page-dark-mode' : 'search-page-light-mode'}>
		    <Navbar isAdmin={isAdmin} />
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
                <div className="loading">Loading...</div>
            ) : results.length > 0 ? (
                <div className="grid">
                {results.map((movie, index) => (
                    <MovieCard
                    key={index}
                    movie={movie}  // Passing the whole movie object to the MovieCard
                    onClickFuncMovie={() => handleClickMovie(movie)}  // Assuming you have a function for handling the click event
                    />
                ))}
                </div>
            ) : (
                hasFetched && query.trim() && (
                <p className="noResults">No results found for "{query}".</p>
                )
            )}
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

export default SearchPage;
