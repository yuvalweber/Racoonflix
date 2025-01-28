import React, {useEffect} from 'react';
import { useNavigate, useLocation} from 'react-router-dom';	
import WatchNavbar from '../components/watchNavbar.js'; // Import the WatchNavbar component
import WatchPlayer from '../components/watchPlayer'; // Import the WatchPlayer component
import '../components/watchPage.css'; // Import the CSS styles for the watch page

const WatchPage = () => {
  const location = useLocation(); // Hook to get the current location
  const navigate = useNavigate(); // Hook to navigate programmatically

  useEffect(() => {
    // Set overflow, height, margin, and padding to hidden/100%/0 when the component is loaded
    document.documentElement.style.overflow = "hidden"; // For <html>
    document.body.style.overflow = "hidden"; // For <body>
    document.documentElement.style.height = "100%"; // For <html>
    document.body.style.height = "100%"; // For <body>
    document.documentElement.style.margin = "0"; // For <html>
    document.body.style.margin = "0"; // For <body>
    document.documentElement.style.padding = "0"; // For <html>
    document.body.style.padding = "0"; // For <body>

    return () => {
      // Clean up: Reset overflow, height, margin, and padding when leaving the page
      document.documentElement.style.overflow = ""; // For <html>
      document.body.style.overflow = ""; // For <body>
      document.documentElement.style.height = ""; // For <html>
      document.body.style.height = ""; // For <body>
      document.documentElement.style.margin = ""; // For <html>
      document.body.style.margin = ""; // For <body>
      document.documentElement.style.padding = ""; // For <html>
      document.body.style.padding = ""; // For <body>
    };
  }, []);

  // Get the videoUrl from the location state
  const videoUrl = location.state?.trailer;

  // If there is no videoUrl, navigate back to the home page
  if (!videoUrl) {
    navigate('/');
    return null;
  }

  return (
    <div className="watch-page">
      {/* Render the WatchNavbar component */}
      <WatchNavbar />

      {/* Render the WatchPlayer component with the videoUrl */}
      <div className="video-section">
        <WatchPlayer videoUrl={videoUrl} />
      </div>
    </div>
  );
};

export default WatchPage;
