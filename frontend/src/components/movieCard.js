import React from 'react';
import './movieCard.css';  // Import MovieCard styles
import { useNavigate } from 'react-router-dom';

const MovieCard = ({ movie }) => {
  const navigate = useNavigate();
  const handleClick = () => {
	navigate(`/movieInfo/${movie._id}`);
  };
  return (
    <div className="movie-card" onClick={handleClick}>
      <img 
        src={movie.image}  // Assuming the movie object has an 'image' field
        alt={movie.title}   // Alt text for accessibility
        style={{ width: '100%', height: 'auto', borderRadius: '8px' }}
      />
      <h5 style={{ color: 'white', textShadow: '1px 1px 0px black, -1px -1px 0px black, 1px -1px 0px black, -1px 1px 0px black' }}>{movie.title}</h5>
    </div>
  );
};

export default MovieCard;
