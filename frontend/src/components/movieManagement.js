import React, { useState } from "react";
import axios from "axios"; // Import axios
import "./card.css";

const MovieManagement = ({ action }) => {
  const [movieData, setMovieData] = useState({
    id: "", 
    title: "",
    category: [],
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
            //remove id from movieData
            delete movieData.id;
            // Validation for required fields
            if (
            !movieData.title ||
            !movieData.image ||
            !movieData.trailer ||
            !movieData.duration ||
            !movieData.director
            ) {
            alert("Please fill in all required fields.");
            return;
            }
            const response = await axios.post("/api/movies", movieData);
            if (response.status === 201) {
            alert("Movie created successfully!");
            setMovieData({ ...initialMovieData });
            } else {
            alert("Failed to create movie.");
            // Add the id back to movieData
            setMovieData({ ...initialMovieData });        
            }
        } catch (error) {
            console.error("Error processing request:", error.response || error.message);
            alert(error.response.data.errors);
            setMovieData({ ...initialMovieData });
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
              setMovieData({...initialMovieData});      
            } else {
              alert("Failed to delete movie.");
              setMovieData({...initialMovieData});           
            }
        } catch (error) {
            console.error("Error processing request:", error.response || error.message);
            alert(error.response.data.errors); 
            setMovieData({...initialMovieData}); 
        }  
      }

      if (action === "Update") {
       try {
          if (!movieData.id) {
            alert("Please provide the ID of the movie to update.");
            return;
          }
          var movieId = movieData.id;
          delete movieData.id;
          if (movieData.category === "") {
            delete movieData.category;
          }
          console.log(movieData);
          console.log(movieId);
          const response = await axios.put(`/api/movies/${movieId}`, movieData);
          if (response.status === 200) {
            alert("Movie updated successfully!");
            setMovieData({...initialMovieData});
          } else {
            alert("Failed to update movie.");
            // Add the id back to movieData
            setMovieData({...initialMovieData});           
          }
        } catch (error) {
            console.error("Error processing request:", error.response || error.message);
            alert(error.response.data.errors); 
            setMovieData({...initialMovieData});     
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
              placeholder="Category"
              className="form-control cardField"
              name="category"
              value={movieData.category}
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
            <button type="submit" className="btn btn-success">
              Update Movie
            </button>
          </form>
        );

      default:
        return null;
    }
  };

  return <div>{renderForm()}</div>;
};

export default MovieManagement;
