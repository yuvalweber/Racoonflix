import React from 'react';
import './movieCard.css';  // Import MovieCard styles

const MovieCard = ({ movie }) => {
  return (
    <div className="movie-card" style={{ margin: '10px', textAlign: 'center', width: '150px' }}>
      <img 
        src={movie.image}  // Assuming the movie object has an 'image' field
        alt={movie.title}   // Alt text for accessibility
        style={{ width: '100%', height: 'auto', borderRadius: '8px' }}
      />
      <h5 style={{ color: 'white', marginTop: '10px' }}>{movie.title}</h5>
    </div>
  );
};

export default MovieCard;
