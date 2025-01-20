import React, {useEffect} from 'react';
import { useNavigate, useLocation} from 'react-router-dom';	
import WatchNavbar from '../components/watchNavbar.js'; // ה-Navbar החדש
import WatchPlayer from '../components/watchPlayer'; // נגן הווידאו
import '../components/watchPage.css'; // סגנונות עמוד הצפייה

const WatchPage = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    // Set overflow to hidden when the component is loaded
    document.documentElement.style.overflow = "hidden"; // For <html>
    document.body.style.overflow = "hidden"; // For <body>
	document.documentElement.style.height = "100%"; // For <html>
	document.body.style.height = "100%"; // For <body>
	document.documentElement.style.margin = "0"; // For <html>
	document.body.style.margin = "0"; // For <body>
	document.documentElement.style.padding = "0"; // For <html>
	document.body.style.padding = "0"; // For <body>

    return () => {
      // Clean up: Reset overflow when leaving the page
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

  // get the videoUrl from the location state
  const videoUrl = location.state?.trailer;

  // if there is no videoUrl, navigate back to the home page
  if (!videoUrl) {
	navigate('/');
	return null;
  }



  return (
    <div className="watch-page">
      {/* ה-Navbar החדש */}
      <WatchNavbar />

      {/* נגן הווידאו */}
      <div className="video-section">
        <WatchPlayer videoUrl={videoUrl} />
      </div>
    </div>
  );
};

export default WatchPage;
