import React from 'react';
import MovieCard from './movieCard';  // Import MovieCard component
import './category.css';  // Import Category styles

const Category = ({ name, movies }) => {
  return (
    <div className="category-container">
      <h3 className='category-title'>
        {name}
    </h3>

      <div className="movies-row" style={{ marginLeft: 20, display: 'flex', overflowX: 'auto'}}>
        {movies.map((movie) => (
          <MovieCard key={movie._id} movie={movie} />  // Render MovieCard for each movie
        ))}
      </div>
    </div>
  );
};

export default Category;
