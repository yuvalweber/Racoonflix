import React from 'react';
import MovieCard from './movieCard';  // Import MovieCard component
import './category.css';  // Import Category styles

const Category = ({ name, movies, onClickFunc }) => {
  return (
    <div className="category-container">
      <h3 className='category-title'>
        {name}
    </h3>

      <div className="movies-row" style={{ marginLeft: 20, display: 'flex', overflowX: 'auto', scrollbarWidth: 'none' }}>
        {movies.map((movie) => (
          <MovieCard key={movie._id} movie={movie} onClickFuncMovie={() => onClickFunc(movie._id)}/>  // Render MovieCard for each movie
        ))}
      </div>
    </div>
  );
};

export default Category;
