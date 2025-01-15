import React from 'react';
import Navbar from '../components/navbar'; // Import the Navbar component
import '../components/homePageBackground.css';  // Import background styles for the page

// Displays the home page for a connected user, according to their role (Admin or User).
const ConnectedHomePage = ({ isAdmin = true }) => {
  return (
    // Background Container
    <div 
      id="mainContainer"
      className="d-flex flex-column justify-content-center align-items-center text-center vh-100 bg-dark text-white"
    >
      {/* Navbar */}
      <Navbar isAdmin={isAdmin} />
    </div>
  );
};

export default ConnectedHomePage;
