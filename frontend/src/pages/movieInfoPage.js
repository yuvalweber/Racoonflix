import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import MovieDesc from '../components/movieDesc';
import ErrObj from '../components/errorObj';

axios.defaults.baseURL = 'http://localhost:8080';


const translateCategories = async (categories) => {
  try {
    const response = await axios.get(`/api/categories`);
    const categoriesFetched = response.data;
    const translatedCategories = categories.map((category) => {
      const categoryFound = categoriesFetched.find((element) => element._id === category);
      return categoryFound ? categoryFound.name : null;
    }).filter((name) => name); // Filter out null values
    return { type: "success", message: translatedCategories };
  } catch (error) {
    return { type: "error", message: error.message };
  }
};



const MovieInfoPage = ({movieId}) => {
  const { id } = useParams(); // Get movie ID from the URL
  const [movie, setMovie] = useState(null);
  const [error, setError] = useState(null);
  const [isFetchedWorked,setIsFetchedWorked] = useState(false);

  var correctMovieId;

  if (movieId) {
    correctMovieId = movieId;
  } else {
    correctMovieId = id;
  }

  useEffect(() => {
	const token = localStorage.getItem('token');
	if (token) {
	  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
	}
  }, []); // Set the Authorization header once on mount

  useEffect(() => {
    const fetchMovieData = async () => {
      try {
        const response = await axios.get(`/api/movies/${correctMovieId}`);
        const result = await translateCategories(response.data.category);
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
          setIsFetchedWorked(true);
        } else {
          setError(result.message);
          setIsFetchedWorked(false);
        }
      } catch (err) {
        setError(err);
        setIsFetchedWorked(false);
      }
    };
  
    fetchMovieData();
  }, [correctMovieId]);
  
  return (
    <div onClick={(e) => e.stopPropagation()}>
      {isFetchedWorked ? ( 
        <MovieDesc
          id = {movie.movieId}
          title={movie.title}
          year={movie.year}
          category={movie.category}
          director={movie.director}
          duration={movie.duration}
          image={movie.image}
          trailer={movie.trailer}
        />
      ) : (
        <ErrObj error={error} />
      )}
    </div>
  );
};

export default MovieInfoPage;
