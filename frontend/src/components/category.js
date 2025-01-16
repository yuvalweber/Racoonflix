import React from 'react';
import MovieCard from './movieCard';  // Import MovieCard component

const Category = ({ name, movies }) => {
  return (
    <div className="category-container" style={{ marginBottom: '30px' }}>
      <h3 style={{ color: 'white' }}>{name}</h3>
      <div className="movies-row" style={{ display: 'flex', overflowX: 'auto', padding: '10px 0' }}>
        {movies.map((movie) => (
          <MovieCard key={movie._id} movie={movie} />  // Render MovieCard for each movie
        ))}
      </div>
    </div>
  );
};

export default Category;
