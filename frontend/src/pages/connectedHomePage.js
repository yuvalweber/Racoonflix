import React, { useEffect, useState } from 'react';
import Navbar from '../components/navbar'; // Assuming you have a Navbar component
import VideoPlayer from '../components/videoPlayer'; // Import the VideoPage component
import '../components/homePageBackground.css';  // Import background styles for the page
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8080';

// Displays the home page for a connected user, according to their role (Admin or User).
const ConnectedHomePage = () => {
	  const [isAdmin, setIsAdmin] = useState(false);

	  useEffect(() => {
		const token = localStorage.getItem('token');
		if (token) {
		  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
		}
	  }, []); // Set the Authorization header once on mount

	  // Check if the user is an admin
	  useEffect(() => {
		const fetchUserData = async () => {
		  try {
			const response = await axios.get('/api/tokens');
			if (response.data.isAdmin === true) {
			  setIsAdmin(true);
			}
		  } catch (err) {
			console.error('Error fetching user data:', err);
		  }
	  }; 
	  fetchUserData();
	}, []);

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
