import React from 'react';
import { useNavigate } from 'react-router-dom'; // Importing useNavigate hook for navigation
import '../components/watchNavbar.css'; // Importing custom styles

const WatchNavbar = () => {
  const navigate = useNavigate(); // Initializing navigate function

  return (
    <div className="watch-navbar">
      {/* Back arrow button */}
      <button className="back-button" onClick={() => navigate(-1)}>
        ← 
      </button>
    </div>
  );
};

export default WatchNavbar; // Exporting WatchNavbar component
