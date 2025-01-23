import React, { useState } from "react";
import axios from "axios"; // Import axios
import "./card.css";

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

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setMovieData({ ...movieData, [name]: value });
  };

  const handleCategoryChange = (index, value) => {
    const updatedCategories = [...movieData.category];
    updatedCategories[index] = value;
    setMovieData({ ...movieData, category: updatedCategories });
  };

  const addCategoryField = () => {
    setMovieData({ ...movieData, category: [...movieData.category, ""] });
  };

  const removeCategoryField = (index) => {
    const updatedCategories = movieData.category.filter((_, i) => i !== index);
    setMovieData({ ...movieData, category: updatedCategories });
  };

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

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (action === "Create") {
      try {
        const movieDataCopy = { ...movieData };
        delete movieDataCopy.id;

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

    if (action === "Delete") {
      try {
        if (!movieData.id) {
          alert("Please provide the ID of the movie to delete.");
          return;
        }

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

    if (action === "Update") {
      try {
        if (!movieData.id) {
          alert("Please provide the ID of the movie to update.");
          return;
        }
        const movieDataCopy = { ...movieData };
        delete movieDataCopy.id;
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
