import React, { useEffect, useState } from 'react';
import axios from 'axios';  // Import axios for API calls
import ReactPlayer from 'react-player';  // Install react-player for more features

const VideoPlayer = () => {  // Changed function name to start with uppercase 'V'
  const [videos, setVideos] = useState([]);
  const [randomVideo, setRandomVideo] = useState('');

  // Fetch videos from the database/API using Axios
  useEffect(() => {
    const fetchVideos = async () => {
      try {
        // Replace with your actual API endpoint
        const response = await axios({url: 'http://localhost:8080/api/movies', method: 'get', headers: {'x-user': '<censored>'}});
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
      setRandomVideo(videos[randomIndex].trailer); // Assuming each video object has a 'url' property
    }
  }, [videos]);

  return (
    <div className="video-container">
      <h2 className="text-center">Admins recommendation</h2>

      {/* Video Player */}
      {randomVideo && (
        <ReactPlayer
          url={randomVideo}  // URL of the random video
          playing={true}      // Auto play the video
          controls={true}     // Show controls
          width="100%"        // Full width
          height="500px"      // Set height as needed
        />
      )}
    </div>
  );
};

export default VideoPlayer;
