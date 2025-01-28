import React from 'react';
import "./homePageBackground.css";
import { Link } from 'react-router-dom';

// Home page background component
const HomeBackground = () => {
  return (
    <div 
      id="mainContainer"
      className="d-flex flex-column justify-content-center align-items-center text-center vh-100 bg-dark text-white" 
    >
      <div className="bg-dark bg-opacity-75 p-4 rounded text-center">
        <h1 className="display-4 fw-bold text-white">Welcome to Racconflix</h1>
        <p className="lead">Wanna be careless like the raccons? Join us!</p>
        <div className="d-flex justify-content-center gap-3">
          <Link to="/signup" className="btn btn-danger btn-lg">Sign Up</Link>
          <Link to="/login" className="btn btn-primary btn-lg">Log In</Link>
        </div>
      </div>
    </div>
  );
};

export default HomeBackground;
