import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ReactPlayer from 'react-player';
import './videoPlayer.css'; // Import CSS for styling

// Set the base URL for Axios requests
axios.defaults.baseURL = `http://${process.env.REACT_APP_API_SERVER}:${process.env.REACT_APP_API_PORT}`;

const VideoPlayer = () => {
  const [videos, setVideos] = useState([]); // State to store the list of videos
  const [randomVideo, setRandomVideo] = useState(''); // State to store the selected random video URL

  // Set the Authorization header once on mount
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
  }, []);

  // Fetch videos from the database/API using Axios
  useEffect(() => {
    const fetchVideos = async () => {
      try {
        const response = await axios.get('api/movies');
        setVideos(response.data); // Update state with the fetched videos
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
      setRandomVideo(videos[randomIndex].trailer); 
    }
  }, [videos]);

  return (
    <div className="video-player-wrapper">
      {/* Video Player */}
      {randomVideo && (
        <ReactPlayer
          url={randomVideo}
          playing={true} // Autoplay video
          controls={false} // Dont Show controls
          muted={true} // Mute video
          loop={true} // Loop video
          width="1300px"
          height="530px"
        />
      )}
    </div>
  );
};

export default VideoPlayer;
