import React from 'react';
import './movieCard.css';  // Import MovieCard styles

// MovieCard component definition
const MovieCard = ({ movie, onClickFuncMovie }) => {
  return (
    // Container div for the movie card with an onClick event handler
    <div className="movie-card" onClick={onClickFuncMovie}>
      {/* Movie image */}
      <img 
        src={movie.image}  // Assuming the movie object has an 'image' field
        alt={movie.title}   // Alt text for accessibility
        style={{ width: '100%', height: 'auto', borderRadius: '8px' }}  // Inline styles for the image
      />
      {/* Movie title */}
      <h5 className='movie-card h5'>{movie.title}</h5>
    </div>
  );
};

export default MovieCard;  // Export the MovieCard component as the default export
