import React from 'react';
import Navbar from '../components/navbar'; // Assuming you have a Navbar component
import VideoPlayer from '../components/videoPlayer'; // Import the VideoPage component
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
      
      {/* Video Player Section */}
      <div className="video-box mt-5" style={{ maxWidth: '500px', width: '100%' }}>
        <VideoPlayer /> 
      </div>
    </div>
  );
};

export default ConnectedHomePage;
