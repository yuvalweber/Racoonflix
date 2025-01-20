import React, { useState } from "react";
import axios from "axios"; // Import axios
import "./card.css";

const MovieManagement = ({ action }) => {
  const [movieData, setMovieData] = useState({
    title: "",
    category: "",
    imageUrl: "",
    videoUrl: "",
    year: "",
    duration: "",
    director: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setMovieData({ ...movieData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (action === "Create") {
      // Check if all requires fields are filled
    if (
        !movieData.title ||
        !movieData.imageUrl ||
        !movieData.videoUrl ||
        !movieData.duration ||
        !movieData.director
      ) {
        alert("Please fill in all required fields.");
        return;
      }
      // Send POST request to create a new movie using axios
      try {
        const response = await axios.post("/api/movies", movieData);
        if (response.status === 201) {
          alert("Movie created successfully!");
          // Optionally, clear the form
          setMovieData({
            title: "",
            category: "",
            imageUrl: "",
            videoUrl: "",
            year: "",
            duration: "",
            director: "",
          });
        } else {
          alert("Failed to create movie.");
        }
      } catch (error) {
        console.error("Error creating movie:", error.response || error.message); // Log error to get more info
        alert("An error occurred while creating the movie.");
      }
    }
  };

  const renderForm = () => {
    switch (action) {
      case "Create":
        return (
          <form onSubmit={handleSubmit} style={{ width: "50%", margin: "auto" }}>
            <h3>Create a New Movie</h3>
            <input
              type="text"
              placeholder="Movie Title (required)"
              className="form-control cardField"
              name="title"
              value={movieData.title}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Category"
              className="form-control cardField"
              name="category"
              value={movieData.category}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Image URL (required)"
              className="form-control cardField"
              name="imageUrl"
              value={movieData.imageUrl}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Video URL (required)"
              className="form-control cardField"
              name="videoUrl"
              value={movieData.videoUrl}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Year"
              className="form-control cardField"
              name="year"
              value={movieData.year}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Duration (required)"
              className="form-control cardField"
              name="duration"
              value={movieData.duration}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Director (required)"
              className="form-control cardField"
              name="director"
              value={movieData.director}
              onChange={handleInputChange}
            />
            <button type="submit" className="btn btn-primary">
              Create Movie
            </button>
          </form>
        );
      // Handle other cases (Delete, Update) here
      default:
        return null;
    }
  };

  return <div>{renderForm()}</div>;
};

export default MovieManagement;
