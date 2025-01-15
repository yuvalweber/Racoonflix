import React from 'react';
import "./homePageBackground.css";
import { Link } from 'react-router-dom';

const HomeBackground = () => {
  return (
    <div 
      id="mainContainer"
      className="d-flex flex-column justify-content-center align-items-center text-center vh-100 bg-dark text-white" 
    >
      <div className="bg-dark bg-opacity-75 p-4 rounded">
        <h1 className="display-4 fw-bold">Welcome to Racconflix</h1>
        <p className="lead">Wanna be careless like the raccons? join us!</p>
        <div className="d-flex gap-3">
		  <Link to="/signup" className="btn btn-danger btn-lg">Sign Up</Link>
          <Link to="/login" className="btn btn-primary btn-lg">Log In</Link>
        </div>
      </div>
    </div>
  );
};

export default HomeBackground;
