import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './movieDesc.css';

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
          console.error('Unexpected response format:', response.data);
        }
      } catch (error) {
        console.error('Error fetching recommended movies:', error);
      }
    };

    fetchRecommendedMovies();
  }, [id]);

  return (
    <div className="movie-desc-container">
      <div className="movie-desc-card bg-dark bg-opacity-75 p-5 rounded d-flex align-items-center">
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
      <div className="recommended-movies mt-4">
        <h3 className="text-white">Recommended Movies</h3>
        <div className="recommended-movies-scroll d-flex overflow-auto">
          {recommendedMovies.map((movie) => (
            <div
              key={movie.id}
              className="recommended-movie-card m-2"
              onClick={() => navigate(`/movies/${movie.id}`)}
            >
              <img src={movie.image} alt={movie.title} className="recommended-movie-image" />
              <p className="text-center text-white mt-2">{movie.title}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default MovieDesc;
