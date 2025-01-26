import React, { useEffect, useRef } from 'react';
import MovieCard from './movieCard';  // Import MovieCard component
import './category.css';  // Import Category styles

const Category = ({ name, movies, onClickFunc }) => {

  const moviesRowRef = useRef(null);

  useEffect(() => {
    if (moviesRowRef.current) {
      if (name === 'Recommended movies') {
        moviesRowRef.current.classList = 'movies-row scrollbar-show';
      } else {
        moviesRowRef.current.classList = 'movies-row scrollbar-hide';
      }
    }
  }, [name]);

  return (
    <div className="category-container">
      <h3 className='category-title'>
        {name}
    </h3>

      <div className="movies-row"
        ref={moviesRowRef}>
        {movies.map((movie) => (
          <MovieCard key={movie._id} movie={movie} onClickFuncMovie={() => onClickFunc(movie._id)}/>  // Render MovieCard for each movie
        ))}
      </div>
    </div>
  );
};

export default Category;
