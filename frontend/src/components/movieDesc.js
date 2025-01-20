import React from 'react';
import { useNavigate } from 'react-router-dom';
import './movieDesc.css';

const MovieDesc = ({
  title,
  year,
  category,
  director,
  duration,
  image,
  trailer
}) => {
	const navigate = useNavigate();
	const handleWatchTrailer = (trailer) => {
		// Navigate to the watch page with the trailer link
		navigate('/play', { state: { trailer } });
	}
  return (
    <div className="movie-desc-card bg-dark bg-opacity-75 p-5 rounded d-flex align-items-center">
        {/* Movie Poster */}
        <div className="movie-poster-container">
          <img src={image} alt={`${title} Poster`} className="movie-poster" />
        </div>

        {/* Movie Info */}
        <div className="movie-info ml-4" style={{ color: 'white' , paddingLeft: '5%'}}>
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
    );
};

export default MovieDesc;
