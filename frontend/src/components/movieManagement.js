import React, { useState } from "react";
import axios from "axios"; // Import axios
import "./card.css";

// MovieManagement component
const MovieManagement = ({ action }) => {
  const [movieData, setMovieData] = useState({
    id: "",
    title: "",
    category: [], // Array for multiple categories
    image: "",
    trailer: "",
    year: "",
    duration: "",
    director: "",
  });

  // Handle input change
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setMovieData({ ...movieData, [name]: value });
  };

  // Handle category change
  const handleCategoryChange = (index, value) => {
    const updatedCategories = [...movieData.category];
    updatedCategories[index] = value;
    setMovieData({ ...movieData, category: updatedCategories });
  };

  // Add category field
  const addCategoryField = () => {
    setMovieData({ ...movieData, category: [...movieData.category, ""] });
  };

  // Remove category field
  const removeCategoryField = (index) => {
    const updatedCategories = movieData.category.filter((_, i) => i !== index);
    setMovieData({ ...movieData, category: updatedCategories });
  };

  // Initial movie data after form submission
  const initialMovieData = {
    id: "",
    title: "",
    category: [],
    image: "",
    trailer: "",
    year: "",
    duration: "",
    director: "",
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Check if create, delete, or update action
    // Create movie
    if (action === "Create") {
      try {
        const movieDataCopy = { ...movieData };
        delete movieDataCopy.id;
        // Check if required fields are filled
        if (
          !movieDataCopy.title ||
          !movieDataCopy.image ||
          !movieDataCopy.trailer ||
          !movieDataCopy.duration ||
          !movieDataCopy.director
        ) {
          alert("Please fill in all required fields.");
          return;
        }
        // Post request to create movie
        const response = await axios.post("/api/movies", movieDataCopy);
        if (response.status === 201) {
          alert("Movie created successfully!");
          setMovieData({ ...initialMovieData });
        } else {
          alert("Failed to create movie.");
        }
      } catch (error) {
        console.error("Error processing request:", error.response || error.message);
        alert(error.response.data?.errors || "An error occurred.");
      }
    }

    // Delete movie
    if (action === "Delete") {
      try {
        // Check if ID is provided
        if (!movieData.id) {
          alert("Please provide the ID of the movie to delete.");
          return;
        }
        // Delete request to delete movie
        const response = await axios.delete(`/api/movies/${movieData.id}`);
        if (response.status === 204) {
          alert("Movie deleted successfully!");
          setMovieData({ ...initialMovieData });
        } else {
          alert("Failed to delete movie.");
        }
      } catch (error) {
        console.error("Error processing request:", error.response || error.message);
        alert(error.response.data?.errors || "An error occurred.");
      }
    }

    // Update movie
    if (action === "Update") {
      try {
        // Check if ID is provided
        if (!movieData.id) {
          alert("Please provide the ID of the movie to update.");
          return;
        }
        const movieDataCopy = { ...movieData };
        delete movieDataCopy.id;
        // put request to update movie
        const response = await axios.put(`/api/movies/${movieData.id}`, movieDataCopy);
        if (response.status === 204) {
          alert("Movie updated successfully!");
          setMovieData({ ...initialMovieData });
        } else {
          alert("Failed to update movie.");
        }
      } catch (error) {
        console.error("Error processing request:", error.response || error.message);
        alert(error.response.data?.errors || "An error occurred.");
      }
    }
  };

  // Render form based on action
  const renderForm = () => {
    switch (action) {
      case "Create":
        return (
          // Create movie form
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
              placeholder="Image URL (required)"
              className="form-control cardField"
              name="image"
              value={movieData.image}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Trailer URL (required)"
              className="form-control cardField"
              name="trailer"
              value={movieData.trailer}
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
            <div>
              <label>Categories:</label>
              {movieData.category.map((cat, index) => (
                <div key={index} style={{ display: "flex", alignItems: "center", marginBottom: "8px" }}>
                  <input
                    type="text"
                    placeholder={`Category ${index + 1}`}
                    className="form-control cardField"
                    value={cat}
                    onChange={(e) => handleCategoryChange(index, e.target.value)}
                  />
                  {movieData.category.length > 1 && (
                    <button
                      type="button"
                      className="btn btn-danger"
                      style={{ marginLeft: "8px" }}
                      onClick={() => removeCategoryField(index)}
                    >
                      Remove
                    </button>
                  )}
                </div>
              ))}
              <button type="button" className="btn btn-secondary" onClick={addCategoryField}>
                Add Category
              </button>
            </div>
            <button type="submit" className="btn btn-primary">
              Create Movie
            </button>
          </form>
        );

      case "Delete":
        return (
          // Delete movie form
          <form onSubmit={handleSubmit} style={{ width: "50%", margin: "auto" }}>
            <h3>Delete a Movie</h3>
            <input
              type="text"
              placeholder="Movie ID (required)"
              className="form-control cardField"
              name="id"
              value={movieData.id}
              onChange={handleInputChange}
            />
            <button type="submit" className="btn btn-danger">
              Delete Movie
            </button>
          </form>
        );

      case "Update":
        return (
          // Update movie form
          <form onSubmit={handleSubmit} style={{ width: "50%", margin: "auto" }}>
            <h3>Update a Movie</h3>
            <input
              type="text"
              placeholder="Movie ID (required)"
              className="form-control cardField"
              name="id"
              value={movieData.id}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Movie Title"
              className="form-control cardField"
              name="title"
              value={movieData.title}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Image URL"
              className="form-control cardField"
              name="image"
              value={movieData.image}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Trailer URL"
              className="form-control cardField"
              name="trailer"
              value={movieData.trailer}
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
              placeholder="Duration"
              className="form-control cardField"
              name="duration"
              value={movieData.duration}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Director"
              className="form-control cardField"
              name="director"
              value={movieData.director}
              onChange={handleInputChange}
            />
            <div>
              <label>Categories:</label>
              {movieData.category.map((cat, index) => (
                <div key={index} style={{ display: "flex", alignItems: "center", marginBottom: "8px" }}>
                  <input
                    type="text"
                    placeholder={`Category ${index + 1}`}
                    className="form-control cardField"
                    value={cat}
                    onChange={(e) => handleCategoryChange(index, e.target.value)}
                  />
                  {movieData.category.length > 0 && (
                    <button
                      type="button"
                      className="btn btn-danger"
                      style={{ marginLeft: "8px" }}
                      onClick={() => removeCategoryField(index)}
                    >
                      Remove
                    </button>
                  )}
                </div>
              ))}
              <button type="button" className="btn btn-secondary" onClick={addCategoryField}>
                Add Category
              </button>
            </div>
            <button type="submit" className="btn btn-primary">
              Update Movie
            </button>
          </form>
        );

      default:
        return <p>Invalid action.</p>;
    }
  };

  return <div>{renderForm()}</div>;
};

export default MovieManagement;
