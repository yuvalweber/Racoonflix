import React from 'react';

const WatchPlayer = ({ videoUrl }) => {
  return (
    <div className="video-container">
      <video
        controls
        autoPlay
        muted
      >
        <source src={videoUrl} type="video/mp4" />
      </video>
    </div>
  );
};

export default WatchPlayer;
