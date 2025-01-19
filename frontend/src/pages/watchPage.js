import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';	
import WatchNavbar from '../components/watchNavbar.js'; // ה-Navbar החדש
import WatchPlayer from '../components/watchPlayer'; // נגן הווידאו
import '../components/watchPage.css'; // סגנונות עמוד הצפייה

const WatchPage = () => {
  const location = useLocation();
  const navigate = useNavigate();

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
