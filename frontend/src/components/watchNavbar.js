import React from 'react';
import { useNavigate } from 'react-router-dom'; // לשימוש בניווט
import '../components/watchNavbar.css'; // סגנונות מותאמים אישית

const WatchNavbar = () => {
  const navigate = useNavigate();

  return (
    <div className="watch-navbar">
      {/* חץ אחורה */}
      <button className="back-button" onClick={() => navigate(-1)}>
        ← 
      </button>
    </div>
  );
};

export default WatchNavbar;
