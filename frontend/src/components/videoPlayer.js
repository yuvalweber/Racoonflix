import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ReactPlayer from 'react-player';
import './videoPlayer.css'; // Import CSS for styling

axios.defaults.baseURL = 'http://localhost:8080';

const VideoPlayer = () => {
  const [videos, setVideos] = useState([]);
  const [randomVideo, setRandomVideo] = useState('');

  useEffect(() => {
		const token = localStorage.getItem('token');
		if (token) {
		  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
		}
	  }, []); // Set the Authorization header once on mount

  // Fetch videos from the database/API using Axios
  useEffect(() => {
    const fetchVideos = async () => {
      try {
        const response = await axios.get('api/movies');
        setVideos(response.data);
      } catch (error) {
        console.error('Error fetching videos:', error);
      }
    };

    fetchVideos();
  }, []);

  // Select a random video once videos are fetched
  useEffect(() => {
    if (videos.length > 0) {
      const randomIndex = Math.floor(Math.random() * videos.length);
      setRandomVideo(videos[randomIndex].trailer); // Assuming 'trailer' contains the video URL
    }
  }, [videos]);

  return (
    <div className="video-player-wrapper">
      {/* Title */}
      <h2 className="video-title">Admins Recommendation</h2>

      {/* Video Player */}
      {randomVideo && (
        <ReactPlayer
          url={randomVideo}
          playing={true} // Auto play enabled
          controls={true} // Show controls
          muted={true} // Unmute by default
          loop={true} // Disable looping
          width="300px"
          height="200px"
        />
      )}
    </div>
  );
};

export default VideoPlayer;
