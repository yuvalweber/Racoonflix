import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import MovieDesc from '../components/movieDesc';
import ErrObj from '../components/errorObj';

axios.defaults.baseURL = `http://${process.env.REACT_APP_API_SERVER}:${process.env.REACT_APP_API_PORT}`; // Set the base URL for axios requests

// Function to translate category IDs to category names
const translateCategories = async (categories) => {
  try {
    const response = await axios.get(`/api/categories`); // Fetch all categories from the server
    const categoriesFetched = response.data;
    const translatedCategories = categories.map((category) => {
      const categoryFound = categoriesFetched.find((element) => element._id === category);
      return categoryFound ? categoryFound.name : null; // Return the category name if found, otherwise null
    }).filter((name) => name); // Filter out null values
    return { type: "success", message: translatedCategories };
  } catch (error) {
    return { type: "error", message: error.message }; // Return error message if the request fails
  }
};

const MovieInfoPage = ({ movieId }) => {
  const { id } = useParams(); // Get movie ID from the URL
  const [movie, setMovie] = useState(null); // State to store movie data
  const [error, setError] = useState(null); // State to store error message
  const [isFetchedWorked, setIsFetchedWorked] = useState(false); // State to track if data fetching was successful

  var correctMovieId;

  if (movieId) {
    correctMovieId = movieId; // Use the movieId prop if provided
  } else {
    correctMovieId = id; // Otherwise, use the ID from the URL
  }

  useEffect(() => {
    const token = localStorage.getItem('token'); // Get the token from local storage
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`; // Set the Authorization header
    }
  }, []); // Set the Authorization header once on mount

  useEffect(() => {
    const fetchMovieData = async () => {
      try {
        const response = await axios.get(`/api/movies/${correctMovieId}`); // Fetch movie data from the server
        const result = await translateCategories(response.data.category); // Translate category IDs to names
        if (result.type === "success") {
          setMovie({
            movieId: response.data.movieId,
            title: response.data.title,
            year: response.data.year,
            category: result.message,
            director: response.data.director,
            duration: response.data.duration,
            image: response.data.image,
            trailer: response.data.trailer,
          });
          setIsFetchedWorked(true); // Set the flag to true if data fetching was successful
        } else {
          setError(result.message); // Set the error message if category translation fails
          setIsFetchedWorked(false); // Set the flag to false if data fetching failed
        }
      } catch (err) {
        setError(err); // Set the error message if the request fails
        setIsFetchedWorked(false); // Set the flag to false if data fetching failed
      }
    };

    fetchMovieData(); // Fetch movie data when the component mounts or correctMovieId changes
  }, [correctMovieId]);

  return (
    <div onClick={(e) => e.stopPropagation()}>
      {isFetchedWorked ? ( 
        <MovieDesc
          id={movie.movieId}
          title={movie.title}
          year={movie.year}
          category={movie.category}
          director={movie.director}
          duration={movie.duration}
          image={movie.image}
          trailer={movie.trailer}
        />
      ) : (
        <ErrObj error={error} /> // Display error message if data fetching failed
      )}
    </div>
  );
};

export default MovieInfoPage;
