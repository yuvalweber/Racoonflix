import React from 'react';
import MovieCard from './movieCard';  // Import MovieCard component

const Category = ({ name, movies }) => {
  return (
    <div className="category-container">
      <h3 style={{ color: 'red', marginLeft: 20, textShadow: '1px 1px 0px black, -1px -1px 0px black, 1px -1px 0px black, -1px 1px 0px black' }}>
        {name}
    </h3>

      <div className="movies-row" style={{ marginLeft: 20, display: 'flex', overflowX: 'auto', padding: '10px 0' }}>
        {movies.map((movie) => (
          <MovieCard key={movie._id} movie={movie} />  // Render MovieCard for each movie
        ))}
      </div>
    </div>
  );
};

export default Category;
