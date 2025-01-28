import React, { useEffect, useRef } from 'react';
import MovieCard from './movieCard';  // Import MovieCard component
import './category.css';  // Import Category styles

// Category component
const Category = ({ name, movies, onClickFunc }) => {

  // Create a reference to the moviesRow div
  const moviesRowRef = useRef(null);

  // UseEffect hook to show scrollbar if the category is 'Recommended movies' and hide it otherwise
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
      {/* Render MovieCard for each movie */}
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
