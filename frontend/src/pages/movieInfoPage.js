import React from 'react';
import { useParams } from 'react-router-dom';
import '../components/homePageBackground.css';
import MovieDesc from '../components/movieDesc';
import Icon from '../components/icon';

// FixMe: need to add ability to fetch data from the server

const MovieInfoPage = () => {
  const { id } = useParams(); // Get movie ID from the URL
  return (
    <div id="mainContainer" className="d-flex flex-column justify-content-center align-items-center vh-100">
      <Icon />
      {/* <MovieDesc 
        title={sampleMovie.title}
        year={sampleMovie.year}
        category={sampleMovie.category}
        director={sampleMovie.director}
        duration={sampleMovie.duration}
        image={sampleMovie.image}
        trailer={sampleMovie.trailer}
      /> */}
    </div>
  );
};

export default MovieInfoPage;
