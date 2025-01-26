import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './movieDesc.css';
import Category from './category';

const MovieDesc = ({
  id, // Add the current movie ID as a prop
  title,
  year,
  category,
  director,
  duration,
  image,
  trailer
}) => {
  const navigate = useNavigate();
  const [recommendedMovies, setRecommendedMovies] = useState([]);

  const handleClickMovie = (movieId) => {
    // Navigate to the movie description page of the selected movie
      console.log(movieId);
    };

  const handleWatchTrailer = (trailer) => {
    // Navigate to the watch page with the trailer link
    navigate('/play', { state: { trailer } });
  };

  useEffect(() => {
    // Fetch recommended movies when the component mounts or the movie ID changes
    const fetchRecommendedMovies = async () => {
      try {
        const response = await axios.get(`/api/movies/${id}/recommend`);
        if (Array.isArray(response.data)) {
          setRecommendedMovies(response.data);
        } else {
          setRecommendedMovies([]);
          console.error('Unexpected response format:', response.data);
        }
      } catch (error) {
        setRecommendedMovies([]);
        console.error('Error fetching recommended movies:', error);
      }
    };

    fetchRecommendedMovies();
  }, [id]);

  return (
    <div className="movie-desc-container">
      {/* Movie Description and Recommendations in the same card */}
      <div className="movie-desc-card bg-dark bg-opacity-75 p-5 rounded">
        <div className="movie-details d-flex align-items-center">
          {/* Movie Poster */}
          <div className="movie-poster-container">
            <img src={image} alt={`${title} Poster`} className="movie-poster" />
          </div>
  
          {/* Movie Info */}
          <div className="movie-info ml-4" style={{ color: 'white', paddingLeft: '5%' }}>
            <h2 className="movie-title">{title} ({year})</h2>
            <p className="director"><strong>Director:</strong> {director}</p>
            <p><strong>Duration:</strong> {duration} minutes</p>
  
            {/* Categories */}
            {category && category.length > 0 && (
              <p><strong>Categories:</strong> {category.join(', ')}</p>
            )}
  
            {/* Trailer Link */}
            {trailer && (
              <div>
                <button className="btn btn-secondary trailer-btn" onClick={() => handleWatchTrailer(trailer)}>Watch Trailer</button>
              </div>
            )}
          </div>
        </div>
  
        {/* Recommended Movies Section */}
        <div className="recommended-section mt-4">
          {recommendedMovies.length > 0 ? (
            <Category
              key="Recommended movies"
              name="Recommended movies"
              movies={recommendedMovies}
              onClickFunc={handleClickMovie}
            />
          ) : (
            <Category
              key="No recommended movies"
              name="There are no recommended movies to show"
              movies={[]}
              onClickFunc={handleClickMovie}
            />
          )}
        </div>
      </div>
    </div>
  );
  
  
};

export default MovieDesc;
